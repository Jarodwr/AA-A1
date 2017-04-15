package generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import nearestNeigh.Category;
import nearestNeigh.Point;

public class Generation {
	public static void main(String[] args) {
		try {
			String smallData ="testing/smallDataSet.txt";
			String mediumData = "testing/mediumDataSet.txt";
			String largeData = "testing/LargeDataSet.txt";
			DataSetGen.generateDataSet(1000, smallData);
			DataSetGen.generateDataSet(2500, mediumData);
			DataSetGen.generateDataSet(7000, largeData);

			CommandSetGen.testAdditionRemovalEffect(txtFileIntoList(smallData), "testing/smalldataset_additionremovaltest");
			CommandSetGen.testAdditionRemovalEffect(txtFileIntoList(mediumData), "testing/mediumdataset_additionremovaltest");
			CommandSetGen.testAdditionRemovalEffect(txtFileIntoList(largeData), "testing/largedataset_additionremovaltest");
		
			CommandSetGen.testVarianceOfKValue(4, "kvariancetest");
			
			
		} catch (FileNotFoundException e) {
			System.out.println("some IO error");
		}
	}
	
	public static List<Point> txtFileIntoList(String filename) throws FileNotFoundException {
		List<Point> points = new ArrayList<Point>();
        File dataFile = new File(filename);
        Scanner scanner = new Scanner(dataFile);
        while (scanner.hasNext()) {
            String id = scanner.next();
            Category cat = Point.parseCat(scanner.next());
            Point point = new Point(id, cat, scanner.nextDouble(), scanner.nextDouble());
            points.add(point);
        }
            scanner.close();
            return points;
	}
}
