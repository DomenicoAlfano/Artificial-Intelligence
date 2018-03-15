import aima.core.agent.Action;

public class Right implements Action {

		@Override
		public boolean isNoOp() {
			return false;
		}
		
		@Override
		public String toString() {
			return "Right";
		}
		
}
