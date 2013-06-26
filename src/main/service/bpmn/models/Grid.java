package main.service.bpmn.models;

import java.util.Arrays;

import main.util.bpmn.BpmnConstants;

import org.apache.log4j.Logger;

/**
 * 
 * @author Joerg Hilscher
 * 
 * Grid-System to position the elements of a bpmn.
 * A grid has one pool, several lanes and elements (e.g. Events, Tasks ...)
 * The grid will find a position for elements added to the grid. 
 * 
 * Grid Example:
 * [L] [E] [E] [E] [E] [E] [E] [E] 
 * [L] [ ] [ ] [ ] [E] [E] [ ] [ ] 
 * [L] [ ] [ ] [E] [E] [E] [E] [ ]
 * [L] [ ] [ ] [ ] [E] [E] [ ] [ ]
 *  ^ lanes Array
 *  				^ 2D-Array of the grid
 *  L represents a lane
 *  E represents a bpmn-element
 *  Empty cells are null
 */
public class Grid {

	/**
	 * Logging with log4j.
	 */
	private static Logger log = Logger.getLogger(Grid.class);
	
	/**
	 * 2D Array of the Elements.
	 */
	private BpmnElement[][] grid;
	
	/**
	 * Array of the Lanes.
	 */
	private BpmnLane[] lanes;
	
	/**
	 * One grid represents one pool.
	 */
	private BpmnPool pool;
	
	/**
	 * default amount of grid columns.
	 */
	private int xSize = BpmnConstants.DEFAULT_GRID_X_SIZE;
	
	/**
	 * default amount of grid rows.
	 */
	private int ySize = BpmnConstants.DEFAULT_GRID_Y_SIZE;
	
	/**
	 * Width of a grid cell.
	 */
	private int cellWidth = BpmnConstants.CELL_WIDTH;
	
	/**
	 * Height of a grid cell.
	 */
	private int cellHeight = BpmnConstants.CELL_HEIGHT;
	
	/**
	 * default x-position of the first element.
	 */
	private int initIndexX = 0;
		
	/**
	 * Constructs the grid with this pool and it's lanes.
	 * @param pool BpmnPool.
	 */
	public Grid(BpmnPool pool) {
		
		int lanesCount = pool.getBpmnLanes().size();
		
		grid = new BpmnElement[xSize][lanesCount];
		lanes = new BpmnLane[lanesCount];
		
		for (int i = 0; i < lanesCount; i++) {
			lanes[i] = pool.getBpmnLanes().get(i);
		}

		this.pool = pool;
		
		log.info("Creating grid with this pool: \n" + pool.toString());
		
		adjustLanes();
	}

	/**
	 * Constructs an empty grid, without lanes and without a pool.
	 */
	public Grid() {
		grid = new BpmnElement[xSize][ySize];
		lanes = new BpmnLane[ySize];
	}
	
	/**
	 * Adds a new - empty - lane to the grid.
	 * Lane should already be part of the pool.
	 * Checks for duplicates.
	 * @param lane the new lane to add.
	 * @return True if the lane is added, false if the lane is already in the grid.
	 */
	public Boolean addLane(BpmnLane lane) {
		
		// will return -1 if the lane is not found
		int y = getMinYofLane(lane);
		
		// if the lane is not there yet
		if (y < 0) {				
			addRow(1, lane);
			
			//lanes[getYSize() - 1] = lane; addRow does this already
			
			adjustLanes();
			
			log.info("adding new lane to grid: " + lane);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Inserts and positions an element in the grid.
	 * A new element can be part of a lane, that is not yet in the grid.
	 * @param element
	 * @param previousElement
	 * @return position in the grid
	 */
	public int[] addElement(BpmnElement element, BpmnElement previousElement) {
						
		/**
		 * cords of the new element
		 */
		int x;
		int y;
				
		if (element.getBpmnLane() != null)
			addLane(element.getBpmnLane());
		
		// if 1st element
		if (previousElement == null)  {
			
			log.info("Prev Element: " + null);
			
			// use default x-cord
			x = initIndexX;
			
			// get y-cord of the lane
			y = getMinYofLane(element.getBpmnLane());
			
			/*
			if (y < 0) {				
				addRow(1, element.getBpmnLane());
				y = lanes.length - 1;
			}
			*/
			
			// increase initIndex, this shouldn't be required
			initIndexX++;
			
		} else {
		
			/*
			 *  *** 1. when switching to another lane ***
			 */
			if (previousElement.getBpmnLane() != element.getBpmnLane()) {
				
				int prevY = previousElement.getGridPostion()[1];
				
				int minLaneY = getMinYofLane(element.getBpmnLane());

				int maxLaneY = getMaxYofLane(element.getBpmnLane());
				
				// check from where the prev elemente is coming, and then take the nearest position
				if (Math.abs(prevY - minLaneY) < Math.abs(prevY - maxLaneY))
					y = minLaneY;
				else 
					y = maxLaneY;
				
				// if new lane, add it on bottom
				if (y < 0) {
					y = lanes.length;
				}
					
				// move one to the right
				x = previousElement.getGridPostion()[0] + 1;
				
				if (!(x + 1  < getXSize())) {
					addColumn(1);
				}
				
				if (y >= getYSize()) {
					
					addRow(1, element.getBpmnLane());
				} else if (!isRowEmptyFromHere(x, y)) {
					
					insertRowFromHere(x, y, element.getBpmnLane());
				}
				
			
			} else {
			
				/*
				 *  *** 2. When staying on the same lane ***
				 */
				
				// Get cords of previous element
				x = previousElement.getGridPostion()[0];
				y = previousElement.getGridPostion()[1];
				
				// increase crid-size if needed and add a column
				if (!(x + 1  < getXSize())) {
					addColumn(1);
				}
				
				// go to next column: a additional element will always step one to the right
				x++;
				
				if (!isRowEmptyFromHere(x, y)) {
									
					// get the depth (max y-value) of a parallel branch
					int depth = getDepth(previousElement, element.getBpmnLane());
					
					int max1 = y;
					int max2 = y;
					
					int	paraDepth = getParallelElementsSize(previousElement);
					
						
					if (paraDepth >= y) {
						max1 = paraDepth + 1;
					}
						
					if (depth >= y) {
						max2 = depth + 1;
					}
						
					// take the maximum value 
					y = Math.max(max1, max2);
										
					// increase crid-size if needed: add a row or insert a row
					if (y >= getYSize()) {
						
						addRow(1, element.getBpmnLane());
					} else if (!isRowEmptyFromHere(x, y) && lanes[y].equals(element.getBpmnLane())) {
						
						insertRowFromHere(x, y, element.getBpmnLane());
					} else if (!isRowEmptyFromHere(0, y) && !lanes[y].equals(element.getBpmnLane())) {
	
						insertRowFromHere(0, y, element.getBpmnLane());
					}
					
				}
			}
		}
		
		lanes[y] = element.getBpmnLane();
		adjustLanes();
		
		// assign to grid
		grid[x][y] = element;
		setElementPosition(element, x, y);
				
		log.info("Adding Element to grid with x: " + x + " / y: " + y + "  \n\n" +  this.toString());
		
		return new int[] { x, y };
	}
	
	/**
	 * Returns the min y-position of a lane.
	 * This is used to find the highest row of a certain lane.
	 * 
	 * Grid Example, for Lane 2 as parameter:
	 * [Lane 1] [E] [E] [E] [E] [E] [E] [E] 
	 * [Lane 2] [ ] [ ] [ ] [E] [E] [ ] [ ] <- MinY of Lane 2
	 * [Lane 2] [ ] [E] [E] [E] [E] [E] [ ] 
	 * [Lane 3] [ ] [ ] [E] [ ] [ ] [ ] [ ] 
	 * 
	 * will return 1
	 * 
	 * @param lane BpmnLane.
	 * @return the index in lanes-array or -1 if the lane could not be found.
	 */
	private int getMinYofLane(BpmnLane lane) {
		
		if (lane == null)
			return -1;
		
		for (int i = 0; i < lanes.length; i++) {
			if (lane.equals(lanes[i])) {
				return i;
			}
		}
		
		// if the lane is not there yet, return -1
		return -1;
	}
	
	/**
	 * Returns the max y-position of a lane.
	 * This is used to find the lowest row of a certain lane.
	 * 
	 * Grid Example, for Lane 1 as parameter:
	 * [Lane 1] [E] [E] [E] [E] [E] [E] [E] 
	 * [Lane 1] [ ] [ ] [ ] [E] [E] [ ] [ ] 
	 * [Lane 1] [ ] [E] [E] [E] [E] [E] [ ] <- MaxY of Lane 1
	 * [Lane 2] [ ] [ ] [E] [ ] [ ] [ ] [ ] 
	 * 
	 * will return 2
	 * 
	 * @param lane BpmnLane.
	 * @return the index in lanes-array or -1 if the lane could not be found.
	 */
	private int getMaxYofLane(BpmnLane lane) {
		
		if (lane == null)
			return -1;
		
		int maxY = -1;
		
		for (int i = 0; i < lanes.length; i++) {
			if (lane.equals(lanes[i])) {
				maxY = i;
			}
		}
		
		// if the lane is not there yet, return -1
		return maxY;
	}
	
	/**
	 * max y-position in the grid of the following elements of the @param element.
	 * only takes values of elements of the same lane.
	 * @param element BpmnElement.
	 * @return max y-position
	 */	
	private int getDepth(BpmnElement element, BpmnLane lane) {
		
		int maxY = -1;
		
		if (element == null)
			return maxY;
		
		for (BpmnElement i : element.getFollowingElements()) {
			if (i.getBpmnLane() == lane)
				maxY = i.getGridPostion()[1];
			
			int y =  getDepth(i, lane);
			
			if (y > maxY) {
				maxY = y;
			}
		}
		
		return maxY;
	}
		
	/**
	 * Get the parallel elements on the same lane.
	 * @param element BpmnElement.
	 * @return Max Y-Index of a parallel element of the same lane.
	 */
	private int getParallelElementsSize(BpmnElement element) {
		int maxY = -1;
		for (BpmnElement i : element.getFollowingElements()) {
			if (i.getBpmnLane() == element.getBpmnLane()) {
				if (i.getGridPostion()[1] > maxY)
					maxY = i.getGridPostion()[1];
			}
		}
		return maxY;
	}
	
	/**
	 * Checks if this cell in the grid is free (null).
	 * @param x x-Position.
	 * @param y	y-Position.
	 * @return Boolean if this cell is free.
	 */
	private Boolean isPositionFree(int x, int y) {
		
		if (x >= grid.length || y >= grid[x].length) {
			throw new IllegalArgumentException("Index out of range!");
		}
		
		return grid[x][y] == null;
	}
	
	/**
	 * Checks if the row is free from this column/row position.
	 * 
	 * Grid Example 1:
	 * [L] [E] [E] [E] [E] [E] [E] [E] 
	 * [L] [ ] [ ] [ ] [E] [E] [ ] [ ] 
	 * [L] [ ] [E] [E] [E] [E] [E] [ ]
	 * [L] [ ] [ ] [E] [X] [ ] [ ] [ ] <- 
	 * 					^Free from here? -> True
	 * 					X: from this example position
	 * 					
	 * Grid Example 2:
	 * [L] [E] [E] [E] [E] [E] [E] [E] 
	 * [L] [ ] [ ] [X] [ ] [E] [ ] [ ] <- 
	 * [L] [ ] [E] [E] [E] [E] [E] [ ]
	 * [L] [ ] [ ] [E] [ ] [ ] [ ] [ ] 
	 * 				^Free from here? -> False
	 * 				X: from this example position
	 * 
	 *  ^ lanes Array
	 *  				^ 2D-Array of the grid
	 *  L represents a lane
	 *  E represents a bpmn-element
	 *  Empty cells are null
	 * 
	 * @param column	x-position
	 * @param row		y-position
	 * @return Boolean if this row is free from here.
	 */
	private Boolean isRowEmptyFromHere(int column, int row) {
		for (int i = column; i < getXSize(); i++) {
			if (!isPositionFree(i, row)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * columns added right.
	 * 
	 * Grid Example:
	 * Before:
	 * [L] [E] [E] [E] [E] [E] [E] [E] 
	 * [L] [ ] [ ] [ ] [E] [E] [ ] [ ] 
	 * [L] [ ] [E] [E] [E] [E] [E] [ ]
	 * 
	 * After:
	 * [L] [E] [E] [E] [E] [E] [E] [E] [ ]
	 * [L] [ ] [ ] [ ] [E] [E] [ ] [ ] [ ]
	 * [L] [ ] [E] [E] [E] [E] [E] [ ] [ ]
	 * 									^ 1 new column added
	 * 	
	 * 
	 * @param add	number of new columns 
	 */
	private void addColumn(int add) {
		int x = getXSize();
		int y = getYSize();
		
		BpmnElement[][] newGrid = new BpmnElement[x + add][y];
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				newGrid[i][j] = grid[i][j];
			}
		}

		grid = newGrid;
	}
	
	/**
	 * Inserts a row from a specific point.
	 * 
     * Grid Example:
	 * Before:
	 * [L] [E] [E] [E] [E] [E] [E] [E] <- Lane 1
	 * [L] [ ] [E] [X] [E] [E] [ ] [ ] <- Lane 1
	 * [L] [ ] [E] [E] [ ] [E] [ ] [ ] <- Lane 2
	 * 				^ X: from this position add the new lane
	 * 
	 * After:
	 * [L] [E] [E] [E] [E] [E] [E] [E] <- lane 1
	 * [L] [ ] [E] [N] [N] [N] [N] [N] <- lane 1
	 * [L] [N] [N] [ ] [E] [E] [ ] [ ] <- lane 1
	 * [L] [ ] [E] [E] [ ] [E] [ ] [ ] <- lane 2
	 * 
	 * N: inserted cells
	 * 
	 * Note: The new cells should not be added to another lane.
	 * 
	 * @param xIndex
	 * @param yIndex
	 * @param lane -> the lane of the new row 
	 */
	private void insertRowFromHere(int xIndex, int yIndex, BpmnLane lane) {
		
		int x = getXSize();
		int y = getYSize();
		
		BpmnElement[][] newGrid = new BpmnElement[x][y + 1];
		BpmnLane[] newLanes = new BpmnLane[y + 1];
		
		/**
		 * adjust lanes
		 */
		for (int j = 0; j < y; j++) {
			if (j < yIndex) {
				newLanes[j] = lanes[j];
			} else {
				newLanes[j + 1] = lanes[j];
			}
		}
		
		newLanes[yIndex] = lane;
		
		/**
		 * adjust grid
		 */
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {

				// just copy if above new row
				if ((j < yIndex || i < xIndex) && j <= getMaxYofLane(lane)) {
					newGrid[i][j] = grid[i][j];
				} else { 
					// rearrange if below new row
					newGrid[i][j + 1] = grid[i][j];
					
					if (newGrid[i][j + 1] != null) {
						setElementPosition(newGrid[i][j + 1], i, j + 1);
					}
				}
				
			}	
		}
				
		lanes = newLanes;
		grid = newGrid;
		
	}
	
	/**
	 * Adjusts the size of the lanes and pool after the grid has been changed.
	 */
	private void adjustLanes() {
		
		BpmnLane oldLane = null;
		int count = 1;
		int i = 0;
		int width = getXSize();
		
		for (BpmnLane lane : lanes) {
					
			if (lane != null) {
				
				log.info("Adjust: " + lane.toString());
				
				if (lane.equals(oldLane)) {
					count++;
					lane.setHeight(count * cellHeight);
				} else {
					lane.setY(cellHeight * i);
					lane.setHeight(cellHeight);
					lane.setWidth(width * cellWidth);
					count = 1;
				}
				
			} 
			i++;
			oldLane = lane;
		}
		
		if (this.pool != null) {
			// if no lanes: set pool to grid size, else set to size of all lanes
			this.pool.setHeight(lanes.length > 0 ? cellHeight * lanes.length : cellHeight);
			this.pool.setWidth(cellWidth * getXSize());
		}
	}
	
	/**
	 * Sets the position of an element.
	 * @param element BpmnElement.
	 * @param xIndex x-Position in the grid.
	 * @param yIndex y-Position in the grid.
	 */
	private void setElementPosition(BpmnElement element, int xIndex, int yIndex) {
		element.setX(xIndex * cellWidth + BpmnConstants.CELL_PADDING + (cellWidth - element.getWidth()) / 2);
		element.setY((yIndex * cellHeight) + (cellHeight - element.getHeight()) / 2);
		element.setGridPostion(new int [] { xIndex, yIndex });
	}
	
	/**
	 * Adds new rows at the bottom of the grid.
	 * 
     * Grid Example:
	 * Before:
	 * [L] [E] [E] [E] [E] [E] [E] [E] 
	 * [L] [ ] [E] [ ] [E] [E] [ ] [ ] 
	 * [L] [ ] [E] [E] [ ] [E] [ ] [ ]
	 * 
	 * After:
	 * [L] [E] [E] [E] [E] [E] [E] [E] 
	 * [L] [ ] [E] [ ] [E] [E] [ ] [ ] 
	 * [L] [ ] [E] [E] [ ] [E] [ ] [ ]
	 * [ ] [ ] [ ] [ ] [ ] [ ] [ ] [ ] <- added one new row
	 * 
	 * 
	 * @param add number of new rows
	 * @param lane can allocate a lane to the new row
	 */
	private void addRow(int add, BpmnLane lane) {
		
		if (add < 0) {
			throw new IllegalArgumentException("cannot add a negativ amount of rows");
		}
		
		int x = getXSize();
		int y = getYSize();
		
		BpmnElement[][] newGrid = new BpmnElement[x][y + add];
		BpmnLane[] newLanes = new BpmnLane[y + add];
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				newGrid[i][j] = grid[i][j];
				newLanes[j] = lanes[j];
			}
		}
		
		// new rows will have the lane of the last lane
		for (int i = lanes.length; i < newLanes.length; i++) {
			newLanes[i] = lane;
		}
		
		
		grid = newGrid;
		lanes = newLanes;
		
		adjustLanes();
	}

	public int getXSize() {
		return grid.length;
	}
	
	public int getYSize() {
		return grid[0].length;
	}
	
	/**
	 * Displays the grid in a matrix-like style.
	 */
	@Override
	public String toString() {
		String output = "";
		
		for (int i = 0; i < getYSize(); i++) {
			
			output += (lanes[i] == null) ? "[ ] " : "[L] ";
			
			for (int j = 0; j < getXSize(); j++) {
				output += (grid[j][i] == null) ? "[ ] " : "[E] ";
			}
			
			// new line
			output += System.getProperty("line.separator");
		}
		
		return output;
	}

	
	public BpmnPool getPool() {
		return pool;
	}

	public void setPool(BpmnPool pool) {
		this.pool = pool;
	}

	public BpmnElement[][] getGrid() {
		return grid;
	}

	public BpmnLane[] getLanes() {
		return lanes;
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cellHeight;
		result = prime * result + cellWidth;
		result = prime * result + Arrays.hashCode(grid);
		result = prime * result + Arrays.hashCode(lanes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grid other = (Grid) obj;
		if (cellHeight != other.cellHeight)
			return false;
		if (cellWidth != other.cellWidth)
			return false;
		if (!Arrays.deepEquals(grid, other.grid))
			return false;
		if (!Arrays.equals(lanes, other.lanes))
			return false;
		return true;
	}
}
