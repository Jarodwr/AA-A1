package nearestNeigh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * @author Jeffrey, Youhan
 */
public class NaiveNN implements NearestNeigh{

    private ArrayList<Point> Index = new ArrayList<Point>();

	@Override
    public void buildIndex(List<Point> points) {
		for(Point p : points) {
			addPoint(p);
		}
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
    	ArrayList<Point> returnPoints = new ArrayList<Point>();
    	Category categ = searchTerm.cat;
    	Boolean didSwapOccur = false;
    	Point temp;
    	
    	if (this.Index.size() < k) { // if the list of points is less than k we would get stuck in an loop
    		k = this.Index.size();
    	}
    	Iterator<Point> iter = this.Index.iterator();
    	while (iter.hasNext()) {
    		Point p = iter.next();
    		if (!(p.cat.equals(categ))) { // remove points that aren't the same category
    			iter.remove();
    		}
    	}
    	while(true) { // swap function
    		didSwapOccur = false;
    		if (this.Index.size() == 1 || this.Index.size() == 0) {
    			break; // no need to change anything
    		}
    		for(int j = 1; j < this.Index.size() - 1; j++) {
    			 if( searchTerm.distTo(this.Index.get(j)) < searchTerm.distTo(this.Index.get(j - 1)) ) {
    				 temp = this.Index.get(j);
    				 this.Index.set(j, this.Index.get(j - 1));
    				 this.Index.set(j - 1, temp);
    				 didSwapOccur = true; // if no swap occurs then we're already done
    			 }
    		}
    		if (didSwapOccur == false) {
    			break;
    		}
    	}
        for (int i = 0; i < k; i++){
        		returnPoints.add(this.Index.get(i));
        }
    	return returnPoints;
    }


	@Override
    public boolean addPoint(Point point) {
		if (this.Index.size() == 0) {
			this.Index.add(point);
			return true;
		}
		for(int i = 0; i < this.Index.size() - 1; i++) {
        	if (point.equals(this.Index.get(i))) {
        		return false;
        	}
        }
        this.Index.add(point);
        return true;
    }

    @Override
    public boolean deletePoint(Point point) {
        for(int i = 0; i < this.Index.size() - 1; i++) {
        	if (point.equals(this.Index.get(i))) {
        		this.Index.remove(i);
        		return true;
        	}
        }
        return false;
    }

    @Override
    public boolean isPointIn(Point point) {
    	for(int i = 0; i < this.Index.size() - 1; i++) {
        	if (point.equals(this.Index.get(i))) {
        		return true;
        	}
        }
        return false;
    }

}
