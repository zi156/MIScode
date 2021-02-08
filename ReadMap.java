import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReadMap {
	private static Scanner input;
	private static Map<Point, Integer> recordMap;
	private static int width;
	private static int height;

	public static void openFile(String fileName) {
		try {
			input = new Scanner(new File(fileName));
		} catch (IOException ioException) {
			System.err.println("Error opening file. Terminating.");
			System.exit(1);
		}
	}

	// read record from file
	public static void readMap() {
		try {
			recordMap = new HashMap<Point, Integer>();
			int row = 1;
			int col = 1;
			while (input.hasNextLine()) { // file reading
				col = 1;
				String[] values = input.nextLine().split(" ");
				int[] init = new int[10];
				Arrays.fill(init, 1);
				for (String str : values) {
					init[Integer.parseInt(str) - 1] = 0;
				}
				for (int i = 0; i < init.length; i++) {
					recordMap.put(new Point(col++, row), init[i]);
				}
				row++;
			}
			width = col - 1;
			height = row - 1;
		} catch (NoSuchElementException elementException) {
			System.err.println("File improperly formed. Terminating.");
		} catch (IllegalStateException stateException) {
			System.err.println("Error reading from file. Terminating.");
		}
	} // end method readRecord

	// close file and terminate application
	public static void closeFile() {
		if (input != null)
			input.close();
	}

	public static Map<Point, Integer> getRecordMap() {
		return recordMap;
	}

	public static int getMapWidth() {
		return width;
	}

	public static int getMapHeight() {
		return height;
	}
}
