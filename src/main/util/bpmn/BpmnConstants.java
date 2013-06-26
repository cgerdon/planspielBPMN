package main.util.bpmn;

/**
 *
 * @author Joerg
 *
 * Default Values for the drawing of the bpmn-elements.
 */
public class BpmnConstants {

	/**
	 * Hidden constructor.
	 */
	private BpmnConstants () {}
	
	// *** Files ***
	/**
	 * output bpmn file.
	 */
	public static final String OUTPUT_FILE_NAME = "output.bpmn";

	/**
	 * blanco bpmn file.
	 */
	public static final String INPUT_FILE_NAME = "simple.bpmn";
	
	/**
	 * Path.
	 */
	public static final String PATH = "BpmnFiles/";
	
	// *** Drawing ***

	/**
	 *  Margin to the left upper corner.
	 */
	public static final int DEFAULT_Y = 10;
	public static final int DEFAULT_X = 10;
	
	/**
	 *  Top-Margin between elements.
	 */
	public static final int MARGIN_TOP = 40;

	/**
	 *  Margin on the left required for pool name.
	 */
	public static final int LANE_TO_POOL_MARGIN = 30;

	/**
	 *  Padding.
	 */
	public static final int LANE_PADDING = 10;

	/**
	 *  Bounds for a lane.
	 */
	public static final int DEFAULT_LANE_HEIGHT = 300;
	public static final int DEFAULT_LANE_WIDTH = 1200;

	/**
	 *  Hight and Width of elements.
	 */
	public static final int DEFAULT_TASK_HEIGHT = 80;
	public static final int DEFAULT_TASK_WIDTH = 160;
	
	/**
	 *  Hight and Width of events.
	 */
	public static final int DEFAULT_EVENT_HEIGHT = 36;
	public static final int DEFAULT_EVENT_WIDTH = 36;
	
	/**
	 *  Hight and Width of gateways.
	 */
	public static final int DEFAULT_GATEWAY_HEIGHT = 36;
	public static final int DEFAULT_GATEWAY_WIDTH = 36;
	
	/**
	 * Grid values.
	 */
	public static final int CELL_WIDTH = 240;
	public static final int CELL_HEIGHT = 160;
	
	public static final int DEFAULT_GRID_X_SIZE = 8;
	public static final int DEFAULT_GRID_Y_SIZE = 1;
	
	public static final int CELL_PADDING = 80;
}
