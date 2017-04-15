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

    private ArrayList<Point> RestaurantIndex = new ArrayList<Point>();

    private ArrayList<Point> EducationIndex = new ArrayList<Point>();

    private ArrayList<Point> HospitalIndex = new ArrayList<Point>();

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
    	//Boolean didSwapOccur = false;
    	//Point temp;
    	ArrayList<Point> index;
    	if (searchTerm.cat == Category.RESTAURANT) {

    		index = RestaurantIndex;
    	}
    	else if (searchTerm.cat == Category.EDUCATION) {
    		index = EducationIndex;
    	}
    	else {
    		index = HospitalIndex;
    	}
    	
    	
    	if (index.size() < k) { // if the list of points is less than k we would get stuck in an loop
    		k = index.size();
    	}
    	/*
    	while(true) { // swap function
    		didSwapOccur = false;
    		if (index.size() == 1 || index.size() == 0) {
    			break; // no need to change anything
    		}
    		for(int j = 1; j < index.size() - 1; j++) {
    			 if( searchTerm.distTo(index.get(j)) < searchTerm.distTo(index.get(j - 1)) ) {
    				 temp = index.get(j);
    				 index.set(j, index.get(j - 1));
    				 index.set(j - 1, temp);
    				 didSwapOccur = true; // if no swap occurs then we're already done
    			 }
    		}
    		if (didSwapOccur == false) {
    			break;
    		}
    	}
    	*/
    	
    	Quicksort.byDist(index, searchTerm, 0, index.size() - 1);
    	
        for (int i = 0; i < k; i++){
        		returnPoints.add(index.get(i));
        }
    	return returnPoints;
    }


	@Override
    public boolean addPoint(Point point) {
		if(point.cat == Category.RESTAURANT) {
			if (this.RestaurantIndex.size() == 0) {
				this.RestaurantIndex.add(point);
				return true;
			}
			if (isPointIn(point)) {
				return false;
			}
	        this.RestaurantIndex.add(point);
	        return true;
		}
		else if(point.cat == Category.HOSPITAL) {
			if (this.HospitalIndex.size() == 0) {
				this.HospitalIndex.add(point);
				return true;
			}
			if (isPointIn(point)) {
				return false;
			}
	        this.HospitalIndex.add(point);
	        return true;
		}
		else {
			if (this.EducationIndex.size() == 0) {
				this.EducationIndex.add(point);
				return true;
			}
			if (isPointIn(point)) {
				return false;
			}
	        this.EducationIndex.add(point);
	        return true;
		}
    }

    @Override
    public boolean deletePoint(Point point) {
    	if (point.cat == Category.EDUCATION) {
    		 Iterator<Point> iter = this.EducationIndex.iterator();
    	       while(iter.hasNext()) {
    	    	   Point p = iter.next();
    	    	   if (p.equals(point)) {
    	    		   iter.remove();
    	    		   return true;
    	    	   }
    	        }
    	        return false;
    	} else if (point.cat == Category.RESTAURANT) {
   		 Iterator<Point> iter = this.RestaurantIndex.iterator();
	       while(iter.hasNext()) {
	    	   Point p = iter.next();
	    	   if (p.equals(point)) {
	    		   iter.remove();
	    		   return true;
	    	   }
	        }
	        return false;
    	} else {
    		Iterator<Point> iter = this.HospitalIndex.iterator();
 	       while(iter.hasNext()) {
 	    	   Point p = iter.next();
 	    	   if (p.equals(point)) {
 	    		   iter.remove();
 	    		   return true;
 	    	   }
 	        }
 	        return false;
    	}
      
    }

    @Override
    public boolean isPointIn(Point point) {
    	if (point.cat == Category.EDUCATION) {
   		 Iterator<Point> iter = this.EducationIndex.iterator();
   	       while(iter.hasNext()) {
   	    	   Point p = iter.next();
   	    	   if (p.equals(point)) {
   	    		   return true;
   	    	   }
   	        }
   	        return false;
   	} else if (point.cat == Category.RESTAURANT) {
  		 Iterator<Point> iter = this.RestaurantIndex.iterator();
	       while(iter.hasNext()) {
	    	   Point p = iter.next();
	    	   if (p.equals(point)) {
	    		   return true;
	    	   }
	        }
	        return false;
   	} else {
   		Iterator<Point> iter = this.HospitalIndex.iterator();
	       while(iter.hasNext()) {
	    	   Point p = iter.next();
	    	   if (p.equals(point)) {
	    		   return true;
	    	   }
	        }
	        return false;
   	}
    }

}