package generation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;

import mainc.NearestNeighFileBased;

public class Testing {

	public static void main(String[] args) {
		final String[] testfilenames = {"_additionremovaltest_0", "_additionremovaltest_1", "_additionremovaltest_2", "_additionremovaltest_3", "_additionremovaltest_4", "_additionremovaltest_5" };
		final String[] datasetfilenames = {"smallDataSet", "mediumDataSet", "largeDataSet"};
		final String[] agents = {"naive", "kdtree"};
		final String absolutefilepath = "C:\\Users\\Spencer\\Documents\\GitHub\\AA-A1\\testing\\";
		
		for (int i = 0; i < testfilenames.length; i++) {
			for (int j = 0; j < datasetfilenames.length;j++) {
				System.out.println("Test " + (i+1) + ": " + testfilenames[i]);
				try {
					Writer output = new BufferedWriter(new FileWriter("TestSummary.out", true));
					Long avg = (long) 0;
					for (int k = 0; k < 10; k++) {
							Long time = (new Date()).getTime();
							NearestNeighFileBased.main(new String[]{agents[1], datasetfilenames[j] + ".txt", absolutefilepath + datasetfilenames[j].toLowerCase() + testfilenames[i] + ".in", absolutefilepath + datasetfilenames[j].toLowerCase() + testfilenames[i] + ".out"});
							Long endtime = (new Date().getTime() - time);
							avg = avg + endtime;
							//output.append(testfilenames[i] + ".in with " + datasetfilenames[j] + " Execution time:" + endtime + "\n");
					        
					}
	
					output.append(testfilenames[i] + ".in with " + datasetfilenames[j] + " Average time:" + avg/10 + "\n");
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
