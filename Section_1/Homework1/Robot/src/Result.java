

import aima.core.agent.Action;
import aima.core.search.framework.ResultFunction;

public class Result implements ResultFunction {
     
	@Override
	public Object result(Object s, Action a) {
		Position temp_robot = new Position(s);
		

		//Right
		if(a.getClass() == Right.class){
			int x = temp_robot.x();
			temp_robot.setX(x+1);
		}
		
		//Left
		if(a.getClass() == Left.class){
			int x = temp_robot.x();
			temp_robot.setX(x-1);
		}
		
		//Up
		if(a.getClass() == Up.class){
			int y = temp_robot.y();
			temp_robot.setY(y+1);
		}
		
		//Down
		if(a.getClass() == Down.class){
			int y = temp_robot.y();
			temp_robot.setY(y-1);
		}
		
		
		
		
		return temp_robot;
	}

	
	
}
