import java.awt.geom.Point2D;
import org.newdawn.slick.SlickException;

public class Elephant extends AIPlayer{
	private static final String elephantAsset = "/karts/elephant.png";
	private static final Point2D.Double initPos = new Point2D.Double(1260, 13086);
	
	public Elephant() throws SlickException {
		super(initPos, Angle.fromDegrees(0), elephantAsset);
	}

	public void move(World world) {
		update(world, world.nextWaypoint(this, this.getCurrentWaypoint()));
	}
}
