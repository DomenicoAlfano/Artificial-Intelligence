public class Position {
	public static int maxX;
	public static int maxY;

	public int x;
	public int y;

	public Position(int x, int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}



	public Position(Object s) {
		if(s.getClass() == Environment.class){
			Environment temp = (Environment)s;
			this.x = temp.getRobot().x();
			this.y = temp.getRobot().y();
		}
		
		if(s.getClass() == Position.class){
			Position temp = (Position)s;
			this.x = temp.x();
			this.y = temp.y();
		}
		
	}
	

	public int x() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int y() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

}
