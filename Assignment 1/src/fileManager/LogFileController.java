package fileManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
// import java.util.Collections;
import java.util.Date;
import java.util.List;

import controller.GridEnvironment;
import entities.StateType;
import entities.Constants;
import entities.StateCoordinate;

public class LogFileController {
	private String timestamp;
	private String fileName;

	//Store headers of State/Wall type
	private List<String> headers;
	private List<List<Double>> data;

	/**
	 * Constructor to define header names
	 * @param fileName,grid
	 */
	public LogFileController(String fileName, GridEnvironment grid) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		this.fileName = fileName;
		this.timestamp = dateFormat.format(date);
		this.headers = new ArrayList<String>();
		this.data = new ArrayList<List<Double>>();

		for (int c = 0; c < Constants.NUM_COL; c++) {
			for (int r = 0; r < Constants.NUM_ROW; r++) {
				if (grid.getCell(new StateCoordinate(c, r)).getStateType() == StateType.WALL)
					headers.add("\"Wall: (" + c + ", " + r + ")\"");
				else
					headers.add("\"State: (" + c + ", " + r + ")\"");
			}
		}
	}

	/**
	 * Function to keep adding the incoming List of utilities for corresponding states in each iteration
	 * @param grida
	 */
	public void add(GridEnvironment grid) {
		List<Double> incoming = new ArrayList<Double>();

		for (int c = 0; c < Constants.NUM_COL; c++) {
			for (int r = 0; r < Constants.NUM_ROW; r++) {
				incoming.add(grid.getCell(new StateCoordinate(c, r)).getUtility());
			}
		}

		this.data.add(incoming);
	}

	public void finalConvertToCSV() {
		List<List<Double>> incomingArray = this.incomingArr(this.data);
		writeToCSV.writeToFile(this.fileName + "_" + this.timestamp + "_K = "+ Constants.K, this.headers, incomingArray);
	}

	private List<List<Double>> incomingArr(List<List<Double>> incoming) {
		List<List<Double>> incomingArray = new ArrayList<List<Double>>(incoming);
		return incomingArray;
	}
}
