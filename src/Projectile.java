import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.newdawn.slick.SlickException;

public class Projectile extends Hazard {

	private static String assetLoc = "/items/tomato-projectile.png";
	private static double PROJ_VELOCITY = 1.7;
	
	public Projectile(Double position, Angle angle) 
	throws SlickException 
	{
		super(position, angle, assetLoc);
	}

	@Override
	public void update(World world) {
		double x = this.getPosition().getX() + this.getAngle().getXComponent(PROJ_VELOCITY);
		double y = this.getPosition().getY() + this.getAngle().getYComponent(PROJ_VELOCITY);
		Point2D.Double nextPos = new Point2D.Double(x, y);
		if(world.blockingAt((int) x,(int) y)) {
			this.setDestroy(true);
		}else{
			this.setPosition(nextPos);
		}
	}
}
