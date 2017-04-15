package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{
	KDNode rootE, rootH, rootR;
	
    @Override
    public void buildIndex(List<Point> points) {
    	long starttime = System.currentTimeMillis();
    	List<Point> sortedByLon = new ArrayList<Point>(points);	//Will eventually get rid of this and embed into recursive build
    	sortedByLon = Quicksort.byLon(sortedByLon, 0, sortedByLon.size()-1);

    	long recursivetime = System.currentTimeMillis();
    	rootE = buildTree(keepCat(Category.EDUCATION, sortedByLon), rootE, true);
    	rootH = buildTree(keepCat(Category.HOSPITAL, sortedByLon), rootH, true);
    	rootR = buildTree(keepCat(Category.RESTAURANT, sortedByLon), rootR, true);
    	long endtime = System.currentTimeMillis();
    	System.out.println("Build\t| Total: " + (endtime - starttime) + ", List sort: " + (recursivetime - starttime) + ", Recursive: " + (endtime - recursivetime) + ", Size: " + sortedByLon.size());
    }
    
    //Restrict a list of points to a certain category
    public List<Point> keepCat(Category k, List<Point> l) {
    	List<Point> rl = new ArrayList<Point>();
    	for (int i = 0; i < l.size(); i++) {
    		if (l.get(i).cat.equals(k))
    			rl.add(l.get(i));
    	}
    	return rl;
    }
    
    public KDNode buildTree(List<Point> p, KDNode parent, boolean lon) {

    	KDNode T = new KDNode(p.get((p.size()-1)/2), parent);	//Get the median
    	p.remove((p.size()-1)/2);	//Remove the median from the list of points

    	switch (p.size()) {
    	case 0:	//If there are no more points in the set, don't create any more children
    		break;
    		
    	case 1:
    		boolean isLeft;	// Check if the remaining node is left or right
    		if (lon)	//Decide what is left or right based off the current layer
    			isLeft = p.get(0).lon > T.value.lon;
    		else
    			isLeft = p.get(0).lat > T.value.lat;

    		if (isLeft)
    			T.left = buildTree(p, T, !lon);
    		else
    			T.right = buildTree(p, T, !lon);
    		break;

    	default:
    		List<Point> left = new ArrayList<Point>(p);
    		List<Point> right = new ArrayList<Point>(p);
    		left = left.subList(0, (left.size()/2));	//Points lower than T
    		right = right.subList((right.size()/2), right.size());	//Points higher than T
    		
    		//Decide what is left or right based off the current layer
    		//Sort for the next level
    		if (lon) {
    			left = Quicksort.byLat(left, 0, left.size()-1);
    			right = Quicksort.byLat(right, 0, right.size()-1);
    		} else {
    			left = Quicksort.byLon(left, 0, left.size()-1);
    			right = Quicksort.byLon(right, 0, right.size()-1);
    		}
    		T.left = buildTree(left, T, !lon);	//Build tree using points lower than T's value
    		T.right = buildTree(right, T, !lon);	//Build tree using points higher than T's value
    	}
    	return T;
    }
    
    @Override
    public List<Point> search(Point searchTerm, int k) {
    	long starttime = System.currentTimeMillis();
    	KDNode root = getRelTree(searchTerm);
    	List<KDNode> nodes = new ArrayList<KDNode>();
    	nodes = BSTSearch(root, searchTerm, k, nodes, true);
    	ArrayList<Point> results = new ArrayList<Point>();
    	
    	for (int i = nodes.size()-1; i >= 0; i--)	//	Populate results with points from nodes
    		results.add(nodes.get(i).value);
    	long endtime = System.currentTimeMillis();
    	System.out.println("Search\t| Total: " + (endtime - starttime) + ", K: " + k);
        return results;
    }

    @Override
    public boolean addPoint(Point point) {
    	System.out.print("\t");
    	long starttime = System.currentTimeMillis();
    	if (!isPointIn(point)){	// Check that the point doesn't already exist
    		addToTree(getRelTree(point), point, true);	// Add the point to the tree
    		long endtime = System.currentTimeMillis();
    		System.out.println("Add\t| Total: " + (endtime - starttime));
    	} else {
    		return false;
    	}
    	return true;
    }

    public KDNode addToTree(KDNode T, Point value, boolean lon) {
    	
    	KDNode left = T.left;
    	KDNode right = T.right;
    	
    	boolean goLeft;
		if (lon)
			goLeft = T.value.lat < value.lat;
		else
			goLeft = T.value.lon < value.lon;
		
		// If T is a leaf, create the new node.
    	if (left == null && right == null) {
    		if (goLeft)
    			T.left = new KDNode(value, T);
    		else
    			T.right = new KDNode(value, T);
    		
    	} else {	// Go next level down the tree in search of a leaf
    		if (goLeft) {
    			if (left != null)
    				return addToTree(left, value, !lon);
    			else
    				return addToTree(right, value, !lon);
    			
    		} else {
    			if (right != null)
    				return addToTree(right, value, !lon);
    			else
    				return addToTree(left, value, !lon);
    		}
    	}
    	return T;
    }
    
    @Override
    public boolean deletePoint(Point point) {
    	long starttime = System.currentTimeMillis();
    	List<KDNode> search = new ArrayList<KDNode>();
    	search = BSTSearch(getRelTree(point), point, 1, search, true);	// Check that the point exists in the tree
    	KDNode n = search.get(0);

        if (point.equals(n.value)) {
        	
        	if (n.left == null && n.right == null) {	//	If the node to be deleted has no children, delete it outright
        		if (n.equals(n.parent.left))	//	find out whether the deleted node is the left or right node of its parent
            		n.parent.left = null;	//	update parent reference
            	else
            		n.parent.right = null;	//	update parent reference
        		
        	} else if (n.left == null) {	//If the node has only a right child, replace the deleted node with the right child
        		n.parent.right = n.right;	//update parent reference
        		n.right.parent = n.parent;	//update child reference
        		
        	} else if (n.right == null) {	//If the node has only a left child, replace the deleted node with the right child
        		n.parent.left = n.left;	//update parent reference
        		n.left.parent = n.parent;	//update child reference
        		
        	} else {	//If the node has 2 children
            	KDNode a = getInnerLeftNode(n.right);	// Set the replacement to be the innermost child node
            	
            	deletePoint(a.value);	// Delete the original node of the replacement
            	
            	if (n.equals(n.parent.left))	// Update parent references
            		n.parent.left = a;
            	else
            		n.parent.right = a;
            	
            	//update the replacement node's references
            	//TODO: look over this
       			a.parent = n.parent;
       			a.left = n.left;
       			a.right = n.right;
        	}
        	long endtime = System.currentTimeMillis();
        	System.out.println("Delete\t| Total: " + (endtime - starttime));
        	return true;
        } else {
        	return false;	
        }
    }

    @Override
    // Searches with k = 1, if first in array is the same as term, return true
    public boolean isPointIn(Point point) {
    	List<Point> search = search(point, 1);
    	if (search.size() > 0)
    		return point.equals(search.get(0));
    	return false;
        
    }
    
    // Recursive search
	List<KDNode> BSTSearch(KDNode T, Point value, int k, List<KDNode> c, boolean lon) {
	
		if (T == null) {
			return c;
		}
		KDNode left = T.left;
		KDNode right = T.right;
	
		if (left == null && right == null) {	//If there are no children, search no further
	
		} else if (left == null) {	//If there is no left child, search the right
			c = BSTSearch(right, value, k, c, !lon);
	
		} else if (right == null) {	//If there is no right child, search the left
			c = BSTSearch(left, value, k, c, !lon);
	
		} else {
			//Choose which path to go down based on layer number
			//Also get bounding dist between current node and child node
	
			boolean goLeft = false;
			double divDist = 0;	//	Get distance in the relevant dimension from the current node to the search
			if (lon) {	//	Check which branch to search based on the current layer
				goLeft = T.value.lat <= value.lat;
				divDist = Math.abs(T.value.lat - value.lat);

			} else {
				goLeft = T.value.lon <= value.lon;
				divDist = Math.abs(T.value.lon - value.lon);
			}
	
			if (goLeft) {
				c = BSTSearch(left, value, k, c, !lon);	//	Search the branch
				//	If there aren't enough entries in the results OR alternate branch could 
				//	potentially have closer nodes than the farthest in the results list
				if (c.size() < k || divDist < c.get(0).value.distTo(value))
					c = BSTSearch(right, value, k, c, !lon);	//	Search alternate branch
			} else {
				c = BSTSearch(right, value, k, c, !lon);
				if (c.size() < k || divDist < c.get(0).value.distTo(value))
					c = BSTSearch(left, value, k, c, !lon);
			}
		}
	
		if (c.size() == 0) {
			c.add(T);	//If there are no entries in the results, add T to results
		}
		else if (k == 1) {	//Alternate case for when k = 1
			if (c.get(0).value.distTo(value) > T.value.distTo(value)) {
				c.remove(0);
				c.add(T);
			}
		}
		else if (c.size() < k) {	//If the search hasn't reached its quota, add the node in order
			insertInOrder(T, c, value);
		}
		else {	//	If the node has met its quota
			for (int i = 0; i < c.size(); i++) {
				if (c.get(i) != null) {
					//	If T is closer than the first not null value in the list
					//	Remove the value and insert T in order
					if (c.get(i).value.distTo(value) > T.value.distTo(value)) {
						c.remove(i);
						c = insertInOrder(T, c, value);
					}
					break;
				}
			}
		}
		return c;
	}

	// Adds node to correct location in list relative to a value specified
    public List<KDNode> insertInOrder(KDNode T, List<KDNode> c, Point value) {

    	for (int i = 0; i < c.size(); i++) {
			if (c.get(i) != null) {
				if (i == c.size()-1)
					c.add(T);
				else if (c.get(i).value.distTo(value) < T.value.distTo(value))
					c.add(i, T);
				else
					continue;	//	Only continue if nothing is added
				break;
			}
		}
		return c;
    }
    
    //Used for when a node with multiple children is deleted
    public KDNode getInnerLeftNode(KDNode root) {
    	
    	if (root == null)
    		return null;
    	
    	KDNode c = getInnerLeftNode(root.left);
    	if (c != null)
    		return c;
    	else
    		return root;
    }
    
    //Switch case for getting the relevant tree
    public KDNode getRelTree(Point point) {
    	switch (point.cat) {
    	case EDUCATION: return rootE;
    	case RESTAURANT: return rootR;
    	case HOSPITAL:return rootH;
    	default: return null;
    	}
    }
}