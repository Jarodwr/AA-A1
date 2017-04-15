package generation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;

import mainc.NearestNeighFileBased;

public class Testing {

	public static void main(String[] args) {
		final String[] testfilenames = {"test1", "test2", "kvariancetest1", "kvariancetest2", "kvariancetest3", "kvariancetest4", "smalldataset_additionremovaltest_0", "smalldataset_additionremovaltest_1", "smalldataset_additionremovaltest_2", "smalldataset_additionremovaltest_3" };
		final String[] datasetfilenames = {"smallDataSet", "mediumDataSet", "largeDataSet"};
		final String[] agents = {"naive", "kdtree"};
		final String absolutefilepath = "C:\\Users\\Spencer\\Documents\\GitHub\\AA-A1\\testing\\";
		
		for (int i = 0; i < testfilenames.length; i++) {
			for (int j = 0; j < datasetfilenames.length;j++) {
				System.out.println("Test " + (i+1) + ": " + testfilenames[i]);
				try {
					Writer output = new BufferedWriter(new FileWriter("TestSummary.out", true));
					Long avg = (long) 0;
					for (int k = 0; k < 3; k++) {
							Long time = (new Date()).getTime();
							NearestNeighFileBased.main(new String[]{agents[0], datasetfilenames[j] + ".txt", absolutefilepath + testfilenames[i] + ".in", absolutefilepath + testfilenames[i] + ".out"});
							Long endtime = (new Date().getTime() - time);
							avg = avg + endtime;
							output.append(testfilenames[i] + ".in with " + datasetfilenames[j] + " Execution time:" + endtime + "\n");
					        
					}
	
					output.append(testfilenames[i] + ".in with " + datasetfilenames[j] + " Average time:" + avg/3 + "\n");
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
