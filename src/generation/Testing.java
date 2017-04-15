package generation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;

import mainc.NearestNeighFileBased;

public class Testing {

	public static void main(String[] args) {
		final String[] testfilenames = {"kvariancetest1", "kvariancetest2", "kvariancetest3", "kvariancetest4", "smalldataset_additionremovaltest_0", "smalldataset_additionremovaltest_1", "smalldataset_additionremovaltest_2", "smalldataset_additionremovaltest_3" };
		final String[] datasetfilenames = {"smallDataSet.txt"};
		final String[] agents = {"naive", "kdtree"};
		final String absolutefilepath = "C:\\Users\\Spencer\\Documents\\GitHub\\AA-A1\\testing\\";
		for (int i = 0; i < testfilenames.length; i++) {
			System.out.println("Test " + (i+1));
			try {
				Writer output = new BufferedWriter(new FileWriter("TestSummary.out", true));
				Long time = (new Date()).getTime();
				NearestNeighFileBased.main(new String[]{agents[0], datasetfilenames[0], absolutefilepath + testfilenames[i] + ".in", absolutefilepath + testfilenames[i] + ".out"});
				output.append(testfilenames[i] + ".in Execution time:" + (new Date().getTime() - time) + "\n");
		        output.close();
			} catch (Exception e) {
				System.out.println("fuck");
			}
		}
	}
}
