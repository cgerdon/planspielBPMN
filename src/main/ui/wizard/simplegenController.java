package main.ui.wizard;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.constraints.Min;

import main.service.bpmn.BpmnProcessingService;
import main.util.bpmn.ExampleBpmns;


/**
 * Controller for JSF.
 * @author Christoph Gerdon
 *
 */
@ManagedBean(name = "simplegen")
@SessionScoped
public class simplegenController  {
	private String taskName = "";
	private String flowName = "";
	private String laneName = "";
	private String poolName = "";
	
	@Min(0)
	private int laneIndex = 0;
	
	private static BpmnProcessingService bpmnService;
	
	/**
	 * Constructor.
	 * @throws IOException
	 */
	public simplegenController() throws IOException {
			bpmnService = BpmnProcessingService.getInstance();		
	}
	/**
	 * Calles the bpmnService to clear the bpmn.
	 */
	public void clear() {
		bpmnService.clearBpmn();
	}
	/**
	 * used to mock an example bpmn.
	 */
	public void mocken() {
		ExampleBpmns example;
		try {
			example = new ExampleBpmns();
			example.mock();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	/**
	 * Used to simulate the wizard input.
	 */
	public void simulateWizardInput() {
		ExampleBpmns example;
		try {
			example = new ExampleBpmns();
			example.simulateWizardInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getLaneName() {
		return laneName;
	}

	public void setLaneName(String laneName) {
		this.laneName = laneName;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public int getLaneIndex() {
		return laneIndex;
	}

	public void setLaneIndex(int laneIndex) {
		this.laneIndex = laneIndex;
	}


	
}
