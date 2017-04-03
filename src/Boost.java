import java.awt.geom.Point2D.Double;

import org.newdawn.slick.SlickException;

public class Boost extends Item
{
	private static String assetLoc = "/items/boost.png";
	//boost for 3 seconds = 3000 milliseconds
	private static int BOOST_TIME = 3000;
	
	public Boost(Double position) 
			throws SlickException {
		super(position, assetLoc);
	}
	
	public static int getBOOST_TIME() {
		return BOOST_TIME;
	}

	@Override
	public void use(Player player, World world) {
		player.setBoosting(true);
		player.setItem(null);
	}
	
}
