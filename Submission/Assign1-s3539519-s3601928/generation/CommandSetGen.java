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

	
	// Explanation of this test is in the document supplied.
	public static void testAdditionRemovalEffect(List<Point> points, String outputTestPrefix) throws FileNotFoundException {
		String s = "";
		for(int i = 0; i < (6); i++) {
			File outputFile = new File(outputTestPrefix + "_" + (i) + ".in");
	        PrintWriter writer = new PrintWriter(outputFile);
			String t = "";
			for(int j = 0; j < (i + 1) * 50; j++) {
			        t = t + randomAdditionPoint(j + "a", "restaurant") + "\n";
			        t = t + randomDeletionPoint(points, "restaurant") + "\n";
		    }
		    s = s + t;
		    String rs = randomSearch(10, "restaurant") + "\n";
		    s = s + rs;
			writer.println(s);
			writer.close();
		}
	}
	// Generate random deletion command. Takes a list of points as an argument, this is just the dataset, as you cannot randomly generate points to delete if they are not in the dataset (there is no point)
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
	// Explanation of this test is in the document supplied.
	public static void testVarianceOfKValue(int amountOfTests, String outputTestPrefix) throws FileNotFoundException {
		for(int i = 0; i < amountOfTests; i++) { 
			System.out.println("Printing Variance of KValue effect test " +(i+1));
	        File outputFile = new File(outputTestPrefix + (i + 1) + ".in");
	        PrintWriter writer = new PrintWriter(outputFile);
	        if (i == 0) {
	        	writer.println(randomSearch(1, "restaurant"));
	
	        	writer.println(randomSearch(1, "education"));
	
	        	writer.println(randomSearch(1, "hospital"));
	        }
	        else if (i == 1) {
	        	writer.println(randomSearch(25, "restaurant"));
	        	
			    writer.println(randomSearch(25, "education"));
		
			    writer.println(randomSearch(25, "hospital"));
	        } else if (i == 2) {
	        	writer.println(randomSearch(75, "restaurant"));
	        	
			    writer.println(randomSearch(75, "education"));
		
			    writer.println(randomSearch(75, "hospital"));
	        } else {
	        	writer.println(randomSearch(150, "restaurant"));
	        	
			    writer.println(randomSearch(150, "education"));
		
			    writer.println(randomSearch(150, "hospital"));
	        }
	        writer.close();
		}
	}
	
	
	// Generate a random search command, can supply the category you want or let it pick one at random
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
	
	// Generate random addition command.
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
}
