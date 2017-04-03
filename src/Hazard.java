import java.awt.geom.Point2D;
import org.newdawn.slick.SlickException;

public abstract class Hazard extends GameObject
{
	private boolean isDestroy = false;
	
	public Hazard(Point2D.Double position, Angle angle, String assetLoc) 
	throws SlickException 
	{
		super(position, angle, assetLoc);
	}
	
	public abstract void update(World world);

	public void setDestroy(boolean isDestroy) {
		this.isDestroy = isDestroy;
	}
	
	public boolean getDestroy() {
		return isDestroy;
	}

}
