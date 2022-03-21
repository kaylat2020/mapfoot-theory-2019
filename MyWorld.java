import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Displays a (technically incorrect) map of the world.
 * @author tuckerka20
 * @version 5/23/19
 */
public class MyWorld extends World
{
    private GreenfootImage bg;
    private List<Location> locs;
    private Set<Edge> allEdges;
    private Map<Location, HashSet<Edge>> map;
    public MyWorld()
    {    
        super(961, 604, 1);
        setBackground("WorldMap.png");
        Greenfoot.setSpeed( 50 );
    }
    
    public HashSet<Edge> getEdges()
    {
        return (HashSet)allEdges;
    }
    
    /**
     * Builds amount of Locations on blue parts of the map.
     * @param amount
     */
    public void fill( int amount )
    {
        bg = getBackground();
        //Prevents StackOverflow (hopefully)
        /*
        if ( amount > 1000 )
        {
            throw new IndexOutOfBoundsException
            ( "Amount of locations must be less than 1000!" );
        }
        */
       
        //cycles through all locations needed
        while ( amount > 0 )
        {
            int newX = (int)(Math.random()* bg.getWidth()-1)+1;
            int newY = (int)(Math.random()* bg.getHeight()-1)+1;
            
            //cycles through randoms places until on land.
            while ( bg.getColorAt( newX, newY ).getRed() == 255)
            {
                newX = (int)(Math.random()* bg.getWidth()-1)+1;
                newY = (int)(Math.random()* bg.getHeight()-1)+1;
            }
            Location l = new Location( newX, newY );
            
            addObject( l, newX, newY );
            
            //prevent locations from being on top of each other.
            if ( l.getAllWithin( 3 ).size() > 0 )
            {
                removeObject( l );
            }
            else
            {
                amount--;
            }
        }
    }
    
    /**
     * Connects every location to each other.
     */
    public void completeGraph()
    {
        locs = getObjects( Location.class );
        if ( locs.size() == 0 )
        {
            return;
        }
        for ( int i = 0 ; i < locs.size() - 1 ; i++ )
        {
            for ( int l = i+1 ; l < locs.size() ; l++ )
            {
                Edge e = new Edge( locs.get(i), locs.get(l) );
                e.show( this );
            }
        }
    }
    
    
    /**
     * Builds n edges on the map.
     * @param n
     */
    public void buildRandomConnections( int n )
    {
        map = new HashMap<>();
        locs = getObjects( Location.class );
        if ( locs.size() == 0 )
        {
            return;
        }
        for ( Location l : locs )
        {
            allEdges = new HashSet<>();
            List<Location> nearby = l.getAllWithin( 500 );
            if ( nearby.size() > 0 )
            {
                for ( int i = 0 ; i < nearby.size() && n > 0 ; i++ )
                {
                    Edge e = new Edge( l , nearby.get( i ) );
                    e.show( this );
                    
                    allEdges.add( e );
                    
                    n--;
                }
                map.put( l , (HashSet)allEdges );
            }
        }
    }
    
    /**
     * Searches for any existing cycles in the graph.
     * @return true if a cycle is found, false if no cycles are possible.
     */
    public boolean hasCycle()
    {
        //TODO
        return false;
    }
    
    
    /**
     * Determines if a single edge would complete a cycle.
     * @param allEdges
     * @param e
     * @return true if a cycle (that contains edge e) is possible
     *  after the edge is created, false otherwise
     */
    public boolean formsCycle( Edge e )
    {
        /* Take one end of the edge and look through the collection of edges,
         * gathering each matching hash value from one of the edges ends,
         * then going to the other end location and finding more edges until a cycle
         * is/ is not found.
         */
        Stack<Edge> s = new Stack();
        Location first = e.vertices().get(0);
        Set<Location> locations = map.keySet();
        for ( Location l : locations )
        {
            HashSet<Edge> currentEdges = map.get(l);
            if ( currentEdges.size() != 0 )
            {
                //change Set to List for easier access to Edges
                List<Edge> edges = new ArrayList<>( currentEdges );
                for ( Edge edge : currentEdges )
                {
                    //creates a clone of the list of actual edges to mess around with
                    ArrayList<Edge> clone = new ArrayList<>( edges );
                    while ( edge.vertices().contains( first ) )
                    {
                        //finds the 
                        s.push( clone.remove( clone.indexOf(e) ) );
                        //TODO
                    }
                }
            }
        }
        return false;
    }
    
    public void drawEdges( List<Edge> edges )
    {
        for ( int i = 0 ; i < edges.size() ; i++ )
        {
            //shows the newest connection in red
            edges.get( i ).setVisited( true );
            edges.get( i ).show( this );
            
            //turns the last connection back to orange
            if ( i != 0 )
            {
                edges.get( i-1 ).setVisited( false );
                edges.get( i-1 ).show( this );
            }
            Greenfoot.delay( 50 );
        }
        //turns the last connection back to orange
        edges.get( edges.size()-1 ).setVisited( false );
        edges.get( edges.size()-1 ).show( this );
    }
    
    /**
     * Prim's algorithm.
     * minimum spanning tree.
     */
    public void minSpan1()
    {
        locs = new ArrayList<Location>(getObjects(Location.class));
        if ( locs.size() == 0 )
        {
            //ignores this method if there's no locations.
            return;
        }
        
        List<Location> visitedNodes = new ArrayList<>();
        List<Location> unvisitedNodes = locs.subList(1,locs.size());
        visitedNodes.add( locs.get(0) );
        List<Edge> edges = new ArrayList<>();
        while ( ! unvisitedNodes.isEmpty() )
        {
            /* track visited nodes. track unvisited nodes and go to the nearest one.
             * if there is no nearest unvisited node go back in the
             * visited node list until there is.
             */
            Edge shortest = new Edge( unvisitedNodes.get(0) , visitedNodes.get(0) );
            for ( int i = 0 ; i < visitedNodes.size() ; i++ )
            {
                for ( int j = 0 ; j < unvisitedNodes.size() ; j++ )
                {
                    Edge e = new Edge( visitedNodes.get(i) , unvisitedNodes.get(j) );
                    if ( shortest.getDistance() > e.getDistance() )
                    {
                        shortest = e;
                    }
                }
            }
            edges.add( shortest );
            visitedNodes.add( shortest.vertices().get(1) );
            unvisitedNodes.remove( shortest.vertices().get(1) );
        }
        //use drawedges method at a delayed speed
        drawEdges( edges );
    }
    /**
     * Kruskal's algorithm.
     */
    public void minSpan2()
    {
        
    }
    /**
     * Dijkstra's algorithm.
     */
    public void shortestPath()
    {
        
    }
}
