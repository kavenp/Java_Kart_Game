import java.awt.geom.Point2D;
import org.newdawn.slick.SlickException;

public class Octopus extends AIPlayer 
{
	private static final String octopusAsset = "/karts/octopus.png";
	private static final Point2D.Double initPos = new Point2D.Double(1476, 13086);
	private static double LOW = 100;
	private static double HIGH = 250;
	
	public Octopus() 
	throws SlickException 
	{
		super(initPos, Angle.fromDegrees(0), octopusAsset);
	}
	
	public boolean playerCheck(Player player) {
		if((LOW <= this.distTo(player)) && (this.distTo(player) <= HIGH)) {
			return true;
		}else{
			return false;
		}
	}
	
	public void move(World world, Player player) {
		if(playerCheck(player)) {
			this.update(world, player.getPosition());
		}else{
			this.update(world, world.nextWaypoint(this, this.getCurrentWaypoint()));
		}
	}
}
