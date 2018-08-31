package util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Merges all files.
 * 
 * @author S.A.M. Janssen
 */
public class FileMerger {

	/**
	 * Gathers all files in a directory and its sub directories.
	 * 
	 * @param directoryName
	 *            The directory name.
	 * @param files
	 *            An empty list.
	 */
	private static void listf(String directoryName, List<File> files) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		if (fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					files.add(file);
				} else if (file.isDirectory()) {
					listf(file.getAbsolutePath(), files);
				}
			}
		}
	}

	/**
	 * Merge all files in a directory.
	 * 
	 * @param directoryName
	 *            The name.
	 * @return The merged file.
	 * @throws IOException
	 *             Exception.
	 */
	public static File mergeFiles(String directoryName) throws IOException {

		List<File> files = new ArrayList<>();
		listf(directoryName, files);
		String filename = new File(directoryName).getParent() + File.separator + "jointFile.txt";
		PrintWriter pw = new PrintWriter(filename);

		for (File file : files) {
			// BufferedReader object for file1.txt
			if (file.getName().contains("returnValues.txt")) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				// loop to copy each line of
				// file to jointfile.txt
				while (line != null) {
					pw.println(line);
					line = br.readLine();
				}
				// pw.println("eof " + file.getParentFile().getName() +
				// File.separator + file.getName());
				pw.flush();
				br.close();
			}
		}

		// closing resources
		pw.close();
		return new File(filename);
	}

}
