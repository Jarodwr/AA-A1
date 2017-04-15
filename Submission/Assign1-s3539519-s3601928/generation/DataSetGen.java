package generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class DataSetGen {
	
	public static final Double[] topLeftCityBounds = {-37.656614, 144.759037};
	public static final Double[] bottomRightCityBounds = {-38.188780, 145.284321};
	public static final String[] categories = {"restaurant", "education", "hospital"};
	/*
	 * @numberOfRecords is how many points to generate
	 * 
	 */
	public static void generateDataSet(int numberOfRecords, String outputFileName) throws FileNotFoundException {

        File outputFile = new File(outputFileName);
        PrintWriter writer = new PrintWriter(outputFile);
        
        Random r = new Random();
        
		for (int i = 0; i < numberOfRecords; i++) {
			
			double x = topLeftCityBounds[0] + (bottomRightCityBounds[0] - topLeftCityBounds[0]) * r.nextDouble(); // generate values between the bounds we have set
			double y = topLeftCityBounds[1] + (bottomRightCityBounds[1] - topLeftCityBounds[1]) * r.nextDouble();
			String cat = categories[r.nextInt(3)]; // randomly pick a category
			writer.println("id" + i + " " + cat + " " + x + " " + y);
			
		}
		writer.close();
	}
}
