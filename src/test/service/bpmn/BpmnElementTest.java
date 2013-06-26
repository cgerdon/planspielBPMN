package test.service.bpmn;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.List;

import main.service.bpmn.models.BpmnElement;
import main.service.bpmn.models.BpmnLane;
import main.service.bpmn.models.BpmnUserTask;
import main.service.bpmn.models.Grid;
import main.util.bpmn.BpmnConstants;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test Class for BpmnElement.
 * @author Joerg
 *
 */
public class BpmnElementTest {

	private static BPMNDiagram bpmnDiagram;
	private static Definitions definitions;
	private static org.eclipse.bpmn2.Process process;
	private static Grid grid;
	
	/*
	 * Test Data.
	 */
	final String NEW_NAME = "test-name";
	final int NEW_X = 30;
	final int NEW_X_EDGE = 400;
	
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

		//
		grid = new Grid();
		
	}
	
	@Test
	public void changeNameTest () {

		// given
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, null, null);
		
		// when
		simpleBpmnUserTask.setName(NEW_NAME);
		
		// then
		assertEquals(NEW_NAME, simpleBpmnUserTask.getName());
		assertEquals(NEW_NAME, simpleBpmnUserTask.getElement().getName());
	}
	
	@Test
	public void prevElementTest () {
		// given
		BpmnElement prevElement = new BpmnUserTask("name", bpmnDiagram, process, null, null, null, null);
		
		// when
		// new Element with prevElement
		BpmnElement newElement = new BpmnUserTask("newElement", bpmnDiagram, process, prevElement, null, null, null);
		
		// Assert
		assertEquals(prevElement, newElement.getPreviousElements().get(0));
		assertEquals(newElement, prevElement.getFollowingElements().get(0));
	}
	
	@Test
	public void multiplePrevElementsTest () {
	
		// given
		BpmnElement prevElement1 = new BpmnUserTask("name1", bpmnDiagram, process, null, null, null, null);
		BpmnElement prevElement2 = new BpmnUserTask("name2", bpmnDiagram, process, null, null, null, null);
		
		// when
		// new Element with prevElement
		// connect with a prevElement
		BpmnElement newElement = new BpmnUserTask("newElement", bpmnDiagram, process, prevElement1, null, null, null);
		newElement.connectWithPrevBpmnElement(prevElement2, null);
		
		// Assert
		assertEquals(2, newElement.getPreviousElements().size());
		assertTrue(newElement.getPreviousElements().contains(prevElement1));
		assertTrue(newElement.getPreviousElements().contains(prevElement2));
	}
	
	@Test
	public void elementWithLaneTest () {

		// Given
		BpmnLane lane = new BpmnLane("lane 1", bpmnDiagram);
		
		// when
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, lane, null);
		
		// then
		assertEquals(lane, simpleBpmnUserTask.getBpmnLane());
	}
	
	@Test
	public void changeXTest () {

		// given
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, null, null);
		
		// when
		simpleBpmnUserTask.setX(NEW_X);
		
		// then
		assertEquals(simpleBpmnUserTask.getX(), NEW_X + BpmnConstants.DEFAULT_X);
	}
	
	@Test
	public void addToProcessTest () {

		// given
		
		// when
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, null, null);
		
		// then
		assertTrue(process.getFlowElements().contains(simpleBpmnUserTask.getElement()));
	}
	
	@Test
	public void addToDiagramTest () {

		// given
			
		// when
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, null, null);
		
		// then
		assertTrue(bpmnDiagram.getPlane().getPlaneElement().contains(simpleBpmnUserTask.getBpmnShape()));
	}
	
	@Test
	public void bpmnEdgesTest () {

		// given
		BpmnElement simpleBpmnUserTask1 = new BpmnUserTask("name", bpmnDiagram, process, null, null, null, null);
		BpmnElement simpleBpmnUserTask2 = new BpmnUserTask("name", bpmnDiagram, process, simpleBpmnUserTask1, null, null, null);
		
		// when
		simpleBpmnUserTask2.setX(NEW_X_EDGE);
		List<BPMNEdge> edges = simpleBpmnUserTask2.getEdgesFromPreviousElements();
		
		// then
		assertEquals(1, edges.size());
		assertEquals(NEW_X_EDGE + BpmnConstants.DEFAULT_X, (int) edges.get(0).getWaypoint().get(1).getX());
	}
	
	@Ignore("Hamcrest package for instanceOf not found in unit-test enviroment")
	public void subclassInstanceTest () {

		// given
			
		// when
		BpmnElement simpleBpmnUserTask = new BpmnUserTask("name", bpmnDiagram, process, null, null, null, null);
		
		// then
		assertThat(simpleBpmnUserTask.getElement(), instanceOf(org.eclipse.bpmn2.UserTask.class));
	}
}
