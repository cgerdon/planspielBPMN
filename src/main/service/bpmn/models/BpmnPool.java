package main.service.bpmn.models;

import java.util.ArrayList;
import java.util.List;

import main.service.bpmn.BpmnProcessingService;
import main.util.bpmn.BpmnConstants;
import main.util.bpmn.Utils;

import org.apache.log4j.Logger;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;

/**
 * 
 * @author Joerg Hilscher
 *
 * BPMNPool
 * A pool equals a participant
 * The participant is part of a Collaboration of 1-n participants
 * A participant has it's own process	
 * A participant has a laneset with 0-n lanes
 */
public class BpmnPool {

	/**
	 * Logging with log4j.
	 */
	private static Logger log = Logger.getLogger(BpmnPool.class);
	
	/**
	 * BPMN2.
	 */
	private BPMNDiagram bpmnDiagram;
	
	/**
	 * BPMN2.
	 */
	private Bpmn2Factory factory;

	/**
	 * BPMN2.
	 */
	private Participant participant;
	
	/**
	 * shape of the pool.
	 */
	private BPMNShape poolShape;
	
	/**
	 * margin to top.
	 */
	private int x = 0;
	
	/**
	 * margin to the left.
	 */
	private int y = 0;
	
	/**
	 * width.
	 */
	private int width = BpmnConstants.DEFAULT_LANE_WIDTH + BpmnConstants.LANE_TO_POOL_MARGIN;
	
	/**
	 * height.
	 */
	private int height = BpmnConstants.DEFAULT_LANE_HEIGHT;
	
	/**
	 * laneSet.
	 */
	private LaneSet laneSet;
	
	/**
	 * List of lanes.
	 */
	private List<BpmnLane> bpmnLanes;
	
	/**
	 * Creates a Pool without Lanes in it.
	 * @param poolName Name of the pool.
	 * @param bpmnDiagram Diagram this pool should be inserted into.
	 * @param definitions Definitions the related participant will be inserted into.
	 * @param process Process this pool should be insertede into.
	 */
	public BpmnPool(String poolName, BPMNDiagram bpmnDiagram, Definitions definitions, org.eclipse.bpmn2.Process process) {
		
		this.bpmnDiagram = bpmnDiagram;
		this.factory = Bpmn2Factory.eINSTANCE;

		// Create Collaboration
		Collaboration collaboration = factory.createCollaboration();
		collaboration.setName("Collaboration");
		
		// Create Participant, this is used as pool
		participant = factory.createParticipant();
		participant.setName(poolName);
		participant.setProcessRef(process);
		
		// Wire Collaboration
		collaboration.getParticipants().add(participant);
		definitions.getRootElements().add(collaboration);
		process.setDefinitionalCollaborationRef(collaboration);
	
		// Create a LaneSet
		laneSet = factory.createLaneSet();
		process.getLaneSets().add(laneSet);
	
		
		bpmnLanes = new ArrayList<BpmnLane>();
		
		// Create the poolShape
		poolShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
		Bounds poolBounds = DcFactory.eINSTANCE.createBounds();
		
		// Wire bounds to the poolShape
		poolShape.setBounds(poolBounds);
		poolShape.setBpmnElement(participant);
		poolShape.setIsHorizontal(true);
		
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		
		Utils.addShape(poolShape, bpmnDiagram);

		// need to set the BpmnElement manually
		bpmnDiagram.getPlane().setBpmnElement(process);
	}
	
	/**
	 * Adds a Lane to the Pool.
	 * Creates the lane and it's shape
	 * Adjusts size of the pool
	 * @param laneName		Name of the Lane
	 * @return				lane
	 */
	public BpmnLane addLane(String laneName) {
		
		// Create the lane
		BpmnLane bpmnLane = new BpmnLane(laneName, bpmnDiagram);
		
		log.info("Createde lane: " + bpmnLane);
		
		// check if duplicate
		for (BpmnLane lane : bpmnLanes) {
			if (lane.equals(bpmnLane)) {
				log.info("This lane is already in the pool.");
				return lane;
			}
		}
		
		// Add it to the  laneSet
		laneSet.getLanes().add(bpmnLane.getLane());
		bpmnLanes.add(bpmnLane);
		
		return bpmnLane;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		
		x += BpmnConstants.DEFAULT_X;
		this.x = x;
		poolShape.getBounds().setX(x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		y += BpmnConstants.DEFAULT_Y;
		this.y = y;
		poolShape.getBounds().setY(y);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		width += BpmnConstants.LANE_TO_POOL_MARGIN;
		this.width = width; 
		poolShape.getBounds().setWidth(width);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		poolShape.getBounds().setHeight(height);
	}

	public void setName(String name) {
		participant.setName(name);
	}
	
	public String getName() {
		return participant.getName();
	}
	
	@Override
	public String toString() {
		return "BpmnPool [participant=" + participant.getName() + ", x=" + x + ", y=" + y
				+ ", width=" + width + ", height=" + height + "]";
	}

	public List<BpmnLane> getBpmnLanes() {
		return bpmnLanes;
	}

	public void setBpmnLanes(List<BpmnLane> bpmnLanes) {
		this.bpmnLanes = bpmnLanes;
	}
}
