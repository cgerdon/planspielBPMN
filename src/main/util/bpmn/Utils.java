package main.util.bpmn;

import java.util.List;

import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.dd.di.DiagramElement;


/**
 *
 * @author Joerg
 * Utility Methods for working with the BPMN2 Libary
 *
 */
public class Utils {

	/**
	 * Adds a Shape to the Diagramm.
	 * @param elem
	 * @param bpmnDiagram
	 */
	public static void addShape(DiagramElement elem, BPMNDiagram bpmnDiagram) {
		List<DiagramElement> elements = bpmnDiagram.getPlane().getPlaneElement();
		elements.add(elem);
	}

	/**
	 *  Adds a Shape to the Diagram on the first position.
	 * @param elem
	 * @param bpmnDiagram
	 */
	public static void addShapeOnFirstPosition(DiagramElement elem, BPMNDiagram bpmnDiagram) {
		List<DiagramElement> elements = bpmnDiagram.getPlane().getPlaneElement();
		elements.add(0, elem);
	}

}
