import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.newdawn.slick.SlickException;

public class AIPlayer extends Kart {
	private Point2D.Double currentWaypoint;
	private Angle destAngle;
	private double rotate_speed = ROTATE_SPEED;
	private double acceleration = ACCELERATION;

	public AIPlayer(Double position, Angle angle, String assetLoc) throws SlickException {
		super(position, angle, assetLoc);
	}
	
	public void setRotate_speed(double rotate_speed) {
		this.rotate_speed = rotate_speed;
	}
	
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public Point2D.Double getCurrentWaypoint() {
		return currentWaypoint;
	}

	public void setCurrentWaypoint(Point2D.Double currentWaypoint) {
		this.currentWaypoint = currentWaypoint;
	}

	public void update(World world, Point2D.Double currentWaypoint) 
	{
		if(!this.isReachedEnd()) {
			this.setMovedir(1);
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
			//get next waypoint from world
			//currentWaypoint = world.nextWaypoint(this, currentWaypoint);
			this.currentWaypoint = currentWaypoint;
			//get vector from AI to next waypoint
			Point2D.Double vector = getVector(currentWaypoint);
			// Get the AI's angle to next waypoint
			destAngle = Angle.fromCartesian(vector.getX(), vector.getY());
			//Get the rotate_dir for AI
			this.setRotatedir(findRotateDir(destAngle));
			//Modify AI's angle with rotate_speed
			Angle rotateamount;
	        if(!this.getAngle().equals(destAngle))	
	        {
	        	rotateamount = new Angle(rotate_speed * this.getRotatedir());
	        }else{
	        	rotateamount = new Angle(0);
	        }
	        this.setAngle(this.getAngle().add(rotateamount));
	        // Determine the friction of the current location
	        double friction = world.frictionAt((int) this.getX(), (int) this.getY());
	        // Modify the kart's velocity. First, increase due to acceleration.
	        velocity += acceleration * this.getMovedir();
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
	        //updates kart to next position
	        this.getPosition().setLocation(next_x, next_y);
	        // If the intended destination is a blocking tile, do not move there
	        // (crash) -- instead, set velocity to 0
	        //if updated position has collision, revert to previous position
	        if (world.blockingAt((int) next_x, (int) next_y) || world.collidesWith(this))
	        {
	            this.setVelocity(0);
	            this.getPosition().setLocation(prev_x, prev_y);
	        }
	        else
	        {
	            //do not revert back to previous position
	        	this.setVelocity(velocity);
	        }
		}
        this.checkEnd();
	}
	
	public Point2D.Double getVector(Point2D.Double currentWaypoint) {
		double vectorX = currentWaypoint.getX() - this.getX();
		double vectorY = currentWaypoint.getY() - this.getY();
		Point2D.Double vector = new Point2D.Double(vectorX, vectorY);
		return vector;
	}
	
	public int findRotateDir(Angle angle) {
		double right = angle.subtract(this.getAngle()).getDegrees();
		//difference between angle we want and angle we are now
		double left = this.getAngle().subtract(angle).getDegrees();
		//difference between angle we are now and angle we want
		if(left < right) {
			//we want to turn in the direction of positive value
			return 1;
		}else{
			return -1;
		}
	}
}
