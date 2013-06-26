package main.service.bpmn.models;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.di.BPMNDiagram;

/**
 * 
 * @author Joerg
 *
 */
public class BpmnUserTask extends BpmnElement {
	
	/**
	 * Constructs the element.
	 * @param name Name of this element.		
	 * @param bpmnDiagram Insert this element into this diagram.
	 * @param process Insert this element into this process.
	 * @param prevElement Previous BpmnElement to this element, can be null.
	 * @param flowName Name of the sequence flow from the prev-element to this element.
	 * @param bpmnLane Lane of this element.
	 * @param grid Grid, the element should be inserted to.
	 */
	public BpmnUserTask(String name, BPMNDiagram bpmnDiagram, 
			org.eclipse.bpmn2.Process process, BpmnElement prevElement, 
			String flowName, BpmnLane bpmnLane, Grid grid) {
		
		super();

		this.factory = Bpmn2Factory.eINSTANCE;
		
		element = factory.createUserTask();
		
		super.createBpmnElement(name, bpmnDiagram, process, prevElement, flowName, bpmnLane, grid);

	}

}
