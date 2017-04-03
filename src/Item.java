import java.awt.geom.Point2D;

import org.newdawn.slick.SlickException;

public abstract class Item extends GameObject
{
	private Boolean pickedUp = false;
	
	public Item(Point2D.Double position, String assetLoc) 
		throws SlickException 
	{
		super(position, Angle.fromDegrees(0), assetLoc);
	}
	
	public void pickUp(Player player) {
		if(player.collides(this)) {
			player.setItem(this);
			pickedUp = true;
		}
	}
	
	public Boolean getPickedUp() {
		return pickedUp;
	}

	public abstract void use(Player player, World world) throws SlickException;
	
	public void drawImage(float panelX, float panelY, Item item) {
		item.getSprite().draw(panelX, panelY);
	}
	
	
}
