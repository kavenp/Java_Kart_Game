import java.awt.geom.Point2D;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GameObject 
{
	private static double COLLISION_DISTANCE = 40;
	private Point2D.Double position;
	private Angle angle;
	private Image sprite;
	
	public GameObject(Point2D.Double position, Angle angle, String assetLoc) 
	throws SlickException
	{
		this.position = position;
		this.angle = angle;
		this.sprite = new Image(Game.ASSETS_PATH + assetLoc);
	}
	
	public Point2D.Double getPosition() {
		return position;
	}


	public void setPosition(Point2D.Double position) {
		this.position = position;
	}


	public Angle getAngle() {
		return angle;
	}


	public void setAngle(Angle angle) {
		this.angle = angle;
	}


	public Image getSprite() {
		return sprite;
	}


	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}
	
	public double distTo(GameObject other) {
		return this.position.distance(other.position);
	}
	
	public boolean collides(GameObject other) {
		return this.distTo(other) <= COLLISION_DISTANCE;
	}
	
	public boolean reachesY(double y) {
		double x = this.getPosition().getX();
		Point2D.Double yAhead = new Point2D.Double(x, y);
		return this.getPosition().distance(yAhead) <= COLLISION_DISTANCE;
	}
	
	public void render(Graphics g, Camera camera)
    {
        // Calculate the objects on-screen location from the camera
        int screen_x = (int) (position.getX() - camera.getLeft());
        int screen_y = (int) (position.getY() - camera.getTop());
        sprite.setRotation((float) angle.getDegrees());
        sprite.drawCentered(screen_x, screen_y);
    }
}
