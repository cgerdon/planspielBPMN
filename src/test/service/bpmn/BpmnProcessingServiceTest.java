package test.service.bpmn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import main.domain.bpmn.Document;
import main.domain.bpmn.User;
import main.service.bpmn.BpmnProcessingService;
import main.service.bpmn.models.BpmnElement;
import main.service.bpmn.models.BpmnLane;
import main.service.bpmn.models.BpmnPool;
import main.service.bpmn.models.Grid;
import main.util.bpmn.IOUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * 
 * @author Joerg
 * Test-Class.
 */

//@RunWith(MockitoJUnitRunner.class)
public class BpmnProcessingServiceTest {

	/**
	 * Test Data.
	 */
	final String POOL_NAME = "Name";
	final String NEW_POOL_NAME = "new Name";
	final String USER_NAME_1 = "user 1";
	final String USER_NAME_2 = "user 2";
	final String USER_NAME_3 = "user 3";
	final String DOKUMENT_NAME = "test doku";
	final String ACTION_NAME_1 = "action 1";
	final String ACTION_NAME_2 = "action 2";
	final String ACTION_NAME_3 = "action 3";
	final String REASON_NAME = "reason 1";
	final String DECICION_NAME = "dec 1";
	final int NUMBER_OF_ELEMENTS = 64;
		
	//@Mock
	private static ServletContext servletContext;
	
	@BeforeClass
	public static void setupClass () throws UnsupportedEncodingException {
				
		// Init IOUtil
		@SuppressWarnings("unused")
		IOUtil iOUtil = IOUtil.getInstance();	
		
		// Mock data.
		IOUtil.setPath("");
		IOUtil.setDisc("");
		
		
		//servletContext = Mockito.mock(ServletContext.class);
		
		servletContext = new MockServlet();
		
		/*
		Mockito.when(servletContext.getResourceAsStream(Matchers.any(String.class)))
			.thenReturn(BpmnTestHelper.getMockInputStream());
		
		Mockito.when(servletContext.getRealPath(Matchers.any(String.class)))
				.thenReturn("");
		*/
		// Insert mock servlet
		IOUtil.setServletContext(servletContext);
	}
	
	@Before
	public void before() throws UnsupportedEncodingException {
		
		BpmnProcessingService service = BpmnProcessingService.getInstance();
		service = null;
		service = BpmnProcessingService.getInstance();
		
		// this resets all data in the service.
		service.clearBpmn();
	}
	
	@After
	public void after() {
		BpmnProcessingService service = BpmnProcessingService.getInstance();
		
		// this resets all data in the service.
		service.clearBpmn();
	}
	
	@Test
	public void singletonTest () {
		
		// Given
		BpmnProcessingService service1 = BpmnProcessingService.getInstance();
		BpmnProcessingService service2 = BpmnProcessingService.getInstance();
		
		// Then
		assertEquals(service1, service2);
	}
	
	@Test
	public void poolTest () {
		
		// Given
		BpmnProcessingService service = BpmnProcessingService.getInstance();
		
		// When
		BpmnPool pool = service.createPool(POOL_NAME);
		
		// Then
		assertEquals(pool, BpmnProcessingService.getBpmnPool());
	}
	
	@Test
	public void gridTest () {
		
		
		// Given
		BpmnProcessingService service = BpmnProcessingService.getInstance();
		
		// When
		Grid grid = service.createGrid();
		
		// Then
		assertEquals(grid, BpmnProcessingService.getGrid());
	}
	
	@Test
	public void setPoolNameTest () {
			
		// Given
		BpmnProcessingService service = BpmnProcessingService.getInstance();
		
		// When
		BpmnPool pool = service.createPool(POOL_NAME);
		service.setPoolName(NEW_POOL_NAME);
		
		// Then
		assertEquals(NEW_POOL_NAME, pool.getName());
	}
	
	@Test
	public void clearTest () {
		
		// Given
		BpmnProcessingService bpmnService = BpmnProcessingService.getInstance();
		
		
		// Insert Data into service.
		bpmnService.createPool(POOL_NAME);

		User user1 = new User(USER_NAME_1, 1);
		User user2 = new User(USER_NAME_2, 2);
		User user3 = new User(USER_NAME_3, 3);
		
		
		bpmnService.addLane(user1);
		bpmnService.addLane(user2);
		bpmnService.addLane(user3);
		
		// When
		bpmnService.clearBpmn();
		
		// Then
		assertEquals(0, BpmnProcessingService.getElements().size());
			
		assertEquals(0, BpmnProcessingService.getLatestSteps().size());
		
		assertNull(BpmnProcessingService.getGrid());
		assertNull(BpmnProcessingService.getBpmnPool());
		assertNull(BpmnProcessingService.getStartEvent());
		
		assertTrue(BpmnProcessingService.getUserLanes().isEmpty());
	}
	
	@Test
	public void initTest () {		
		
		// Given
		BpmnProcessingService bpmnService = BpmnProcessingService.getInstance();
		
		// When
		bpmnService.createPool(POOL_NAME);

		User user1 = new User(USER_NAME_1, 1);
		User user2 = new User(USER_NAME_2, 2);
		User user3 = new User(USER_NAME_3, 3);
		
		
		bpmnService.addLane(user1);
		bpmnService.addLane(user2);
		bpmnService.addLane(user3);
		
		// Then
		assertNotNull(BpmnProcessingService.getGrid());
		assertNotNull(BpmnProcessingService.getBpmnPool());
		assertEquals(3, BpmnProcessingService.getUserLanes().size());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void addElementTestWithoutInitTest () {
		
		// Given
		BpmnProcessingService bpmnService = BpmnProcessingService.getInstance();
		User user1 = new User(USER_NAME_1, 1);
		final Document d1 = new Document(DOKUMENT_NAME, "1", 1);
		
		
		// When
		bpmnService.addElement(ACTION_NAME_1, null, null , user1, new ArrayList<Document>() {{add(d1);}});

		// Then
		assertEquals(1, BpmnProcessingService.getUserLanes().size());
		assertEquals(1, BpmnProcessingService.getElements().size());
		assertEquals(1, BpmnProcessingService.getLatestSteps().size());
		assertNotNull(BpmnProcessingService.getStartEvent());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void addElementTestWithInitTest () {
		
		// Given
		BpmnProcessingService bpmnService = BpmnProcessingService.getInstance();
		bpmnService.createPool(POOL_NAME);
		User user1 = new User(USER_NAME_1, 1);
		final Document d1 = new Document(DOKUMENT_NAME, "1", 1);
		
		
		// When
		bpmnService.addElement(ACTION_NAME_1, null, null , user1, new ArrayList<Document>() {{add(d1);}});

		// Then
		assertEquals(1, BpmnProcessingService.getUserLanes().size());
		assertNotNull(BpmnProcessingService.getUserLanes().get(user1));
		assertEquals(1, BpmnProcessingService.getElements().size());
		assertEquals(1, BpmnProcessingService.getLatestSteps().size());
		assertNotNull(BpmnProcessingService.getStartEvent());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void addElementsTestWithGatewayTest () {
		
		// Given
		BpmnProcessingService bpmnService = BpmnProcessingService.getInstance();
		bpmnService.createPool(POOL_NAME);
		User user1 = new User(USER_NAME_1, 1);
		final Document d1 = new Document(DOKUMENT_NAME, "1", 1);
		
		
		// When
		bpmnService.addElement(ACTION_NAME_1, null, null , user1, new ArrayList<Document>() {{add(d1);}});
		bpmnService.addElement(ACTION_NAME_2, DECICION_NAME, REASON_NAME , user1, new ArrayList<Document>() {{add(d1);}});

		// Then
		assertEquals(1, BpmnProcessingService.getUserLanes().size());
		assertEquals(3, BpmnProcessingService.getElements().size());
		assertEquals(BpmnProcessingService.getLatestSteps().get(d1), BpmnProcessingService.getElements().get(BpmnProcessingService.getElements().size() - 1));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void addElementsWithMultiLanesTest () {
		
		// Given
		BpmnProcessingService bpmnService = BpmnProcessingService.getInstance();
		bpmnService.createPool(POOL_NAME);
		User user1 = new User(USER_NAME_1, 1);
		User user2 = new User(USER_NAME_2, 2);
		User user3 = new User(USER_NAME_3, 3);
		
		bpmnService.addLane(user1);
		bpmnService.addLane(user2);
		bpmnService.addLane(user3);
		
		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		
		List<Document> documents = new ArrayList<Document>();
		
		final Document d1 = new Document(DOKUMENT_NAME, "1", 1);
		final Document d2 = new Document(DOKUMENT_NAME + "2", "2", 2);
		final Document d3 = new Document(DOKUMENT_NAME + "3", "3", 3);
		final Document d4 = new Document(DOKUMENT_NAME + "4", "4", 4);
		
		documents.add(d1);
		documents.add(d2);
		documents.add(d3);
		documents.add(d4);
		
		List<String> decicions = new ArrayList<String>();
		decicions.add(null);
		decicions.add(DECICION_NAME);
		
		List<String> actions = new ArrayList<String>();
		actions.add(ACTION_NAME_1);
		actions.add(ACTION_NAME_2);
		actions.add(ACTION_NAME_3);
		
		// When
		bpmnService.addElement(ACTION_NAME_1, null, null , user1, new ArrayList<Document>() {{add(d1);}});
		
		BpmnProcessingService.getLatestSteps().get(d1);
		
		// Assign random lanes to elements
		for (int i = 0; i < NUMBER_OF_ELEMENTS - 1; i++) {
			bpmnService.addElement(actions.get(BpmnTestHelper.getRandom(3)) + i, 
					decicions.get(BpmnTestHelper.getRandom(2)), null , 
					users.get(BpmnTestHelper.getRandom(3)), 
					documents.subList(0, BpmnTestHelper.getRandom(3) + 1));
		}

		Grid grid = BpmnProcessingService.getGrid();
		
		// Then
		for (BpmnElement element : BpmnProcessingService.getElements()) {			
			BpmnLane lane = grid.getLanes()[BpmnTestHelper.getRowOfElement(grid.getGrid(), element)];
			assertEquals(element.getBpmnLane(), lane);
		}
	}
}
