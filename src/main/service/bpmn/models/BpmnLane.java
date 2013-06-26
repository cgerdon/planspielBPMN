package main.service.bpmn.models;

import java.util.ArrayList;
import java.util.List;

import main.util.bpmn.BpmnConstants;
import main.util.bpmn.Utils;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;

/**
 * 
 * @author Joerg Hilscher
 * BpmnLane Element
 *
 */
public class BpmnLane {

	/**
	 * Factory to create the bpmn2 element.
	 */
	protected Bpmn2Factory factory;
	
	/**
	 *  Shape of the Lane.
	 */
	private BPMNShape laneShape;
	
	/**
	 *  The bpmn2 lane element.
	 */
	private Lane lane;
	
	/**
	 *  List of BpmnElements on this lane.
	 */
	private List<BpmnElement> elements;
	
	/**
	 * Name of this lane.
	 */
	private String name;
	
	/**
	 *  X-margin of the lane.
	 */
	private int x = BpmnConstants.LANE_TO_POOL_MARGIN;
	
	/**
	 * y-margin of the lane.
	 */
	private int y = 0;
	
	/**
	 * Width of the lane.
	 */
	private int width = BpmnConstants.DEFAULT_LANE_WIDTH;
	
	/**
	 * Height of the lane.
	 */
	private int height = BpmnConstants.DEFAULT_LANE_HEIGHT;
	
	/**
	 * 
	 * @param laneName Name of the lane
	 * @param bpmnDiagram 
	 */
	public BpmnLane(String laneName, BPMNDiagram bpmnDiagram) {
		
		this.factory = Bpmn2Factory.eINSTANCE;
		this.elements = new ArrayList<BpmnElement>();
		
		// Create a new Lane
		lane = factory.createLane();
		lane.setName(laneName);		
		
		this.name = laneName;
		
		// Create laneShape
		laneShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
		Bounds laneBounds = DcFactory.eINSTANCE.createBounds();

		// laneShape.
		laneShape.setBounds(laneBounds);
		laneShape.setBpmnElement(lane);
		laneShape.setIsHorizontal(true);
		
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		
		Utils.addShape(laneShape, bpmnDiagram);
	}

	/**
	 * Add a BpmnElement to this lane.
	 * @param element BpmnElement to add to this lane.
	 */
	public void AddElement(BpmnElement element) {
		this.elements.add(element);
		this.lane.getFlowNodeRefs().add(element.getElement());
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Lane getLane() {
		return lane;
	}

	public void setX(int x) {
		x += BpmnConstants.DEFAULT_X;
		this.x = x;
		this.laneShape.getBounds().setX(x);
	}

	public void setY(int y) {
		y += BpmnConstants.DEFAULT_Y;
		this.y = y;
		this.laneShape.getBounds().setY(y);
	}

	public void setWidth(int width) {
		this.width = width;
		this.laneShape.getBounds().setWidth(width);
		
	}

	public void setHeight(int height) {
		this.height = height;
		this.laneShape.getBounds().setHeight(height);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		BpmnLane other = (BpmnLane) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BpmnLane [name=" + name + "]";
	}

	public BPMNShape getLaneShape() {
		return laneShape;
	}

	public void setLaneShape(BPMNShape laneShape) {
		this.laneShape = laneShape;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.lane.setName(name);
	}

	public void setLane(Lane lane) {
		this.lane = lane;
	}
	
	
}
