import aima.core.search.framework.GoalTest;

public class Goal implements GoalTest {

	@Override
	public boolean isGoalState(Object state) {

		if (state.equals(Environment.goal)) {
			return true;
		} else {
			return false;
		}
	}
}
