import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Creates seeable locations.
 */
public class Location extends Actor
{
    private int x;
    private int y;
    private boolean visited;
    
    /**
     * Builds a new dot and places it on the map.
     */
    public Location(int x, int y) 
    {
        this.x = x;
        this.y = y;
        this.visited = false;
    }
    
    /**
     * Creates a unique hash code based on this Location
     * @returns int
     */
    public int hashCode()
    {
        int hash = 0;
        hash += getX() - getY();
        if ( this.getY() > (getWorld().getHeight()/2) )
        {
            hash *= 31;
        }
        else
        {
            hash *= 30;
        }
        if ( this.getX() > (getWorld().getWidth()/2) )
        {
            hash *= 21;
        }
        else
        {
            hash *= 20;
        }
        return hash;
    }
    
    /**
     * Compares two Locations and determines if they are equal based
     * on a threshold distance or precise point.
     */
    public boolean equals( Location l )
    {
        double distance = 
        Math.sqrt(Math.pow((l.getX() - getX()), 2 )
                + Math.pow((l.getY() - getY()), 2 ));
        if ( distance > 1 )
        {
            return false;
        }
        return true;
    }
    
    public List<Location> getAllWithin( int radius )
    {
        return this.getObjectsInRange( radius, Location.class );
    }
    
    public boolean visited()
    {
        return visited;
    }
    
    /**
     * @returns String
     */
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }
}
