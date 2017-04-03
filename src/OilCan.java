import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.newdawn.slick.SlickException;

public class OilCan extends Item
{
	private static String assetLoc = "/items/oilcan.png";
	private static double PLACEMENT_DIST = 40;
	private static Angle OPPOSITE_ANGLE = new Angle(Math.PI);
	
	public OilCan(Double position) 
			throws SlickException {
		super(position, assetLoc);
	}

	@Override
	public void use(Player player, World world) throws SlickException 
	{
		//gets the angle opposite of direction player is facing
		Angle angle = player.getAngle().add(OPPOSITE_ANGLE);
		double x = player.getX() + angle.getXComponent(PLACEMENT_DIST);
		double y = player.getY() + angle.getYComponent(PLACEMENT_DIST);
		Point2D.Double oilPos = new Point2D.Double(x, y);
		OilSlick newSlick = new OilSlick(oilPos);
		world.addHazard(newSlick);
		player.setItem(null);
	}
	
}
