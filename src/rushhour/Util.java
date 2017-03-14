package rushhour;

import java.util.List;
import java.util.LinkedList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Util {

	public static List<Path> getFilePaths(String filename) {
		Path path = Paths.get(filename);
		if(path.toFile().isDirectory()) {
			return getFilePathsHelper(new LinkedList<Path>(), path);
		} else {
			List<Path> ret = new LinkedList<>();
			ret.add(path);
			return ret;
		}
	}

	private static List<Path> getFilePathsHelper(List<Path> ret, Path dir) {
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for(Path path : stream) {
				if(path.toFile().isDirectory()) {
					getFilePathsHelper(ret, path);
				} else {
					ret.add(path);
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return ret;
	} 

	public static String vectorToString(double[] vector) {
		String ret = "" + vector[0];
		for(int i=1; i<vector.length; i++) {
			ret += "," + vector[i];
		}
		return ret;
	}

	public static double[] vectorFromString(String string) {
		String[] split = string.split(",");
		double[] vector = new double[split.length];
		for(int i=0; i<split.length; i++) {
			vector[i] = Double.parseDouble(split[i]);
		}
		return vector;
	}

	public static double[] vectorFromFile(String filename) {
		try {
			Scanner s = new Scanner(new File(filename), "utf-8");
			return vectorFromString(s.next());
		} catch (FileNotFoundException e) {
			return null;
		}
	}

}
