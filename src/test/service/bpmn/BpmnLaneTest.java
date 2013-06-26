package test.service.bpmn;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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
 * Test Class for BpmnLane.
 * @author Joerg
 * TODO: Tests schreiben
 */
public class BpmnLaneTest {

	private static BPMNDiagram bpmnDiagram;
	private static Definitions definitions;
	private static org.eclipse.bpmn2.Process process;
	
	/*
	 * Test Data.
	 */
	final String NEW_NAME = "test-name";
	final String POOL_NAME = "pool-name";
	final String LANE_NAME = "lane-name";
	final String NEW_LANE_NAME = "new lane-name";
	final int NEW_X = 30;
	final int NEW_WIDTH = 1200;

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
	public void createOverPoolTest () {

		// given
		BpmnPool pool = new BpmnPool(POOL_NAME, bpmnDiagram, definitions, process);
		
		// when
		BpmnLane lane = pool.addLane(LANE_NAME);
		
		// then
		assertNotNull(lane);
	}
	
	@Test
	public void changeWidthTest () {

		// given
		BpmnLane lane = new BpmnLane(LANE_NAME, bpmnDiagram);
	 
		// when
		lane.setWidth(NEW_WIDTH);
		
		// then
		assertEquals(NEW_WIDTH, lane.getWidth());
	}
	
	@Test
	public void changeXTest () {

		// given
		BpmnLane lane = new BpmnLane(LANE_NAME, bpmnDiagram);
	 
		// when
		lane.setX(NEW_X);
		
		// then
		assertEquals(NEW_X + BpmnConstants.DEFAULT_X, lane.getX());
	}
	
	@Test
	public void setNameTest () {

		// given
		BpmnLane lane = new BpmnLane(LANE_NAME, bpmnDiagram);
	 
		// when
		lane.setName(NEW_LANE_NAME);
		
		// then
		assertEquals(NEW_LANE_NAME, lane.getName());
		assertEquals(NEW_LANE_NAME, lane.getLane().getName());
	}
	
}
