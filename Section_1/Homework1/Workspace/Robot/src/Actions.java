import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;

public class Actions implements ActionsFunction {

	private static final Set<Action> action = new LinkedHashSet<Action>();

	@Override
	public Set<Action> actions(Object s) {

		Position robot_position = new Position(s);

		Position right = new Position(robot_position.x() + 1, robot_position.y());
		Position left = new Position(robot_position.x() - 1, robot_position.y());
		Position up = new Position(robot_position.x(), robot_position.y() + 1);
		Position down = new Position(robot_position.x(), robot_position.y() - 1);

		Right r = new Right();
		Left l = new Left();
		Down d = new Down();
		Up u = new Up();

		action.clear();
		action.add(r);
		action.add(l);
		action.add(d);
		action.add(u);

		// iterate Walls
		Iterator<Position> iter = Environment.walls.iterator();

		while (iter.hasNext()) {
			Position temp_wall = iter.next();

			if (temp_wall.equals(right) || (right.x() > Position.maxX))
				action.remove(r);

			if (temp_wall.equals(left) || (left.x() < 0))
				action.remove(l);

			if (temp_wall.equals(up) || (up.y() > Position.maxY))
				action.remove(u);

			if (temp_wall.equals(down) || (down.y() < 0))
				action.remove(d);

		}

		// System.out.println(action.toString());
		return action;

	}
}