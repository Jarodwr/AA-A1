package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{
	List<Point> sortedByLat, sortedByLon;
	KDNode rootOfTree;
	
    @Override
    public void buildIndex(List<Point> points) {
        //Bubblesort by latitude and longitude
    	sortedByLat = points;
    	sortedByLon = points;
    	
    	boolean swapped = false;
    	
    	do {
        	for (int i = 0; i < sortedByLat.size(); i++) {
        		Point first = sortedByLat.get(i);
        		Point second = sortedByLat.get(i+1);
        		if (first.lat > second.lat) {
        			sortedByLat.set(i, second);
        			sortedByLat.set(i+1, first);
        			if (!swapped)
        				swapped = true;
        		}
        	}
    	} while(swapped);
    	
    	if (swapped)
    		swapped = false;
    	
    	do {
        	for (int i = 0; i < sortedByLon.size(); i++) {
        		Point first = sortedByLon.get(i);
        		Point second = sortedByLon.get(i+1);
        		if (first.lon > second.lon) {
        			sortedByLon.set(i, second);
        			sortedByLon.set(i+1, first);
        			if (!swapped)
        				swapped = true;
        		}
        	}
    	} while(swapped);
    	
    	//Build with starting direction and starting list
    	rootOfTree = buildTree(sortedByLat, sortedByLat);
    	
    }
    
    public KDNode buildTree(List<Point> points, List<Point> direction) {
    	int pointsSize = points.size();
    	KDNode c;
    	
    	//Need to do a switch case
    	switch(pointsSize) {
    	
    	case 0:
    		c = null;
    		break;
    		
    	case 1:
    		c = new KDNode(points.get(0));
    		break;
    		
    	case 2:
    		c = new KDNode(points.get(1));
    		c.setLeft(new KDNode(points.get(0)));
    		break;
    		
    	default:
        	c = new KDNode(points.get(pointsSize/2));
        	
        	List<Point> left = direction;
        	List<Point> right = direction;
        	
        	left.retainAll(points.subList(0, (pointsSize/2)-1));
        	right.retainAll(points.subList((pointsSize/2)+1, pointsSize));
        	
        	List<Point> nextDirection;
        	
        	if (direction.equals(sortedByLat)) {
        		nextDirection = sortedByLon;
        	} else {
        		nextDirection = sortedByLat;
        	}
        	c.setLeft(buildTree(left, nextDirection));
        	c.setRight(buildTree(right, nextDirection));
    		break;
    	}
    	
    	return c;

    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
        return new ArrayList<Point>();
    }

    @Override
    public boolean addPoint(Point point) {
        // To be implemented.
        return false;
    }

    @Override
    public boolean deletePoint(Point point) {
        // To be implemented.
        return false;
    }

    @Override
    public boolean isPointIn(Point point) {
        // To be implemented.
        return false;
    }

}
