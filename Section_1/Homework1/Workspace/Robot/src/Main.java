
import static java.lang.Math.pow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import aima.core.agent.Action;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.AStarSearch;
import aima.core.search.uninformed.DepthFirstSearch;

public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException, Exception {

		// Input file must be specified in args[1]
		BufferedReader reader = new BufferedReader(
				new FileReader(new File("/Users/domenicoalfano/Università/Master/Workspace/Robot/src/map.txt")));

		//"/Users/domenicoalfano/Università/Master/Workspace/Robot/src/map.txt"
		// In order to increment the resolution of the grid we use v as the
		// exponent of the power of 2;
		// so if v = 0 then m = 2^0 = 1 and the grid resolution is 16x16
		// if v = 1 then m = 2^1 = 2 and the grid resolution is 32x32
		// and so on...
		int v = 0;
		int m = (int) pow(2, v);
		System.out.println("M: " + m);

		// Read from the file and set the grid dimensions
		StringTokenizer tokens = new StringTokenizer(reader.readLine());
		int maxX = Integer.parseInt(tokens.nextToken()) * m;
		int maxY = Integer.parseInt(tokens.nextToken()) * m;
		System.out.println("maxX: " + maxX + " maxY: " + maxY);
		Position.maxX = maxX;
		Position.maxY = maxY;

		// Store the matrix of integers that will be written in the .bmp file
		// and initialize it with white cells
		int[][] occupancy = new int[maxX][maxY];
		for (int i = 0; i < maxX; i++)
			for (int j = 0; j < maxY; j++)
				occupancy[i][j] = 16777215;

		// Read from the file the start and the goal position
		// and store them in the occupancy matrix
		tokens = new StringTokenizer(reader.readLine());
		Position robot = new Position(Integer.parseInt(tokens.nextToken()) * m,
				Integer.parseInt(tokens.nextToken()) * m);
		System.out.println("Robot");
		System.out.println("X: " + robot.x() + " Y: " + robot.y());
		occupancy[robot.y()][robot.x()] = 255;
		tokens = new StringTokenizer(reader.readLine());
		Position finish = new Position(Integer.parseInt(tokens.nextToken()) * m,
				Integer.parseInt(tokens.nextToken()) * m);
		System.out.println("Goal");
		System.out.println("X: " + finish.x() + " Y: " + finish.y());
		occupancy[finish.y()][finish.x()] = 65280;

		// Build the environment
		Environment init = new Environment(robot);
		Environment.setFinish(finish);
		int numWalls = Integer.parseInt(reader.readLine());
		for (int i = 0; i < numWalls; i++) {
			tokens = new StringTokenizer(reader.readLine());
			Position temp = new Position(Integer.parseInt(tokens.nextToken()) * m,
					Integer.parseInt(tokens.nextToken()) * m);
			for (int x = 0; x < m; x++)
				for (int y = 0; y < m; y++) {
					int newX = temp.x() + x;
					int newY = temp.y() + y;
					Environment.addWall(new Position(newX, newY));
					occupancy[newY][newX] = 0x000000;
				}
		}

		// Store the occupancy matrix in a bitmap image
		new BMP().saveBMP("map.bmp", occupancy);

		// Use AIMA framework to solve the problem
		Actions actFunc = new Actions();
		Result resFunc = new Result();
		Goal goal = new Goal();
		Problem p = new Problem(init, actFunc, resFunc, goal);
		SearchAgent agent = null;
		long startTime = 0;
		long elapsedTime = 0;

		int choose = 1; // 1 For Depth Search | 2 For A* Star Search

		if (choose == 1) {
			Search search = new DepthFirstSearch(new GraphSearch());
			startTime = System.currentTimeMillis();
			agent = new SearchAgent(p, search);
			elapsedTime = (System.currentTimeMillis() - startTime);
		}
		
		if (choose == 2) {
			Search search = new AStarSearch(new GraphSearch(), new ManhattanHeursiticFuctionRobot());
			startTime = System.currentTimeMillis();
			agent = new SearchAgent(p, search);
			elapsedTime = (System.currentTimeMillis() - startTime);
		}

		printActions(agent.getActions());
		printInstrumentation(agent.getInstrumentation());
		System.out.println("Tempo: " + elapsedTime + " milliseconds");

		int xRobot = Environment.getRobot().x();
		int yRobot = Environment.getRobot().y();

		for (int i = 0; i < agent.getActions().size() - 1; i++) {
			Action a = agent.getActions().get(i);
			if (a.getClass() == Right.class) {
				xRobot = xRobot + 1;
				occupancy[yRobot][xRobot] = 0xff0000;
			}
			if (a.getClass() == Left.class) {
				xRobot = xRobot - 1;
				occupancy[yRobot][xRobot] = 0xff0000;
			}
			if (a.getClass() == Up.class) {
				yRobot = yRobot + 1;
				occupancy[yRobot][xRobot] = 0xff0000;
			}

			if (a.getClass() == Down.class) {
				yRobot = yRobot - 1;
				occupancy[yRobot][xRobot] = 0xff0000;
			}
		}
		new BMP().saveBMP("final_map.bmp", occupancy);

	}

	private static void printActions(List<Action> actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = actions.get(i).toString();
			System.out.println(action);
		}
	}

	private static void printInstrumentation(Properties properties) {
		Iterator<Object> keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}
	}

}