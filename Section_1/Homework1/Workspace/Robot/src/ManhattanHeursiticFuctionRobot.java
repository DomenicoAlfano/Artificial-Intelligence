import aima.core.search.framework.HeuristicFunction;

public class ManhattanHeursiticFuctionRobot implements HeuristicFunction {

	@Override
	public double h(Object state) {
		Position s = (Position) state;

		int nearestGoalDist = 0;
		nearestGoalDist = evaluateManhattanDistanceOf(s.x(), s.y(), Environment.goal.x(), Environment.goal.y());
		return nearestGoalDist;

	}

	private int evaluateManhattanDistanceOf(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
}
