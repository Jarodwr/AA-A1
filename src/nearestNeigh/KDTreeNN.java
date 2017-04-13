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
	KDNode rootE;
	KDNode rootH;
	KDNode rootR;
	
    @Override
    public void buildIndex(List<Point> points) {
        //Bubblesort by latitude and longitude
    	//Bubblesort is placeholder, should be replaced with QSort
    	sortedByLat = new ArrayList<Point>(points);
    	sortedByLon = new ArrayList<Point>(points);
    	
    	boolean swapped = false;
    	
    	do {
    		swapped = false;
        	for (int i = 0; i < sortedByLat.size()-1; i++) {
        		Point first = sortedByLat.get(i);
        		Point second = sortedByLat.get(i+1);
        		if (first.lat > second.lat) {
        			sortedByLat.remove(first);
        			sortedByLat.add(i+1, first);
        			swapped = true;
        		}
        	}
    	} while(swapped);
    	
    	do {
    		swapped = false;
        	for (int i = 0; i < sortedByLon.size()-1; i++) {
        		Point first = sortedByLon.get(i);
        		Point second = sortedByLon.get(i+1);
        		if (first.lon > second.lon) {
        			sortedByLon.remove(first);
        			sortedByLon.add(i+1, first);
        			swapped = true;
        		}
        	}
    	} while(swapped);
    	System.out.println();
    	for (Point p : sortedByLon) {
    		System.out.print("(lon: " + p.lon + ", lat: " + p.lat + "), ");
    	}
    	System.out.println();
    	for (Point p : sortedByLat) {
    		System.out.print("(lon: " + p.lon + ", lat: " + p.lat + "), ");
    	}
    	System.out.println();
    	//Build with starting direction and starting list
    	//rootOfTree = buildTree(sortedByLat, rootOfTree, 0);
    	rootE = buildTree(keepCat(Category.EDUCATION, sortedByLon), rootE, 0);
    	rootH = buildTree(keepCat(Category.HOSPITAL, sortedByLon), rootH, 0);
    	rootR = buildTree(keepCat(Category.RESTAURANT, sortedByLon), rootR, 1);
    }
    
    public List<Point> keepCat(Category k, List<Point> l) {
    	List<Point> rl = new ArrayList<Point>();
    	for (int i = 0; i < l.size(); i++) {
    		if (l.get(i).cat.equals(k))
    			rl.add(l.get(i));
    	}
    	return rl;
    }
    
    public KDNode buildTree(List<Point> p, KDNode parent, int layer) {

    	if (p.size() == 0) {
    		return null;
    	}
    	for (Point a : p) {
    		System.out.println(a);
    	}
    	System.out.println();
    	KDNode T = new KDNode(p.get((p.size()-1)/2), parent);
//    	for (int i = 0; i < layer; i++) {
//    		System.out.print("\t");
//    	}
    	System.out.println(T.getValue());
    	System.out.println();
    	p.remove((p.size()-1)/2);

    	switch (p.size()) {

    	case 0:
    		break;

    	case 1:
    		boolean isLeft;
    		if (layer % 2 == 0)
    			isLeft = p.get(0).lon > T.getValue().lon;
    		else
    			isLeft = p.get(0).lat > T.getValue().lat;

    		if (isLeft)
    			T.setLeft(buildTree(p, T, layer+1));
    		else
    			T.setRight(buildTree(p, T, layer+1));
    		break;

    	default:
    		if (layer % 2 == 0) {
    			List<Point> temp = new ArrayList<Point>(sortedByLat);
    			temp.retainAll(p);
    			p = temp;
    		} else {
    			List<Point> temp = new ArrayList<Point>(sortedByLon);
    			temp.retainAll(p);
    			p = temp;
    		}

    		List<Point> left = new ArrayList<Point>(p);
    		left = left.subList(0, (left.size()/2));

    		List<Point> right = new ArrayList<Point>(p);
    		right = right.subList((right.size()/2), right.size());

    		T.setLeft(buildTree(left, T, layer+1));
    		T.setRight(buildTree(right, T, layer+1));
    	}

    	return T;
    }
    
    @Override
    public List<Point> search(Point searchTerm, int k) {
    	KDNode root = getRelTree(searchTerm);
    	List<KDNode> results = new ArrayList<KDNode>();
    	results = BSTSearch(root, searchTerm, 0, results, k);
    	ArrayList<Point> points = new ArrayList<Point>();
    	for (int i = 0; i < results.size(); i++){
    		points.add(results.get(i).getValue());
    		System.out.println(results.get(i).getValue());
    	}
        return points;
    }

    @Override
    public boolean addPoint(Point point) {
    	if (!isPointIn(point))
    		addToTree(getRelTree(point), point, 0);
    	else
    		return false;
    	return true;
    }

    public KDNode addToTree(KDNode T, Point value, int layer) {
    	
    	KDNode left = T.getLeft();
    	KDNode right = T.getRight();
    	
    	boolean goLeft;
		if (layer%2 == 0)
			goLeft = T.getValue().lat < value.lat;
		else
			goLeft = T.getValue().lon < value.lon;
		
    	if (left == null && right == null) {
    		if (goLeft)
    			T.setLeft(new KDNode(value, T));
    		else
    			T.setRight(new KDNode(value, T));
    		
    	} else {
    		if (goLeft) {
    			if (left != null)
    				return addToTree(left, value, layer+1);
    			else
    				return addToTree(right, value, layer+1);
    			
    		} else {
    			if (right != null)
    				return addToTree(right, value, layer+1);
    			else
    				return addToTree(left, value, layer+1);
    		}
    	}
    	return T;
    }
    
    @Override
    public boolean deletePoint(Point point) {
    	List<KDNode> search = new ArrayList<KDNode>();
    	search = BSTSearch(getRelTree(point), point, 0, search, 1);
    	
    	KDNode n = search.get(search.size()-1);
        if (point.equals(n.getValue())) {
        	
        	KDNode a = null;
        	boolean nIsLeft = n.equals(n.getParent().getLeft());
        	
        	if (n.getLeft() != null)
        		a = getInnerRightNode(n);
        	else if (n.getRight() != null)
        		a = getInnerLeftNode(n);

        	if (nIsLeft)
        		n.getParent().setLeft(a);
        	else
        		n.getParent().setRight(a);
        	
       		if (a != null)
       			a.setParent(n.getParent());
       		
       		return true;

        } else {
        	return false;	
        }
    }

    @Override
    public boolean isPointIn(Point point) {
    	List<Point> search = search(point, 1);
    	if (search.size() > 0)
    		return point.equals(search.get(0));
    	return false;
        
    }
    
	List<KDNode> BSTSearch(KDNode T, Point value, int layer, List<KDNode> c, int k) {
	
		if (T == null) {
			return c;
		}
		KDNode left = T.getLeft();
		KDNode right = T.getRight();
	
		if (left == null && right == null) {
			//nothing happens
	
		} else if (left == null) {
			c = BSTSearch(right, value, layer + 1, c, k);
	
		} else if (right == null) {
			c = BSTSearch(left, value, layer + 1, c, k);
	
		} else {
			//Choose which path to go down based on dimension
			//Also get bounding dist between current node and child node
	
			boolean goLeft = false;
			double divDist = 0;
			if (layer%2 == 0) {
				goLeft = T.getValue().lat < value.lat;
				if (goLeft)
					divDist = Math.abs(T.getValue().lat - value.lat);
				else
					divDist = Math.abs(T.getValue().lat - value.lat);
			} else {
				goLeft = T.getValue().lon < value.lon;
				if (goLeft)
					divDist = Math.abs(T.getValue().lon - value.lon);
				else
					divDist = Math.abs(T.getValue().lon - value.lon);
			}
	
			if (goLeft) {
				c = BSTSearch(left, value, layer + 1, c, k);
				if (c.size() == 0 || divDist < c.get(c.size()-1).getValue().distTo(value))
					c = BSTSearch(right, value, layer + 1, c, k);
			} else {
				c = BSTSearch(right, value, layer + 1, c, k);
				if (c.size() == 0 || divDist < c.get(c.size()-1).getValue().distTo(value))
					c = BSTSearch(left, value, layer + 1, c, k);
			}
		}
	
		if (value.cat.equals(T.getValue().cat)) {
			if (c.size() == 0) {
				c.add(T);
			}
			else if (c.size() < k) {
				insertInOrder(T, c, value);
			}
			else {
				for (int i = 0; i < c.size(); i++) {
					if (c.get(i) != null) {
						if (c.get(i).getValue().distTo(value) > T.getValue().distTo(value)) {
							c.remove(i);
							c = insertInOrder(T, c, value);
						}
						break;
					}
				}
			}
		}
		return c;
	}

    public List<KDNode> insertInOrder(KDNode T, List<KDNode> c, Point value) {
    	if (c.size() == 0)
    		c.add(T);

		for (int i = 0; i < c.size(); i++) {
			if (c.get(i) != null) {
				if (i == c.size()-1)
					c.add(T);
				else if (c.get(i).getValue().distTo(value) > T.getValue().distTo(value))
					c.add(i, T);
				else
					continue;
				break;
			}
		}
		return c;
    }
    
    //Used for when a node with multiple children is deleted
    public KDNode getInnerLeftNode(KDNode root) {
    	
    	if (root == null)
    		return null;
    	
    	KDNode c = getInnerLeftNode(root.getLeft());
    	if (c != null)
    		return c;
    	else
    		return root;
    }
    
    public KDNode getInnerRightNode(KDNode root) {
    	
    	if (root == null)
    		return null;
    	
    	KDNode c = getInnerRightNode(root.getRight());
    	if (c != null)
    		return c;
    	else
    		return root;
    }
    
    public KDNode getRelTree(Point point) {
    	switch (point.cat) {
    	case EDUCATION: return rootE;
    	case RESTAURANT: return rootR;
    	case HOSPITAL:return rootH;
    	default: return null;
    	}
    }
}