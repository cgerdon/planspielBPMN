package test.service.bpmn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import main.service.bpmn.models.BpmnLane;
import main.service.bpmn.models.BpmnPool;
import main.util.bpmn.BpmnConstants;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test Class for BpmnPool.
 * @author Joerg Hilscher
 *
 */
public class BpmnPoolTest {

	private static BPMNDiagram bpmnDiagram;
	private static Definitions definitions;
	private static org.eclipse.bpmn2.Process process;
	
	/*
	 * Test Data.
	 */
	final String NEW_NAME = "test-name";
	final String POOL_NAME = "pool-name";
	final String LANE_NAME = "lane-name";
	final int NEW_X = 30;

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
	public void changeXTest () {

		// given
		BpmnPool pool = new BpmnPool(POOL_NAME, bpmnDiagram, definitions, process);
		
		// when
		pool.setX(NEW_X);
		
		// then
		assertEquals(NEW_X + BpmnConstants.DEFAULT_X, pool.getX());
	}
	
	@Test
	public void addLaneTest () {

		// given
		BpmnPool pool = new BpmnPool(POOL_NAME, bpmnDiagram, definitions, process);
	 
		// when
		BpmnLane lane = pool.addLane(LANE_NAME);
		
		// then
		assertEquals(lane, pool.getBpmnLanes().get(0));
	}
	
	@Test
	public void addMultipleLanesTest () {

		// given
		BpmnPool pool = new BpmnPool(POOL_NAME, bpmnDiagram, definitions, process);
	 
		// when
		BpmnLane lane1 = pool.addLane(LANE_NAME + "1");
		BpmnLane lane2 = pool.addLane(LANE_NAME + "2");
		BpmnLane lane3 = pool.addLane(LANE_NAME + "3");
		
		// then
		assertTrue(pool.getBpmnLanes().contains(lane1));
		assertTrue(pool.getBpmnLanes().contains(lane2));
		assertTrue(pool.getBpmnLanes().contains(lane3));
	}
	
	@Test
	@SuppressWarnings("unused")
	public void addMultipleLanesDuplicatesTest () {

		// given
		BpmnPool pool = new BpmnPool(POOL_NAME, bpmnDiagram, definitions, process);
	 
		// when
		BpmnLane lane1 = pool.addLane(LANE_NAME);
		BpmnLane lane2 = pool.addLane(LANE_NAME);
		BpmnLane lane3 = pool.addLane(LANE_NAME);
		
		// then
		assertEquals(1, pool.getBpmnLanes().size());
	}
}
