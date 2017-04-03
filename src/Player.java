/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import java.awt.geom.Point2D;

import org.newdawn.slick.SlickException;

/** The player's kart (Donkey).
 */
public class Player extends Kart
{
	private static final String donkeyAsset = "/karts/donkey.png";
	private static Point2D.Double initPos = new Point2D.Double(1332, 13086);
	private double rotate_speed = ROTATE_SPEED;
	private double acceleration = ACCELERATION;
	private static double BOOST_SPEED = 0.0008;
	private Item item;
	private boolean isBoosting = false;
	private int boostTimer = 0;

    /** Creates a new Player(donkey).
     * @param x The player's initial X location (pixels).
     * @param y The monster's initial Y location (pixels).
     * @param angle The player's initial angle.
     */
    public Player()
    throws SlickException
    {
    	super(initPos , Angle.fromDegrees(0), donkeyAsset);
    }

	public void update(double rotate_dir, double move_dir, boolean use_item, 
			World world) 
	throws SlickException
	{	
		if(this.reachesY(World.FINISH_LINE)) {
			this.endRank = world.getRanking();
		}
		if(!this.isReachedEnd()) {
			this.setMovedir(move_dir);
			this.setRotatedir(rotate_dir);
		}else{
			this.reachEnd();
		}
		if(this.isSpin() && (spinCounter != SPIN_TIME)) {
			spinCounter++;
			this.spinUpdate(world);
		}else{
			spinCounter = 0;
			this.setSpin(false);
			double velocity = this.getVelocity();
			// Modify the kart's angle
	        Angle rotateamount = new Angle(rotate_speed * this.getRotatedir());
	        this.setAngle(this.getAngle().add(rotateamount));
	        // Determine the friction of the current location
	        double friction = world.frictionAt((int) this.getX(), (int) this.getY());
	        //checks to see if still boosting
	        if(isBoosting && (boostTimer != Boost.getBOOST_TIME())) 
	        {
	        	//increment timer and set acceleration to boosted speed
	        	boostTimer++;
	        	acceleration = BOOST_SPEED;
	        	//player can only move in forwards direction while boosted
	        	move_dir = 1;
	        }else{
	        	boostTimer = 0;
	        	acceleration = ACCELERATION;
	        	isBoosting = false;
	        }
	        // Modify the kart's velocity. First, increase due to acceleration.
	        velocity += acceleration * this.getMovedir();
	        //System.out.println("Player velocity = " + velocity + "Player acceleration = " + acceleration);
	        // Then, reduce due to friction (this has the effect of creating a
	        // maximum velocity for a given coefficient of friction and
	        // acceleration)
	        velocity *= (1 - friction);
	        // Modify the position, based on velocity
	        // Calculate the amount to move in each direction
	        double amount = velocity;
	        // Store previous position and compute next position
	        double prev_x = this.getX();
	        double prev_y = this.getY();
	        double next_x = this.getX() + this.getAngle().getXComponent(amount);
	        double next_y = this.getY() + this.getAngle().getYComponent(amount);
	        if (this.item != null && use_item) {
	        	//player has item and uses the item held
	        	item.use(this, world);
	        }
	        //updates kart to next position
	        this.getPosition().setLocation(next_x, next_y);
	        //if updated position has collision revert back to previous position
	        if (world.blockingAt((int) next_x, (int) next_y) || world.collidesWith(this))
	        {
	            this.setVelocity(0);
	            this.getPosition().setLocation(prev_x, prev_y);
	        }
	        else
	        {
	            // Do not revert back to previous position
	        	this.setVelocity(velocity);
	        }
		}
		this.checkEnd();
    }
	
	public void setBoosting(boolean isBoosting) {
		this.isBoosting = isBoosting;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}
