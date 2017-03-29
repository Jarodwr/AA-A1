package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class KDTreeNNTest {

	KDTreeNN test = new KDTreeNN();
	List<Point> testPoints = new ArrayList<Point>();
	@Test
	public void testBuildIndex1() {
		test.buildIndex(testPoints);
		assert(true);
	}

}
