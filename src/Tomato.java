import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.newdawn.slick.SlickException;

public class Tomato extends Item
{
	private static String assetLoc = "/items/tomato.png";
	private static double PLACEMENT_DIST = 40;
	
	public Tomato(Double position) 
			throws SlickException {
		super(position, assetLoc);
	}

	@Override
	public void use(Player player, World world) 
	throws SlickException 
	{
		Angle angle = player.getAngle();
		double x = player.getX() + angle.getXComponent(PLACEMENT_DIST);
		double y = player.getY() + angle.getYComponent(PLACEMENT_DIST);
		Point2D.Double tomatoPos = new Point2D.Double(x, y);
		Projectile newProj = new Projectile(tomatoPos, angle);
		world.addHazard(newProj);
		player.setItem(null);	
	}
	
}
