package main.service.bpmn.models;

import java.util.ArrayList;
import java.util.List;

import main.util.bpmn.BpmnConstants;
import main.util.bpmn.Utils;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.di.impl.BpmnDiFactoryImpl;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;

/**
 * @author Joerg Hilscher
 * Abstract superclass for all bpmn-elements.
 */
public abstract class BpmnElement {

	/**
	 * Diagram this element is part of.
	 */
	protected BPMNDiagram bpmnDiagram;
	
	/**
	 * process this element is part of.
	 */
	protected org.eclipse.bpmn2.Process process;
	
	/**
	 * bpmn factory to create bpmn-elements.
	 */
	protected Bpmn2Factory factory;

	/**
	 * the specific bpmn-element.
	 */
	protected FlowNode element;
	
	/**
	 * shape of this element, used for rendering.
	 */
	protected BPMNShape bpmnShape;
	
	/**
	 * the edges from the previous element to this element.
	 */
	private List<BPMNEdge> edgesFromPreviousElements;

	/**
	 * the edges from the this element to following element.
	 */
	private List<BPMNEdge> edgesToFollowingElements;
	
	/**
	 * initial x.
	 */
	protected int x = BpmnConstants.DEFAULT_X;
	
	/**
	 * initial y.
	 */
	protected int y = BpmnConstants.DEFAULT_Y;
	
	/**
	 * width of the element.
	 */
	protected int width = BpmnConstants.DEFAULT_TASK_WIDTH;
	
	/**
	 * height of the element.
	 */
	protected int height = BpmnConstants.DEFAULT_TASK_HEIGHT;
	
	/**
	 * List of previous elements - this class is a doubled chained list.
	 */
	private List<BpmnElement> previousElements;
	
	/**
	 * List of following elements - this class is a doubled chained list.
	 */
	private List<BpmnElement> followingElements;
	
	/**
	 * this element can be part of a lane.
	 */
	private BpmnLane bpmnLane;
	
	/**
	 * the position in the grid: x,y.
	 */
	private int[] gridPostion;

	/**
	 * Creates a BPMN Element.
	 * Use a SubClass for a specific BPMN-Element.
	 * 
	 * @param name			Title of the Element - can be null or empty
	 * @param bpmnDiagram	The element is part of this diagram
	 * @param process		The element is part of this process
	 * @param prevElement	Previous element to this element/ can be null for startElements - can be null
	 * @param flowName		Title of the Flow from the previous Element to this Element - can be null or empty
	 * @param bpmnLane		Lane this element is part of - can be null
	 * @param grid			Grid this element should be part of - can be null
	 */
	public void createBpmnElement(String name, BPMNDiagram bpmnDiagram, 
			org.eclipse.bpmn2.Process process, BpmnElement prevElement, 
			String flowName, BpmnLane bpmnLane, Grid grid) {
		
		// these values shall not be null
		if (element == null || bpmnDiagram == null || process == null) {
			throw new IllegalArgumentException();
		}
		
		this.bpmnDiagram = bpmnDiagram;
		this.process = process;
		this.previousElements = new ArrayList<BpmnElement>();
		this.followingElements = new ArrayList<BpmnElement>();
		this.edgesFromPreviousElements = new ArrayList<BPMNEdge>();
		this.edgesToFollowingElements = new ArrayList<BPMNEdge>();
		
		// set the name of this element
		element.setName(name);

		// Add the element to the process
		process.getFlowElements().add(element);
		
		// Add element to the lane
		if (bpmnLane != null) {
			this.bpmnLane = bpmnLane;
			this.bpmnLane.AddElement(this);
		}
		
		// Create the shape
		this.createShape(prevElement);
		
		if (grid != null)
			gridPostion = grid.addElement(this, prevElement);
		
		// Connect this element with the previous element
		if (prevElement != null) {
			connectWithPrevBpmnElement(prevElement, flowName);
		}
	}
	
	/**
	 * Creates the shape of the element and positions it.
	 * @param prevElement previous BpmnElement to this element.
	 */
	private void createShape(final BpmnElement prevElement) {
		
		// Create Bounds of the element
		Bounds diBounds = DcFactory.eINSTANCE.createBounds();

		// Creates the shape
		bpmnShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
		
		// Connect the shape to the BPMNElement
		bpmnShape.setBounds(diBounds);
		bpmnShape.setBpmnElement(element);
		bpmnShape.setIsHorizontal(true);
		Utils.addShape(bpmnShape, bpmnDiagram);
				
		// this sets the values of the bounds too 
		this.setHeight(this.height);
		this.setWidth(this.width);
		this.setX(this.x);
		this.setY(this.y);
	}
	
	/**
	 * Connects the current element with the previous one.
	 * 
	 * @param prevElement	 	the previous Element 
	 * @param name 				the name of the FLOW from the previous element to the current element
	 */
	public final void connectWithPrevBpmnElement(BpmnElement prevElement, String name) {
					
			// Simple calculation of the start and end of the edge
			int sourceX = prevElement.x + prevElement.width;
			int sourceY = prevElement.y + (prevElement.height / 2);
			int targetX = this.x;
			int targetY = this.y + (this.height / 2);
			
			// Add prevElement to the list, this works like a linked list
			this.previousElements.add(prevElement);
			prevElement.getFollowingElements().add(this);
			
			SequenceFlow flow = factory.createSequenceFlow();
			flow.setName(name);
			
			BpmnDiFactory diFactory = BpmnDiFactoryImpl.init();
			
			// Create the EDGE
			BPMNEdge edge = diFactory.createBPMNEdge();
			edge.setSourceElement(prevElement.bpmnShape);
			edge.setTargetElement(bpmnShape);
			
			// Endpoint - is at the End of the element
			Point endPoint = DcFactory.eINSTANCE.createPoint();
			endPoint.setX(targetX);
			endPoint.setY(targetY);
			
			// Startpoint
			Point startPoint = DcFactory.eINSTANCE.createPoint();
			startPoint.setX(sourceX);
			startPoint.setY(sourceY);
			
			// Set start waypoint
			edge.getWaypoint().add(startPoint);

			// Set end waypoint
			edge.getWaypoint().add(endPoint);
			
			// Connect the Edge with the Flow
			edge.setBpmnElement(flow);

			element.getIncoming().add(flow);

			prevElement.element.getOutgoing().add(flow);
			
			// allocate the edges to the elements
			edgesFromPreviousElements.add(edge);
			prevElement.getEdgesToFollowingElements().add(edge);
			
			Utils.addShape(edge, bpmnDiagram);
			
			// Add the flow to the process
			this.process.getFlowElements().add(flow);
		}

	/**
	 * Constructor should only be used by sub-classes.
	 */
	protected BpmnElement() { }

	/**
	 * get the x-position.
	 * @return x-position
	 */
	public final int getX() {
		return x;
	}

	/**
	 * Sets the x-position, and correctly moves the edges.
	 * @param x new x.
	 */
	public final void setX(int x) {
		// add a margin
		x += BpmnConstants.DEFAULT_X;
		
		this.bpmnShape.getBounds().setX(x);
		this.x = x;
		
		// adjust waypoints TO this element		
		for (BPMNEdge edge : edgesFromPreviousElements) {
			if (edge.getWaypoint().size() > 0) {
				int last = edge.getWaypoint().size() - 1;	
				edge.getWaypoint().get(last).setX(this.x);
			}
		}
		
		// adjust waypoints FROM this element			
		for (BPMNEdge edge : edgesToFollowingElements) {
			if (edge.getWaypoint().size() > 0) {
				edge.getWaypoint().get(0).setY(this.y + this.width);
			}
		}
	}

	/**
	 * gets y-value.
	 * @return y.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Moves the element and it's edges to the new y-position.
	 * @param y new y-position.
	 */
	public void setY(int y) {
		// add a margin
		y += BpmnConstants.DEFAULT_Y;
		
		this.bpmnShape.getBounds().setY(y);
		this.y = y;
		
		// adjust waypoints TO this element		
		for (BPMNEdge edge : edgesFromPreviousElements) {
			if (edge.getWaypoint().size() > 0) {
				int last = edge.getWaypoint().size() - 1;	
				edge.getWaypoint().get(last).setY(this.y + (this.height / 2));
			}
		}
		
		// adjust waypoints FROM this element			
		for (BPMNEdge edge : edgesToFollowingElements) {
			if (edge.getWaypoint().size() > 0) {
				edge.getWaypoint().get(0).setY(this.y + (this.height / 2));
			}
		}
	
	}

	public List<BPMNEdge> getEdgesFromPreviousElements() {
		return edgesFromPreviousElements;
	}

	public List<BPMNEdge> getEdgesToFollowingElements() {
		return edgesToFollowingElements;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.bpmnShape.getBounds().setWidth(width);
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.bpmnShape.getBounds().setHeight(height);
		this.height = height;
	}

	public List<BpmnElement> getPreviousElements() {
		return previousElements;
	}

	public List<BpmnElement> getFollowingElements() {
		return followingElements;
	}

	public void setFollowingElements(List<BpmnElement> followingElements) {
		this.followingElements = followingElements;
	}

	public FlowNode getElement() {
		return element;
	}

	public void setElement(FlowNode element) {
		this.element = element;
	}
	
	public String getName() {
		return this.element.getName();
	}
	
	public void setName(String name) {
		this.element.setName(name);
	}
	
	public int[] getGridPostion() {
		return gridPostion;
	}

	public void setGridPostion(int[] gridPostion) {
		this.gridPostion = gridPostion;
	}

	public BpmnLane getBpmnLane() {
		return bpmnLane;
	}

	public void setBpmnLane(BpmnLane bpmnLane) {
		this.bpmnLane = bpmnLane;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bpmnLane == null) ? 0 : bpmnLane.hashCode());
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result
				+ ((previousElements == null) ? 0 : previousElements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BpmnElement other = (BpmnElement) obj;
		if (bpmnLane == null) {
			if (other.bpmnLane != null) {
				return false;
			}
		} else if (!bpmnLane.equals(other.bpmnLane)) {
			return false;
		}
		if (element == null) {
			if (other.element != null) {
				return false;
			}
		} else if (!element.getName().equals(other.element.getName())) {
			return false;
		}
		if (previousElements == null) {
			if (other.previousElements != null) {
				return false;
			}
		} else if (!previousElements.equals(other.previousElements)) {
			return false;
		}
		return true;
	}

	public BPMNShape getBpmnShape() {
		return bpmnShape;
	}
}
