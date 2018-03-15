
import java.util.HashSet;
import java.util.Set;

public class Environment {
    private static Position robot;
    public static Position goal;
    public static final Set<Position> walls = new HashSet<Position>();
   

    public static  Position getRobot() {
		return robot;
	}


	public static void setRobot(Position robot) {
		Environment.robot = robot;
	}


	public static Position getGoal() {
		return goal;
	}


	public static Set<Position> getWalls() {
		return walls;
	}


	public static void setGoal(Position goal) {
		Environment.goal = goal;
	}

	public Environment(Position robot) {
        Environment.robot = robot;
    }

	public static void setFinish(Position finish) {
		Environment.goal = finish;
	}

	public static void addWall(Position position) {
		walls.add(position);
	}

	

    //TODO
    //insert the required class methods
    //
}
