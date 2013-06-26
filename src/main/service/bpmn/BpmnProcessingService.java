package main.service.bpmn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.domain.bpmn.Document;
import main.domain.bpmn.User;
import main.service.bpmn.models.BpmnElement;
import main.service.bpmn.models.BpmnEndEvent;
import main.service.bpmn.models.BpmnExclusiveGateway;
import main.service.bpmn.models.BpmnLane;
import main.service.bpmn.models.BpmnParallelGateway;
import main.service.bpmn.models.BpmnPool;
import main.service.bpmn.models.BpmnStartEvent;
import main.service.bpmn.models.BpmnUserTask;
import main.service.bpmn.models.Grid;
import main.util.bpmn.IOUtil;

import org.apache.log4j.Logger;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.dd.di.Diagram;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Joerg Hilscher
 * Logic to take user input and make a logical bpmn out of it.
 * Singleton.
 */
public class BpmnProcessingService {

	/**
	 * Logging with log4j.
	 */
	private static Logger log = Logger.getLogger(BpmnProcessingService.class);
	
	/**
	 * Only instance of this class.
	 */
	private static BpmnProcessingService instance;
	
	/**
	 * List of bpmn elements.
	 */
	private static List<BpmnElement> elements;

	/**
	 * for reading and writing out the bpmn file.
	 */
	private static IOUtil iOUtil;

	/**
	 * resource out of the input file.
	 */
	private static Resource resource;

	/**
	 * definitions out of the input file.
	 */
	private static Definitions definitions;

	/**
	 * process out of the input file.
	 */
	private static org.eclipse.bpmn2.Process process;

	/**
	 * Diagram out of the input file.
	 */
	private static Diagram diagram;

	/**
	 * BpmnDiagram out of the input file.
	 */
	private static BPMNDiagram bpmnDiagram;

	/**
	 *  Pool.
	 */
	private static BpmnPool bpmnPool;

	/**
	 * lanes, with User as key.
	 */
	private static Map<User, BpmnLane> userLanes;

	/**
	 * Map that holds the latestSteps of the roleplay.
	 * Document is used as a connection between bpmn-tasks, therefore
	 * you can get the previous bpmn-element out of it.
	 */
	private static Map<Document, BpmnElement> latestSteps;
	
	/**
	 * the grid.
	 */
	private static Grid grid;

	/**
	 * the first element of a process.
	 */
	private static BpmnStartEvent startEvent; 
	
	/**
	 * Counts the rounds.
	 */
	private static int roundNr = 0;
	
	/**
	 * Counts the elements of one round.
	 */
	private static int stepCount = 0;
	
	
	
	/**
	 * creates this class and inits the IOutil.
	 */
	private BpmnProcessingService() {

		if (elements == null) {
			elements = new ArrayList<BpmnElement>();
		}

		userLanes = new HashMap<User, BpmnLane>();
		latestSteps = new HashMap<Document, BpmnElement>();
		
		// Get IOUtil, which will load and save the resource
		iOUtil = IOUtil.getInstance();

	
		try {
			initBpmnProcess();
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * Gets the only instance of this class.
	 * @return Instance of BpmnProcessingService;
	 */
	public static BpmnProcessingService getInstance() {
		
		if (instance == null)
			instance = new BpmnProcessingService();
		
		return instance;
	}
	
	/**
	 * Loads all the resources from the input file.
	 * Resets all elements before loading, so it can be used to clear a process.
	 * @throws IOException Exception, when loading the file fails.
	 */
	private void initBpmnProcess() throws IOException {

		log.info("initalize BpmnProcessingService ...");
		
		// Load the resource
		resource = iOUtil.loadFromBpmnFile();

		// Definitions of the xml file
		definitions = (Definitions) resource.getContents().get(0).eContents().get(0);

		// We know there is only one child: our process 
		process = (org.eclipse.bpmn2.Process) definitions.getRootElements().get(0);

		// Get Diagram
		diagram = definitions.getDiagrams().get(0);

		// Convert to BPMNDiagram
		bpmnDiagram = (BPMNDiagram) diagram;

		// Reset elements
		elements.clear();
		userLanes.clear();
		latestSteps.clear();
		startEvent = null;
		
		bpmnPool = null;
		grid = null;
				
		roundNr = 0;
		stepCount = 0;
	}

	/**
	 * resets the bpmn-process.
	 * Simply resets all elements and saves it to the output file.
	 */
	public final void clearBpmn() {
		
		log.info("Clearing Bpmn ...");
		
		try {
			initBpmnProcess();

			saveToBpmnFile();
		} catch (IOException e) {
			log.error(e);

			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a pool.
	 * Only creates the pool if it's not null.
	 * @param name Name of the pool.
	 * @return the Pool-Element.
	 */
	public final BpmnPool createPool(final String name) {
		if (bpmnPool == null)
			bpmnPool = new BpmnPool(name, bpmnDiagram, definitions, process);
		return bpmnPool;
	}
	
	/**
	 * adds a lane to the pool.
	 * @param user User who will represent a lane.
	 */
	public final void addLane(final User user) {
		
		if (bpmnPool == null)
			createPool("default");
		
		// cancel if lane is already there
		if (userLanes.get(user) != null) {
			log.info("Lane of user is already there.");
			return;
		}
		
		log.info("Creating new lane ... ");
		
		BpmnLane lane = bpmnPool.addLane(user.getRole());
		
		userLanes.put(user, lane);
		
		if (grid == null)
			createGrid();
		else 
			grid.addLane(lane);
		
		log.info("adding lane: " + lane.toString());
		
		saveToBpmnFile();
	}
	
	/**
	 * Creates the grid with a pool.
	 * It's recommended to create the crid directly with a pool.
	 * All lanes should be attached in the pool.
	 * @return the grid.
	 */
	public final Grid createGrid() {
		if (bpmnPool == null)
			createPool("default");
		
		grid = new Grid(bpmnPool);
		
		saveToBpmnFile();
		
		return grid;
	}
	
	/**
	 * Adds an Element within a round.
	 * Main method to fill the bpmn-process with user-input.
	 * @param action can not be null
	 * @param decision  can be null
	 * @param reason can be null
	 * @param lane should not be null
	 * @param documents can be null or empty
	 */
	public final void addElement (String action, String decision, String reason, User user, List<Document> documents ) {
		
		/**
		 * Workflow of this method:
		 * 1. Get lane from user.
		 * 2. Get previous elements from documents.
		 * 3. Check if the element is already there from a previous round.
		 * 4. Construct new element.
		 * 5. Update latest steps.
		 */
		
		log.info("Adding element ...");
		
		// throw Exception if action is null or empty!
		if (action == null || action.isEmpty())
			throw new IllegalArgumentException();
		
		// count the steps
		stepCount++;
		
		// 1. Get lane
		BpmnLane lane = userLanes.get(user);
		
		// If the lane is not created before
		if (lane == null) {
			if (bpmnPool == null)
				createPool("default");
			
			lane = bpmnPool.addLane(user.getRole());
			
			userLanes.put(user, lane);
			
			if (grid == null) {
				createGrid();
			}
		}
		
		// 2. Get previous element(s) by documents
		List<BpmnElement> previousElements = getPreviousElementsOutOfDocuments(documents);
		
		// 3. Check if element is already there
		BpmnElement element = null;
		
		if (decision == null || decision.isEmpty()) {
			element = searchElement(action, lane, previousElements);
		}
		
		// Update lastestSteps and cancel if element is already there
		if (element != null) {
			
			for (Document document : documents) { 
				latestSteps.put(document, element);
			}
			return;
		}
		
		// 4. Construct new element
		
		// Create StartEvent, if first Element
		BpmnElement userTask = null;
		
		// if it's the first element, then add a start-element
		if (previousElements.size() == 0 && elements.size() == 0) {
			startEvent = new BpmnStartEvent(null, bpmnDiagram, process, null, null, lane, grid);
		}
		
		// if no decision -> simple user task
		if (decision == null || decision.isEmpty()) {
			
			BpmnElement parallelGateway = null;
			
			// if more than 1 prev element -> Gateway
			if (previousElements.size() > 1) {
				parallelGateway = searchElement(null, lane, previousElements);
				
				if (parallelGateway == null) {
					parallelGateway = new BpmnParallelGateway(null, bpmnDiagram, process, 
							 previousElements.get(0), null, lane, grid);
					
					// add further previous elements
					for (int i = 1; i < previousElements.size(); i++) {
						parallelGateway.connectWithPrevBpmnElement(previousElements.get(i), null);
					}
					
					elements.add(parallelGateway);
				}
				
				// Check again for the usertask
				List<BpmnElement> prev = new ArrayList<>();
				prev.add(parallelGateway); 
				
				userTask = searchElement(action, lane, prev);
				
				// if new element, add it to the bpmn
				if (userTask == null) {
					userTask = new BpmnUserTask(action, bpmnDiagram, process, parallelGateway , reason, lane, grid);
				}
				
			} else {
			
				userTask = new BpmnUserTask(action, bpmnDiagram, process, 
						previousElements.size() > 0 ? previousElements.get(0) : startEvent , null, lane, grid);
				
				// add further previous elements
				for (int i = 1; i < previousElements.size(); i++) {
					userTask.connectWithPrevBpmnElement(previousElements.get(i), reason);
				}

			}
			
		} else {
			
			// If decicion: Gateway and then a usertask
			
			// Check if gateway already exists
			BpmnElement gateway = searchElement(action, lane, previousElements);
			
			// Create gateway, if not found
			if (gateway == null) {
				gateway = new BpmnExclusiveGateway(action, bpmnDiagram, process, 
					previousElements.size() > 0 ? previousElements.get(0) : startEvent , null, lane, grid);
				
				for (int i = 1; i < previousElements.size(); i++) {
					gateway.connectWithPrevBpmnElement(previousElements.get(i), null);
				}
				
				elements.add(gateway);
			}
			
			// Check again for the usertask
			List<BpmnElement> prev = new ArrayList<>();
			prev.add(gateway); 
			
			userTask = searchElement(decision, lane, prev);
			
			// if new element, add it to the bpmn
			if (userTask == null) {
				userTask = new BpmnUserTask(decision, bpmnDiagram, process, gateway , reason, lane, grid);
				
			}
		}
		
		elements.add(userTask);
		
		// 5. Put element in latest Steps
		for (Document document : documents) { 
			latestSteps.put(document, userTask);
		}
		
		saveToBpmnFile();
	}

	/**
	 * Gets the previous elements out of a list of documents, without any duplicates.
	 * @param documents List of documents.
	 * @return List of previous Elements.
	 */
	public List<BpmnElement> getPreviousElementsOutOfDocuments(List<Document> documents) {
		
		List<BpmnElement> previousElements = new ArrayList<BpmnElement>();
		
		// LinkedHashSet to restrain the order
		final Set<BpmnElement> setWithoutDuplicates = new LinkedHashSet<BpmnElement>(); 

		for (Document document : documents) {
			BpmnElement element = latestSteps.get(document);
			setWithoutDuplicates.add(element);
		}

		for (BpmnElement ele : setWithoutDuplicates) {
			if (ele != null)
				previousElements.add(ele);
		}
		
		return previousElements;
	}
	
	/**
	 * Starts a new round and finishes the old round.
	 * Saves the bpmn.
	 * Adds EndEvents to all open processes.
	 */
	public void startNewRound() {
		roundNr++;

		for(BpmnElement ele : latestSteps.values()) {
			if (ele.getFollowingElements().size() == 0)
				new BpmnEndEvent(null, bpmnDiagram, process, ele, null, ele.getBpmnLane(), grid);
		}
			
		latestSteps.clear();
		
		saveToBpmnFile();
	}
	
	/**
	 * Searches an element.
	 * @param name Name of the element to look for.
	 * @param lane Lane of the element to look for.
	 * @return BpmnElement.
	 */
	private BpmnElement searchElement (String name, BpmnLane lane, List<BpmnElement> previousElements) {
		
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getName() == name && elements.get(i).getBpmnLane().equals(lane)) {
				return (checkElements(previousElements, elements.get(i)))?
					 elements.get(i) : null;	
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if the previous elements are equal.
	 * @param previousElements
	 * @param element
	 * @return Boolean if they are equal
	 */
	private Boolean checkElements(List<BpmnElement> previousElements, BpmnElement element) {
		
		Boolean areEqual = true;
		
		for (BpmnElement prevElementofList : previousElements) {
			 if (!element.getPreviousElements().contains(prevElementofList))
				 areEqual = false;			
		}
		return areEqual;
	}
	
	/**
	 * writes out the current bpmn to the output file.
	 */
	public final void saveToBpmnFile() {
		try {
			iOUtil.saveToBpmnFile(resource);
		}
		catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the name of the pool.
	 */
	public final void setPoolName (String name) {
		if (bpmnPool != null)
			bpmnPool.setName(name);
	}
	
	/**
	 * Gets the pool.
	 * @return
	 */
	public static BpmnPool getBpmnPool() {
		return bpmnPool;
	}

	/**
	 * Sets the pool.
	 * @param bpmnPool
	 */
	public final void setBpmnPool(final BpmnPool bpmnPool) {
		BpmnProcessingService.bpmnPool = bpmnPool;
	}

	public static List<BpmnElement> getElements() {
		return elements;
	}

	public static void setElements(List<BpmnElement> elements) {
		BpmnProcessingService.elements = elements;
	}

	public static org.eclipse.bpmn2.Process getProcess() {
		return process;
	}

	public static void setProcess(org.eclipse.bpmn2.Process process) {
		BpmnProcessingService.process = process;
	}

	public static Diagram getDiagram() {
		return diagram;
	}

	public static void setDiagram(Diagram diagram) {
		BpmnProcessingService.diagram = diagram;
	}

	public static BPMNDiagram getBpmnDiagram() {
		return bpmnDiagram;
	}

	public static void setBpmnDiagram(BPMNDiagram bpmnDiagram) {
		BpmnProcessingService.bpmnDiagram = bpmnDiagram;
	}

	public static Grid getGrid() {
		return grid;
	}

	public static void setGrid(Grid grid) {
		BpmnProcessingService.grid = grid;
	}

	public static Map<User, BpmnLane> getUserLanes() {
		return userLanes;
	}

	public static void setUserLanes(Map<User, BpmnLane> userLanes) {
		BpmnProcessingService.userLanes = userLanes;
	}

	public static Resource getResource() {
		return resource;
	}

	public static Map<Document, BpmnElement> getLatestSteps() {
		return latestSteps;
	}

	public static BpmnStartEvent getStartEvent() {
		return startEvent;
	}

	public static int getRoundNr() {
		return roundNr;
	}

	public static int getStepCount() {
		return stepCount;
	}
	
}
