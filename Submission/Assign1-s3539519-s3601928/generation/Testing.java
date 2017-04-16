package generation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;

import mainc.NearestNeighFileBased;
// Note, if you're running this class the main for the implementation must be in a proper package NOT default, and imported correctly to this file
public class Testing {

	// This class is a giant wrapper for the implementation, it runs the implementation many times with different settings, appending the time it takes for each execution to the output file "TestSummary.out"
	public static void main(String[] args) {
		final String[] testfilenames = {"kvariance1" };
		final String[] datasetfilenames = {"smallDataSet", "mediumDataSet", "largeDataSet"};
		final String[] agents = {"naive", "kdtree"};
		final String absolutefilepath = "C:\\Users\\Spencer\\Documents\\GitHub\\AA-A1\\testing\\"; // ignore this, my IDE wasn't able to find files without it supplied
		
		for (int i = 0; i < testfilenames.length; i++) {
			for (int j = 0; j < datasetfilenames.length;j++) {
				System.out.println("Test " + (i+1) + ": " + testfilenames[i]);
				try {
					Writer output = new BufferedWriter(new FileWriter("TestSummary.out", true));
					Long avg = (long) 0;
					for (int k = 0; k < 10; k++) { // Runs each test 10 times, to get the average time taken
							Long time = (new Date()).getTime();
							NearestNeighFileBased.main(new String[]{agents[0], datasetfilenames[j] + ".txt", absolutefilepath + testfilenames[i] + ".in", absolutefilepath + testfilenames[i] + ".out"}); // Call the implementation
							Long endtime = (new Date().getTime() - time);
							avg = avg + endtime;
							//output.append(testfilenames[i] + ".in with " + datasetfilenames[j] + " Execution time:" + endtime + "\n"); // uncomment this line to get the execution time of each run, we only wanted to collect the average time of 10 runs of each test
					        
					}
	
					output.append(testfilenames[i] + ".in with " + datasetfilenames[j] + " Average time:" + avg/10 + "\n"); // Write down the average
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
