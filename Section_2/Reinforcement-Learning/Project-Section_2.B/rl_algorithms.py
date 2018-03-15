from collections import defaultdict
from multiprocessing import Process
import environment
import eps_policy
import plotting
import pickle
import mean_sq

def monte_carlo(iterations=1000000, policy=eps_policy.epsilon_greedy, n_zero=100):
    """
    :param iterations: number of monte carlo iterations
    :param policy: exploration strategy to use
    :param n_zero: epsilon greedy constant (only applicable if epsilon greedy policy is used)
    :return: value function and the plot of the optimal value function
    """
    # (player, dealer, action) key
    value_function = defaultdict(float)
    # (player, dealer) key
    counter_state = defaultdict(int)
    # (player, dealer, action) key
    counter_state_action = defaultdict(int)
    # number of wins
    wins = 0

    print('Iterations completed:')
    for i in xrange(iterations):

        if (i % 500000) == 0:
            print(i)

        # create a new random starting state
        state = environment.State()
        # play a round
        observed_keys = []
        while not state.terminal:
            player = state.player_sum
            dealer = state.dealer_first_card

            # find an action defined by the policy
            epsilon = n_zero / float(n_zero + counter_state[(player, dealer)])
            action = policy(epsilon, value_function, state)
            observed_keys.append((player, dealer, action))

            # take a step
            [state, reward] = environment.step(state, action)

        # we have reached an end of episode
        if reward is not None:
            # update over all keys
            for key in observed_keys:
                # update counts
                counter_state[key[:-1]] += 1
                counter_state_action[key] += 1

                # update value function
                alpha = 1.0 / counter_state_action[key]
                value_function[key] += alpha * (reward - value_function[key])

        if reward == 1:
            wins += 1

    print('Wins: %.4f%%' % ((float(wins) / iterations) * 100))
    # plot the optimal value function
    plotting.plot_value_function(value_function)
    return value_function

def sarsa_lambda(l=0.9, max_episodes=5000, policy=eps_policy.epsilon_greedy,
                 n_zero=100, gamma=1, plot_learning_curve=True, multiproc=True):
    """
    :param l: lambda parameter
    :param max_episodes: stop learning after this many episodes
    :param policy: exploration strategy to use
    :param n_zero: epsilon greedy constant (only applicable if epsilon greedy policy is used)
    :param gamma: discounting rate
    :param plot_learning_curve: whether to turn on plotting of learning curve for lambda = 0 and 1
    :param multiproc: whether to use multiprocessing when doing plots or not
    :return: value function after max_episodes
    """
    # (player, dealer, action) key
    value_function = defaultdict(float)
    # (player, dealer) key
    counter_state = defaultdict(int)
    # (player, dealer, action) key
    counter_state_action = defaultdict(int)
    # no. of wins to calculate the percentage of wins at the end
    wins = 0

    # learning curve plotting
    if l in {0, 1} and plot_learning_curve:
        learning_curve = []
        try:
            mc_values = pickle.load(open("Data/MC_value_function.pickle", "rb"))
        except:
            mc_values = monte_carlo(iterations=1000000)

    for episode in range(max_episodes):

        # current (player, dealer, action)
        eligibility_trace = defaultdict(float)

        # initial state, action [SA..]
        state = environment.State()
        player_current = state.player_sum
        dealer_current = state.dealer_first_card
        epsilon = n_zero / float(n_zero + counter_state[(player_current, dealer_current)])
        action_current = policy(epsilon, value_function, state)

        while not state.terminal:

            # update counts
            counter_state[(player_current, dealer_current)] += 1
            counter_state_action[(player_current, dealer_current, action_current)] += 1

            # take a step, get reward [..R..]
            [state, reward] = environment.step(state, action_current)
            if reward is None:
                reward = 0

            # follow up state, action [..SA]
            player_next = state.player_sum
            dealer_next = state.dealer_first_card
            epsilon = n_zero / float(n_zero + counter_state[(player_next, dealer_next)])
            action_next = policy(epsilon, value_function, state)

            delta = reward + gamma * value_function[(player_next, dealer_next, action_next)] - \
                value_function[(player_current, dealer_current, action_current)]

            alpha = 1.0 / counter_state_action[(player_current, dealer_current, action_current)]

            eligibility_trace[(player_current, dealer_current, action_current)] += 1

            # update the values
            for key in value_function:
                value_function[key] += alpha * delta * eligibility_trace[key]
                eligibility_trace[key] *= gamma * l

            player_current = player_next
            dealer_current = dealer_next
            action_current = action_next

        # use it later to calculate the percentage of wins
        if reward == 1:
            wins += 1

        # get the episode MSE for plotting learning curve
        if l in {0, 1} and plot_learning_curve:
            learning_curve.append((episode, mean_sq.calculate_mse(mc_values, value_function)))

    # plot learning curve
    if l in {0, 1} and plot_learning_curve:
        if multiproc:
            # create a new process so computation can continue after plotting
            p = Process(target=plotting.plot_learning_curve, args=(learning_curve, l,))
            p.start()
        else:
            plotting.plot_learning_curve(learning_curve, l)

    # get the percentage of wins and mean square error when lambda is changed
    print('--------------------')
    print('Lambda: %.1f' % l)
    print('Wins: %.4f%%' % ((float(wins) / max_episodes) * 100))
    print('\n')

    return value_function