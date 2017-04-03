public class Coordinate 
{
	private String name;
	private double x;
	private double y;
	
	public Coordinate(String name, double x, double y) 
	{
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public double distanceTo(Coordinate other)
	{
		double delX = Math.pow(other.getX() - this.getX(), 2);
		double delY = Math.pow(other.getY() - this.getY(), 2);
		return Math.sqrt(delX + delY);
	}
}
