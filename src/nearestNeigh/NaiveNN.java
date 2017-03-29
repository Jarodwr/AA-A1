package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * @author Jeffrey, Youhan
 */
public class NaiveNN implements NearestNeigh{

    private ArrayList<Point> Index;

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
    	
    	for (Point p : this.Index) {
    		if (!(p.cat.equals(categ))) { // remove points that aren't the same category
    			this.Index.remove(p);
    		}
    	}
    	while(true) {
    		didSwapOccur = false;
    		for(int j = 0; j < returnPoints.size() - 1; j++) {
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
    	
    	return returnPoints;
    }


	@Override
    public boolean addPoint(Point point) {
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
