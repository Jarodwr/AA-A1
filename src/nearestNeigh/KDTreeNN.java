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
	boolean isLat = true;
	
    @Override
    public void buildIndex(List<Point> points) {
        //Bubblesort by latitude and longitude
    	//Bubblesort is placeholder, should be replaced with QSort
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
    	rootOfTree = buildTree(null, sortedByLat, sortedByLat);
    	
    }

    //currently list is backwards
    @Override
    public List<Point> search(Point searchTerm, int k) {
    	List<Point> results = new ArrayList<Point>();
    	results = goUpTree(searchTerm, goDownTree(searchTerm, rootOfTree), results, rootOfTree);
    	
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

    //Could be done better
    public KDNode buildTree(KDNode parent, List<Point> points, List<Point> direction) {
    	int pointsSize = points.size();
    	
    	switch(pointsSize) {
    	
    	case 0:
    		return null;
    		
    	case 1:
    		return new KDNode(points.get(0), parent);
    		
    	case 2:
    		KDNode c = new KDNode(points.get(1), parent);
    		c.setLeft(new KDNode(points.get(0), parent));
    		return c;
    		
    	default:
    		KDNode d = new KDNode(points.get(pointsSize/2), parent);
        	
        	List<Point> left = direction;
        	List<Point> right = direction;
        	
        	left.retainAll(points.subList(0, (pointsSize/2)-1));
        	right.retainAll(points.subList((pointsSize/2)+1, pointsSize));
        	
        	List<Point> nextDirection;
        	
        	//Make this more efficient
        	if (direction.equals(sortedByLat)) {
        		nextDirection = sortedByLon;
        	} else {
        		nextDirection = sortedByLat;
        	}
        	d.setLeft(buildTree(d, left, nextDirection));
        	d.setRight(buildTree(d, right, nextDirection));
    		return d;
    	}

    }
    
    public KDNode goDownTree(Point value, KDNode root) {
    	
    	KDNode left = root.getLeft();
    	KDNode right = root.getRight();
    	boolean isLeft = false;
    	
    	if (isLat) {
    		if (Math.abs(left.getValue().lat - value.lat) < Math.abs(right.getValue().lat - value.lat))
    			isLeft = true;

    	} else {
    		if (Math.abs(left.getValue().lon - value.lon) < Math.abs(right.getValue().lon - value.lon))
    			isLeft = true;
    	}
    	
    	isLat = !isLat;
    	if (isLeft) {
    		return goDownTree(value, left);
    	} else {
    		return goDownTree(value, right);
    	}
    }
    
    public List<Point> goUpTree(Point value, KDNode child, List<Point> closest, KDNode root) {
    	
    	Point closestValue = closest.get(closest.size());
    	Point parentValue = child.getParent().getValue();
    	double testSubDist;
    	boolean isLeft;
    	
    	//Checks whether or not to terminate this sub process
    	if (child.getParent().equals(root)) {
    		return closest;
    	}
    	
    	//Update closest value if parent is closer than the current closest value
    	if (parentValue.distTo(value) < closestValue.distTo(value)) {
    		closest.add(parentValue);
    	}
    	
    	//Look over this
    	if (isLat) {
        	testSubDist = Math.abs(closestValue.lat - parentValue.lat);
        	isLeft = closestValue.lat < parentValue.lat;
    	} else {
    		testSubDist = Math.abs(closestValue.lon - parentValue.lon);
        	isLeft = closestValue.lon < parentValue.lon;
    	}
    	
    	isLat = !isLat;
    	
    	//checks whether to check the other branch
    	if (closestValue.distTo(value) > testSubDist) {
    		if (isLeft) {
    			return goUpTree(value, goDownTree(value, child.getParent().getLeft()), closest, child.getParent());
    		} else {
    			return goUpTree(value, goDownTree(value, child.getParent().getRight()), closest, child.getParent());
    		}
    	}
    	
    	return goUpTree(value, child.getParent(), closest, root);
    }
    
    //Used for when a node with multiple children is deleted
    //Very unfinished? I don't know
    public KDNode getInnerNode(KDNode root) {
    	KDNode c = getInnerNode(root.getLeft());
    	if (c != null) {
    		return c;
    	} else {
    		return root;
    	}
    }
}