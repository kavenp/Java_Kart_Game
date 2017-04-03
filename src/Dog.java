import java.awt.geom.Point2D;
import org.newdawn.slick.SlickException;

public class Dog extends AIPlayer {
	private static final String dogAsset = "/karts/dog.png";
	private static final Point2D.Double initPos = new Point2D.Double(1404, 13086);
	private static final double dogBehindV = 1.1;
	private static final double dogAheadV = 0.9;
	
	public Dog() 
	throws SlickException 
	{
		super(initPos, Angle.fromDegrees(0), dogAsset);
	}
	
	public void dogHonour(Player player) {
		if (this.compareTo(player) > 0) {
			//when dog is ahead of player
			this.setAcceleration(dogAheadV * ACCELERATION);
		}else if(this.compareTo(player) == 0){
			this.setAcceleration(ACCELERATION);
		}else{
			//when dog is behind player
			this.setAcceleration(dogBehindV * ACCELERATION);
		}
	}
	
	public void move(World world, Player player) {
		dogHonour(player);
		update(world, world.nextWaypoint(this, this.getCurrentWaypoint()));
	}
}
