import java.awt.geom.Point2D;
import org.newdawn.slick.SlickException;

public class OilSlick extends Hazard{
	
	private static String assetLoc = "/items/oilslick.png";
	private static Angle angle = new Angle(0);
	
	public OilSlick(Point2D.Double position) 
	throws SlickException 
	{
		super(position, angle, assetLoc);
	}

	@Override
	public void update(World world) {
		
	}

}
