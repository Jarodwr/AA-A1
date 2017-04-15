package generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import nearestNeigh.Category;
import nearestNeigh.NaiveNN;
import nearestNeigh.NearestNeigh;
import nearestNeigh.Point;

public class CommandSetGen {
	
	public static final Double[] topLeftCityBounds = {-37.656614, 144.759037};
	public static final Double[] bottomRightCityBounds = {-38.188780, 145.284321};
	public static final String[] categories = {"hospital", "restaurant", "education"};
	
	public static void main(String[] args) {
		try {
			 List<Point> points = new ArrayList<Point>();
		        File dataFile = new File(args[0]);
		        Scanner scanner = new Scanner(dataFile);
		        while (scanner.hasNext()) {
		            String id = scanner.next();
		            Category cat = Point.parseCat(scanner.next());
		            Point point = new Point(id, cat, scanner.nextDouble(), scanner.nextDouble());
		            points.add(point);
		        }
		            scanner.close();
			testAdditionRemovalEffect(points, args[1]);
			testVarianceOfKValue(4, args[2]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void testAdditionRemovalEffect(List<Point> points, String outputTestPrefix) throws FileNotFoundException {
		String s = "";
		for(int i = 0; i < (10); i++) {
			File outputFile = new File(outputTestPrefix + "_" + (i) + ".in");
	        PrintWriter writer = new PrintWriter(outputFile);
			if (i % 2 == 0 || i == 0) {
				String t = "";
		        for(int j = 0; j < (i + 1) * 50; j++) {
			        t = t + randomAdditionPoint(j + "a", "restaurant") + "\n";
			        t = t + randomDeletionPoint(points, "restaurant") + "\n";
		        }
		        s = s + t;
			}
					

			else {
				String rs = randomSearch(10, "restaurant") + "\n";
				s = s + rs;
			}
			writer.println(s);
			writer.close();
		}
	}
	
	private static String randomDeletionPoint(List<Point> points, String category) throws FileNotFoundException {
       
        Random r = new Random();
        Point randompoint;
		String s;
        if (category == "") {
        	System.out.println("No category");
            randompoint = points.get(r.nextInt(points.size()-1));
    		s = "D " + randompoint.id + " " + randompoint.cat.toString() + " " + randompoint.lat + " " + randompoint.lon;
        }
        else {
        	 do {
                randompoint = points.get(r.nextInt(points.size()-1));
        		s = "D " + randompoint.id + " " + randompoint.cat.toString() + " " + randompoint.lat + " " + randompoint.lon;
        	} while(randompoint.cat != Point.parseCat(category));
        }
		
		return s;
	}

	public static void testVarianceOfKValue(int amountOfTests, String outputTestPrefix) throws FileNotFoundException {
		for(int i = 0; i < amountOfTests; i++) { 
			System.out.println("Printing Variance of KValue effect test " +(i+1));
	        File outputFile = new File(outputTestPrefix + (i + 1) + ".in");
	        PrintWriter writer = new PrintWriter(outputFile);
	        // increase searches by 30 for each test
	        Random r = new Random();
	        int k = r.nextInt(149) + 1; // any value between 1 and 150
		    writer.println(randomSearch(k, "restaurant"));
	
		    writer.println(randomSearch(k, "education"));
	
		    writer.println(randomSearch(k, "hospital"));
	        writer.close();
		}
	}
	
	public static String randomSearch(int k, String category) {
			String cat;
			Random r = new Random();
			double x = topLeftCityBounds[0] + (bottomRightCityBounds[0] - topLeftCityBounds[0]) * r.nextDouble();
			double y = topLeftCityBounds[1] + (bottomRightCityBounds[1] - topLeftCityBounds[1]) * r.nextDouble();
			if (category.equals("")) {
				cat = categories[r.nextInt(3)];
			}
			else {
				cat = category;
			}
			if (k == -1) {
				k = r.nextInt(149) + 1; // generates a k-value between 1 and 150
			}
			String s = "S " + cat + " " + x + " " + y + " " + k;
			
			return s;
	}
	
	//leaving category as an empty string randomizes the category. Otherwise it will use whatever the category is
	public static String randomAdditionPoint(String id, String category) {
		String cat;
		Random r = new Random();
		double x = topLeftCityBounds[0] + (bottomRightCityBounds[0] - topLeftCityBounds[0]) * r.nextDouble();
		double y = topLeftCityBounds[1] + (bottomRightCityBounds[1] - topLeftCityBounds[1]) * r.nextDouble();
		if (category.equals("")) {
			cat = categories[r.nextInt(3)];
		}
		else {
			cat = category;
		}
		String s = "A " + "id" + id + " " + cat + " " + x + " " + y;
		
		return s;
		
	}
	/*
	public static String createExpectedTestOutput(String testInput, String sampleData) {

        		String s = "";
	            Scanner scanner = new Scanner(testInput);
	            NearestNeigh agent = new NaiveNN();
	            
	            String dataFileName = sampleData;
	            List<Point> points = new ArrayList<Point>();
	            try {
	                File dataFile = new File(dataFileName);
	                Scanner scanner2 = new Scanner(dataFile);
	                while (scanner2.hasNext()) {
	                    String id = scanner2.next();
	                    Category cat = Point.parseCat(scanner2.next());
	                    Point point = new Point(id, cat, scanner2.nextDouble(), scanner2.nextDouble());
	                    points.add(point);
	                }
	                scanner2.close();
	                agent.buildIndex(points);
	            } catch (FileNotFoundException e) {
	            	return null;
	            }

	            // operating commands
	            while (scanner.hasNext()) {
	                String command = scanner.next();
	                String id;
	                Category cat;
	                // remember lat = latitude (approximately correspond to x-coordinate)
	                // remember lon = longitude (approximately correspond to y-coordinate)
	                double lat;
	                double lon;
	                int k;
	                Point point;
	                switch (command) {
	                    // search
	                    case "S":
	                        cat = Point.parseCat(scanner.next());
	                        lat = scanner.nextDouble();
	                        lon = scanner.nextDouble();
	                        k = scanner.nextInt();
	                        point = new Point("searchTerm", cat, lat, lon);
	                        List<Point> searchResult = agent.search(point, k);
	                        for (Point writePoint : searchResult) {
	                            s = s + writePoint.toString();
	                        }
	                        break;
	                    // add
	                    case "A":
	                        id = scanner.next();
	                        cat = Point.parseCat(scanner.next());
	                        lat = scanner.nextDouble();
	                        lon = scanner.nextDouble();
	                        point = new Point(id, cat, lat, lon);
	                        if (!agent.addPoint(point)) {
	                            s = s + "Add point failed.";
	                        }
	                        break;
	                    // delete
	                    case "D":
	                        id = scanner.next();
	                        cat = Point.parseCat(scanner.next());
	                        lat = scanner.nextDouble();
	                        lon = scanner.nextDouble();
	                        point = new Point(id, cat, lat, lon);
	                        if (!agent.deletePoint(point)) {
	                            s = s + "Delete point failed.";
	                        }
	                        break;
	                    // check
	                    case "C":
	                        id = scanner.next();
	                        cat = Point.parseCat(scanner.next());
	                        lat = scanner.nextDouble();
	                        lon = scanner.nextDouble();
	                        point = new Point(id, cat, lat, lon);
	                        s = s + agent.isPointIn(point);
	                        break;
	                    default:
	                        System.err.println("Unknown command.");
	                        System.err.println(command + " " + scanner.nextLine());
	                        
	                	}

	        		scanner.close();
	            }
		return s;
	}*/
}
