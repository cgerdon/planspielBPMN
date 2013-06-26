package test.service.bpmn;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import main.service.bpmn.models.BpmnElement;
import main.service.bpmn.models.BpmnLane;
import main.service.bpmn.models.BpmnPool;
import main.service.bpmn.models.BpmnUserTask;
import main.service.bpmn.models.Grid;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Joerg
 *
 */
public class GridTest {

	private static BPMNDiagram bpmnDiagram;
	private static Definitions definitions;
	private static org.eclipse.bpmn2.Process process;

	
	/*
	 * Test Data.
	 */
	final int NUMBER_OF_ELEMENTS = 24;
	
	@BeforeClass
	public static void setupClass () throws UnsupportedEncodingException {
				
		// Definitions of the xml file
		definitions = BpmnTestHelper.getDefinitions();		
	}
	
	@Before
	public void beforeTest () {
		
		// These elements should be resetted 
		
		// We know there is only one child: our process 
		process = (org.eclipse.bpmn2.Process) definitions.getRootElements().get(0);

		// Convert to BPMNDiagram
		bpmnDiagram = (BPMNDiagram) definitions.getDiagrams().get(0);
		
	}
	
	@Test
	public void constructorWithPoolTest () {

		// given

		BpmnPool pool = new BpmnPool("testpool", bpmnDiagram, definitions, process);
		BpmnLane lane = pool.addLane("testLane");
		
		// when
		Grid grid = new Grid(pool);
		
		// then
		assertEquals(pool, grid.getPool());
		assertEquals(lane, grid.getLanes()[0]);
	}
	
	@Test
	public void addElementToGridTest () {

		// given
		BpmnPool pool = new BpmnPool("testpool", bpmnDiagram, definitions, process);
		BpmnLane lane = pool.addLane("testLane");
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, lane, null);	
		Grid grid = new Grid(pool);
		
		
		// when
		grid.addElement(simpleBpmnUserTask, null);
		
		// then
		assertEquals(simpleBpmnUserTask, grid.getGrid()[0][0]);
	}
	
	@Test
	@SuppressWarnings("unused")
	public void addElementToGridMultipleLanesTest () {

		// given
		BpmnPool pool = new BpmnPool("testpool", bpmnDiagram, definitions, process);
		BpmnLane lane1 = pool.addLane("testLane 1");
		BpmnLane lane2 = pool.addLane("testLane 2");
		BpmnLane lane3 = pool.addLane("testLane 3");
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, lane2, null);	
		Grid grid = new Grid(pool);
		
		
		// when
		grid.addElement(simpleBpmnUserTask, null);
		
		// then
		assertEquals(simpleBpmnUserTask, grid.getGrid()[0][1]);
		assertEquals(lane2, grid.getLanes()[1]);
		assertEquals(3, grid.getYSize());
	}

	@Test 
	@SuppressWarnings("unused")
	public void addMultipleLanesInDifferentWays1Test() {
		
		// given
		BpmnPool pool = new BpmnPool("testpool", bpmnDiagram, definitions, process);
		// 1
		BpmnLane lane1 = pool.addLane("testLane 1");
		
		// when
		Grid grid = new Grid(pool);
		BpmnLane lane2 = pool.addLane("testLane 2"); // 2
		BpmnLane lane3 = pool.addLane("testLane 3"); // 3
		
		grid.addLane(lane2);
		grid.addLane(lane3);
		
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, 
				pool.addLane("testLane 4"), grid);	// 4
		
		// then
		assertEquals(4, grid.getLanes().length);
	}
	
	@Test 
	@SuppressWarnings("unused")
	public void addMultipleLanesInDifferentWays2Test() {
		
		// given
		BpmnPool pool = new BpmnPool("testpool", bpmnDiagram, definitions, process);
		
		// 1, 2
		BpmnLane lane1 = pool.addLane("testLane 1");
		BpmnLane lane2 = pool.addLane("testLane 2"); 
		
		// when
		Grid grid = new Grid(pool);
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, 
				pool.addLane("testLane 3"), grid);	// 3
		
		
		BpmnLane lane4 = pool.addLane("testLane 4"); // 4
		grid.addLane(lane4);
		
		// then
		assertEquals(4, grid.getLanes().length);
	}
	
	@Test 
	public void addDuplicatedLanesTest() {
		
		// given
		BpmnPool pool = new BpmnPool("testpool", bpmnDiagram, definitions, process);
		Grid grid = new Grid(pool); // pool with no lanes
		
		BpmnLane lane1 = pool.addLane("testLane 1");
		BpmnLane lane2 = pool.addLane("testLane 2"); 
		
		// when
		grid.addLane(lane1);
		grid.addLane(lane1);
		grid.addLane(lane2);
		grid.addLane(lane1);
		grid.addLane(lane1);
		grid.addLane(lane2);
		grid.addLane(lane2);
		
		// then
		assertEquals(2, grid.getLanes().length);
	}
	
	@Test
	public void addMultipleElementsToGridTest () {
		
		// given
		BpmnPool pool = new BpmnPool("testpool", bpmnDiagram, definitions, process);
		BpmnLane lane = pool.addLane("testLane");
	
		Grid grid = new Grid(pool);
				
		BpmnUserTask prevTask = new BpmnUserTask("first Task", bpmnDiagram, process, null, null, lane, grid);
		
		// when
		for (int i = 0; i < NUMBER_OF_ELEMENTS - 1; i++) {
			prevTask = new BpmnUserTask("name-Nr " + i, bpmnDiagram, process, prevTask, null, lane, grid);
		}	
			
		// then
		assertEquals(NUMBER_OF_ELEMENTS, grid.getXSize());
	}
	
	@Test
	public void addElementToGridCheckForCorrectLanePlacementTest () {
		
		// given
		BpmnPool pool = new BpmnPool("testpool", bpmnDiagram, definitions, process);
		
		List<BpmnLane> lanes = new ArrayList<BpmnLane>();
		
		lanes.add(pool.addLane("testRandomLane 1"));
		lanes.add(pool.addLane("testRandomLane 2"));
		lanes.add(pool.addLane("testRandomLane 3"));
		
		Grid grid = new Grid(pool);
		
		List<BpmnElement> elements = new ArrayList<BpmnElement>();
		
		// when
		BpmnElement prevTask = new BpmnUserTask("first Task", bpmnDiagram, process, null, null, lanes.get(BpmnTestHelper.getRandom(3)), grid);
		elements.add(prevTask);
		
		// Assign random lanes to elements
		for (int i = 0; i < NUMBER_OF_ELEMENTS - 1; i++) {
			BpmnElement ele = new BpmnUserTask("name-Nr " + i, bpmnDiagram, process, prevTask, null, lanes.get(BpmnTestHelper.getRandom(3)), grid);
			prevTask = ele;
			elements.add(prevTask);
		}
		
		// then
		
		for (BpmnElement element : elements) {
			BpmnLane lane = grid.getLanes()[BpmnTestHelper.getRowOfElement(grid.getGrid(), element)];
			assertEquals(element.getBpmnLane(), lane);
		}

	}
	
	@Test
	public void addElementToGridCheckForCorrectLanePlacementWithMultipleConnectionsTest () {
		
		// given
		BpmnPool pool = new BpmnPool("testpool", bpmnDiagram, definitions, process);
		
		List<BpmnLane> lanes = new ArrayList<BpmnLane>();
		
		lanes.add(pool.addLane("testRandomLane 1"));
		lanes.add(pool.addLane("testRandomLane 2"));
		lanes.add(pool.addLane("testRandomLane 3"));
		
		Grid grid = new Grid(pool);
		
		List<BpmnElement> elements = new ArrayList<BpmnElement>();
		
		// when	 
		elements.add(new BpmnUserTask("first Task", bpmnDiagram, process, null, null, lanes.get(BpmnTestHelper.getRandom(3)), grid));
		
		// Assign random lanes to elements
		for (int i = 0; i < NUMBER_OF_ELEMENTS - 1; i++) {
			BpmnElement ele = new BpmnUserTask("name-Nr " + i, bpmnDiagram, process, 
					elements.get(BpmnTestHelper.getRandom(elements.size())), null, lanes.get(BpmnTestHelper.getRandom(3)), grid);
			elements.add(ele);
		}
		
		// then
		for (BpmnElement element : elements) {
			BpmnLane lane = grid.getLanes()[BpmnTestHelper.getRowOfElement(grid.getGrid(), element)];
			
			assertEquals(element.getBpmnLane(), lane);
		}

	}
}
