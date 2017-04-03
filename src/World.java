/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.tiled.TiledMap;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{
	/** The string identifier to use for looking up the map friction. */
    private static final String MAP_FRICTION_PROPERTY = "friction";

	/** The world map (two dimensional grid of tiles).
     * The concept of tiles is a private implementation detail to World. All
     * public methods deal with pixels, not tiles.
     */
    private TiledMap map;
    
    private static double WAYPOINT_RADIUS = 250;
    public static final int FINISH_LINE = 1000;
    /** The player's kart. */
    private Player player = new Player();
    private Dog dog = new Dog();
    private Elephant elephant = new Elephant();
    private Octopus octopus = new Octopus();
    private ArrayList<Hazard> hazards = new ArrayList<Hazard>();
    private ArrayList<Kart> karts = new ArrayList<Kart>();
    
    /** The camera used to track what the screen should show. */
    private Camera camera;
    
    private Panel panel;
    
    private ArrayList<Item> itemList = new ArrayList<Item>();
    private ArrayList<Point2D.Double> waypointList = new ArrayList<Point2D.Double>();
    
    private static String waypointsFile = "data/waypoints.txt";
    private static String itemsFile = "data/items.txt";
    private static String oilCanName = "Oilcan";
    private static String tomatoName = "Tomato";
    private static String boostName = "Boost";
    
    /** Create a new World object. */
    public World()
    throws SlickException
    {
        map = new TiledMap(Game.ASSETS_PATH + "/map.tmx", Game.ASSETS_PATH);
        initializeKarts();
        camera = new Camera(Game.SCREENWIDTH, Game.SCREENHEIGHT, this.player);
        panel = new Panel();
        initializeWaypoints();
        initializeItems();

    }
    
    public void initializeKarts() {
    	karts.add(player);
    	karts.add(dog);
    	karts.add(elephant);
    	karts.add(octopus);
    }

    /** Get the width of the game world in pixels. */
    public int getWidth()
    {
        return map.getWidth() * map.getTileWidth();
    }

    /** Get the height of the game world in pixels. */
    public int getHeight()
    {
        return map.getHeight() * map.getTileHeight();
    }

    /** Update the game state for a frame.
     * @param rotate_dir The player's direction of rotation
     *      (-1 for anti-clockwise, 1 for clockwise, or 0).
     * @param move_dir The player's movement in the car's axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void update(double rotate_dir, double move_dir, boolean use_item)
    throws SlickException
    {
        for(Iterator<Item> iterator = itemList.iterator(); iterator.hasNext();) {
        	Item item = iterator.next();
        	item.pickUp(player);
        	if(item.getPickedUp()) {
        		iterator.remove();
        	}
        }
        for(Iterator<Hazard> hazardIter = hazards.iterator(); hazardIter.hasNext(); ) {
        	Hazard hazard = hazardIter.next();
        	hazard.update(this);
        	for(Iterator<Kart> kartIter = karts.iterator(); kartIter.hasNext(); ) {
        		Kart kart = kartIter.next();
        		if(hazard.collides(kart)) {
        			kart.setSpin(true);
        			hazardIter.remove();
        		}
        	}
        	if(hazard.getDestroy())  {
        		hazardIter.remove();
        	}
        }
        // Rotate and move the player by rotate_dir and move_dir
        player.update(rotate_dir, move_dir, use_item, this);
        dog.move(this, player);
        elephant.move(this);
        octopus.move(this, player);
        camera.follow(player);
    }
    
    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
    	// Calculate the camera location (in tiles) and offset (in pixels).
        // Render 24x18 tiles of the map to the screen, starting from the
        // camera location in tiles (rounded down). Begin drawing at a
        // negative offset relative to the screen, to ensure smooth scrolling.
        int cam_tile_x = camera.getLeft() / map.getTileWidth();
        int cam_tile_y = camera.getTop() / map.getTileHeight();
        int cam_offset_x = camera.getLeft() % map.getTileWidth();
        int cam_offset_y = camera.getTop() % map.getTileHeight();        
        int screen_tilew = camera.getWidth() / map.getTileWidth() + 2;
        int screen_tileh = camera.getHeight() / map.getTileHeight() + 2;
        
        map.render(
        		-cam_offset_x, -cam_offset_y,
        		cam_tile_x, cam_tile_y,
        		screen_tilew, screen_tileh);

        // Render all carts
        for(Kart kart: karts) {
        	kart.render(g, camera);
        }
        if(player.isReachedEnd()) {
        	panel.render(g, player.endRank, player.getItem());
        }else{
        	panel.render(g, getRanking(), player.getItem());
        }
        if(player.isReachedEnd()) {
        	renderEnd(g);
        }
        //check waypoints by uncommenting
        //renderWaypoints(g);
        renderItems(g);
        renderHazards(g);
        }

    /** Get the friction coefficient of a map location.
     * @param x Map tile x coordinate (in pixels).
     * @param y Map tile y coordinate (in pixels).
     * @return Friction coefficient at that location.
     */
    public double frictionAt(int x, int y)
    {
        int tile_x = x / map.getTileWidth();
        int tile_y = y / map.getTileHeight();
        int tileid = map.getTileId(tile_x, tile_y, 0);
        String friction =
        		map.getTileProperty(tileid, MAP_FRICTION_PROPERTY, null);
        return java.lang.Double.parseDouble(friction);
    }

    /** Determines whether a particular map location blocks movement.
     * @param x Map tile x coordinate (in pixels).
     * @param y Map tile y coordinate (in pixels).
     * @return true if the tile at that location blocks movement.
     */
    public boolean blockingAt(int x, int y)
    {
        return frictionAt(x, y) >= 1;
    }
    
    public int getRanking() {
    	Collections.sort(karts);
    	//sorts in ascending order
    	Collections.reverse(karts);
    	//reverses to get descending order
    	return karts.indexOf(player) + 1;
    	//+1 because index starts from 0
    }
    
    public void renderEnd(Graphics g) {
    	String endMsg = "You came " + Panel.ordinal(player.endRank); 
    	g.drawString(endMsg, Game.SCREENWIDTH/2, Game.SCREENHEIGHT/2);
    }
    
    public boolean collidesWith(Kart kart)
    {
    	for(Kart k : karts) {
    		if(kart != k) {
    			if(kart.collides(k)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public Point2D.Double getClosestWaypoint(AIPlayer kart) {
    	int index = 0;
    	double minDist = kart.getPosition().distance(waypointList.get(0));
    	//initialize minDist
    	for(Point2D.Double waypoint: waypointList) {
    		double dist = kart.getPosition().distance(waypoint);
    		if(dist < minDist) {
    			//keeps index of the smallest distance found between all waypoints
    			index = waypointList.indexOf(waypoint);
    			minDist = dist;
    		}
    	}
    	return waypointList.get(index);
    }
    
    
    public Point2D.Double nextWaypoint(AIPlayer kart, Point2D.Double currentWaypoint) 
    {
    	if(currentWaypoint == null) {
    		return waypointList.get(0);
    		//first waypoint
    	}
    	if(!waypointList.contains(currentWaypoint)) {
    		//waypoint is not in waypointlist
    		return getClosestWaypoint(kart);
    	}
    	if(kart.getPosition().distance(currentWaypoint) <= WAYPOINT_RADIUS) 
    	{
			for(int i = 0; i < waypointList.size() - 1; i++) {
				//-1 from waypointList size to avoid going past array bounds
				if(waypointList.get(i).equals(currentWaypoint)) 
				{
					return waypointList.get(i+1);
				}
			}
    	}
		return currentWaypoint;
    }
    
    public void initializeWaypoints() {
    	try
    	{
    		File file = new File(waypointsFile);
    		Scanner scanner = new Scanner(file);
    		
    		while(scanner.hasNextLine())
    		{
    			String line = scanner.nextLine();
    			String array[] = line.split(",");
    			Point2D.Double waypoint = new Point2D.Double(java.lang.Double.parseDouble(array[0]), 
    					java.lang.Double.parseDouble(array[1]));
    			waypointList.add(waypoint);
    		}
    		scanner.close();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    }

    public void initializeItems() 
    throws SlickException 
    {
    	try
    	{
    		File file = new File(itemsFile);
    		Scanner scanner = new Scanner(file);
    		while(scanner.hasNextLine())
    		{
    			String itemName = scanner.next();
    			String line = scanner.nextLine();
    			String array[] = line.split(",");
    			Point2D.Double itemLoc = new Point2D.Double(java.lang.Double.parseDouble(array[0]), 
    					java.lang.Double.parseDouble(array[1]));
    			if(itemName.equals(oilCanName)) {
    				OilCan oilcan = new OilCan(itemLoc);
    				itemList.add(oilcan);
    			}else if(itemName.equals(tomatoName)) {
    				Tomato tomato = new Tomato(itemLoc);
    				itemList.add(tomato);
    			}else if(itemName.equals(boostName)) {
    				Boost boost = new Boost(itemLoc);
    				itemList.add(boost);
    			}else{
    				System.err.println("Unrecognized item : " + itemName);
    			}
    		}
    		scanner.close();
    	} catch (FileNotFoundException e) {
    			e.printStackTrace();
    	}
    
    }
    
    public void renderHazards(Graphics g) {
    	for(Iterator<Hazard> iterator = hazards.iterator();iterator.hasNext(); ) {
    		Hazard hazard = iterator.next();
    		hazard.render(g, camera);
    	}
    }
    
    public void renderWaypoints(Graphics g) {
    	for(Point2D.Double p: waypointList ) {
    		g.setColor(Color.white);
    		g.drawOval((float)(p.getX()-camera.getLeft()), (float)(p.getY() - camera.getTop()), 10, 10);
    	}
    }
    
    public void renderItems(Graphics g) {
    	for(Iterator<Item> iterator = itemList.iterator(); iterator.hasNext(); ) {
    		Item item = iterator.next();
    		item.render(g, camera);
    	}
    }

	public void addHazard(Hazard newHazard) {
		hazards.add(newHazard);		
	}
    
    
}



