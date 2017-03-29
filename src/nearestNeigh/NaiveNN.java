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
			this.Index.add(p);
		}
    }

    @Override
    // note to jarod: int k is the amount of results to return
    public List<Point> search(Point searchTerm, int k) {
    	ArrayList<Point> returnPoints = new ArrayList<Point>();
    	Category categ = searchTerm.cat;
    	
    	if (this.Index.size() < k) { // if the list of points is less than k we would get stuck in an loop
    		k = this.Index.size();
    	}
    	
    	for (Point p : this.Index) {
    		if (!(p.cat.equals(categ))) { // remove points that aren't the same category
    			this.Index.remove(p);
    		}
    	}
    	while ((returnPoints).size() < k) {
    		
    	}
    	return returnPoints;
    }


	@Override
    public boolean addPoint(Point point) {
        this.Index.add(point);
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
