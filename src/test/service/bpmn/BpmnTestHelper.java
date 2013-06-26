package test.service.bpmn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Random;

import main.service.bpmn.models.BpmnElement;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Helper Class for unit-tests.
 * @author Joerg
 *
 */
public class BpmnTestHelper {

	final static String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\" id=\"_gC_2IIJpEeKtZuMdvQPFJA\">"
			+ "<process id=\"simple-service-task-process\" name=\"Default Process\">"
			+ "</process>"
			+ "<bpmndi:BPMNDiagram id=\"BPMNDiagram_1\" name=\"Default Process Diagram\">"
			+   "<bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"sample-process\">"
			+    "</bpmndi:BPMNPlane>"
			+  "</bpmndi:BPMNDiagram>"
			+ "</definitions>";
	
	/**
	 * Returns an InputStream used for unit-testing.
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static InputStream getMockInputStream () throws UnsupportedEncodingException {
		return new ByteArrayInputStream(XML.getBytes("UTF-8"));
	}
	
	/**
	 * Gets basic definitions of a BPMN2.
	 * @return definitions
	 */
	public static Definitions getDefinitions() {
		
		Resource resource = new Bpmn2ResourceFactoryImpl().createResource(URI.createURI("test.bpmn"));
	    
		InputStream iS;
		
		try {
			iS = new ByteArrayInputStream(XML.getBytes("UTF-8"));
			resource.load(iS, Collections.EMPTY_MAP);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Definitions of the xml file
		Definitions definitions = (Definitions) resource.getContents().get(0).eContents().get(0);	
		
		return definitions;
		
	}
	
	/**
	 * Checks if an element is part of the grid.
	 * @param grid
	 * @param element
	 * @return Boolean if it's in the grid.
	 */
	public static Boolean contains(BpmnElement[][] grid, BpmnElement element) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == element)
					return true;
			}
			
		}
		return false;
	}
	
	/**
	 * Returns the row (y-position) of an element within the grid.
	 * @param grid
	 * @param element
	 * @return y-index.
	 */
	public static int getRowOfElement(BpmnElement[][] grid, BpmnElement element) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == element)
					return j;
			}
			
		}
		return -1;
	}
	
	/**
	 * Counts the bpmn-elements inside the grid.
	 * @param grid
	 * @return Number of elements.
	 */
	public static int countElements(BpmnElement[][] grid) {
		int sum = 0;
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] != null)
					sum++;
			}
			
		}
		return sum;
	}
	
	/**
	 * Returns a random number in the range of length.
	 * @param length
	 * @return
	 */
	public static int getRandom(int length) {
	    int rnd = new Random().nextInt(length);
	    return rnd;
	}
}
