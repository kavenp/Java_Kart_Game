import java.awt.geom.Point2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Kart extends GameObject implements Comparable<Kart>
	{
	/** Rotation speed, in radians per ms. */
    public static final double ROTATE_SPEED = 0.004;
    /** Acceleration while the player is driving, in px/ms^2. */
    public static final double ACCELERATION = 0.0005;
    public static final double SPIN_SPEED = 0.008;
    private double velocity;
    private boolean isSpin = false;
    protected int spinCounter = 0;
    public static final int SPIN_TIME = 700;
    private boolean reachedEnd = false;
    private double movedir;
    private double rotatedir;
	protected int endRank = 0;
    
    public Kart(Point2D.Double position, Angle angle, String assetLoc) 
    throws SlickException
    {
    	super(position, angle, assetLoc);
    	this.velocity = 0;
    }
    
    public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getX() {
		return this.getPosition().getX();
	}

	public double getY() {
		return this.getPosition().getY();
	}

	public double getVelocity() {
		return velocity;
	}

	public void update(double rotate_dir, double move_dir, boolean use_item, World world) throws SlickException {
	}
	
	public void spinUpdate(World world) {
		Angle spinAmount = new Angle(SPIN_SPEED);
		this.setAngle(this.getAngle().add(spinAmount));
		double friction = world.frictionAt((int) this.getX(), (int) this.getY());
		velocity += ACCELERATION;
		velocity *= (1 - friction);
		double amount = velocity;
		double prev_x = this.getX();
        double prev_y = this.getY();
        double next_x = this.getX() + this.getAngle().getXComponent(amount);
        double next_y = this.getY() + this.getAngle().getYComponent(amount);
        this.getPosition().setLocation(next_x, next_y);
        if (world.blockingAt((int) next_x, (int) next_y) || world.collidesWith(this))
        {
            velocity = 0;
            this.getPosition().setLocation(prev_x, prev_y);
        }
	}
	
	public void checkEnd() {
		if(this.getPosition().getY() <= World.FINISH_LINE) {
			reachedEnd = true;
		}
	}
	
	@Override
	public int compareTo(Kart other){
		if(this.getY() < other.getY()) {
			return 1;
			//Kart is considered higher rank if lower in Y coordinate, 
		}else if(this.getY() > other.getY()) {
			return -1;
			//Kart is considered lower rank if higher in Y coordinate
		}else{
			return 0;
		}
	}

	public boolean isSpin() {
		return isSpin;
	}

	public void setSpin(boolean isSpin) {
		this.isSpin = isSpin;
	}
	
	public boolean isReachedEnd() {
		return reachedEnd;
	}

	public double getMovedir() {
		return movedir;
	}

	public void setMovedir(double movedir) {
		this.movedir = movedir;
	}

	public double getRotatedir() {
		return rotatedir;
	}

	public void setRotatedir(double rotatedir) {
		this.rotatedir = rotatedir;
	}

	public void reachEnd() {
			movedir = 0;
			rotatedir = 0;
	}
    
}
