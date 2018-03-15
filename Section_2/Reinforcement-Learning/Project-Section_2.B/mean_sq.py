# action constants
HIT = 0
STICK = 1

def calculate_mse(mc_values, sarsa_values):
    """ Given the true value function and another value function calculates Mean Squared Error
    :param mc_values: Monte Carlo value function
    :param sarsa_values: SARSA value function
    :return: MSE between MC and SARSA value functions
    """
    mse = 0
    for key in mc_values:
        mse += (sarsa_values[key] - mc_values[key]) ** 2
    return mse / len(mc_values.keys())
