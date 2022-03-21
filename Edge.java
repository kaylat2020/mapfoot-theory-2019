import greenfoot.*;
import java.util.ArrayList;
/**
 * Connects locations.
 */
public class Edge implements Comparable<Edge>
{
    private int dis;
    private boolean visited;
    private ArrayList<Location> vertices;
    /**
     * Constructor for objects of class Edge
     */
    public Edge( Location l1, Location l2 )
    {
        this.visited = false;
        this.vertices = new ArrayList<Location>();
        vertices.add( l1 );
        vertices.add( l2 );
        
        this.dis = (int) Math.sqrt(Math.pow((l1.getX() - l2.getX()), 2 )
                                 + Math.pow((l1.getY() - l2.getY()), 2 ) );
    }
    
    public boolean wasVisited()
    {
        return visited;
    }
    public void setVisited( boolean n )
    {
        visited = n;
    }
    
    public ArrayList<Location> vertices()
    {
        return this.vertices;
    }
    
    public int getDistance()
    {
        return dis;
    }
    
    public void show( World w ) 
    {
        GreenfootImage bg = w.getBackground();
        bg.setColor( Color.GREEN );
        if ( wasVisited() )
        {
            bg.setColor( Color.RED );
        }
        bg.drawLine( vertices.get(0).getX(), vertices.get(0).getY(), 
                     vertices.get(1).getX(), vertices.get(1).getY() );
    }
    
    public int compareTo( Edge e )
    {
        if (vertices().equals(e.vertices()))
        {
            return 0;
        }
        return (int)(this.dis - (e.getDistance()));
    }
}
