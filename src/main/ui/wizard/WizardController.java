package main.ui.wizard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import main.domain.bpmn.Document;
import main.domain.bpmn.Lane;
import main.domain.bpmn.Roleplay;
import main.domain.bpmn.User;
import main.domain.wizard.ActionGlossary;
import main.domain.wizard.DecisionGlossary;
import main.domain.wizard.Document_ProcessStep;
import main.domain.wizard.ProcessStep;
import main.domain.wizard.ReasonGlossary;
import main.service.bpmn.BpmnProcessingService;
import main.service.db.ActionGlossaryService;
import main.service.db.DbService;
import main.service.db.DecisionGlossaryService;
import main.service.db.ReasonGlossaryService;
import main.service.db.RoleplayService;
import main.ui.settings.DocumentBin;
import main.ui.settings.SettingsController;
import main.util.Constants;

import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

/**
 * 
 * @author pierre.bartholomae
 * @author christianroser
 *
 */
@Named("wizard")
@SessionScoped
public class WizardController implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -588316790187407042L;

	@Inject
	private UserListController uLC;
	
	@Inject
	private DocumentBin bin;
	
	/**
	 * Load Services.
	 */
	private static BpmnProcessingService bps;
	private DbService dbs;
	private ActionGlossaryService ags;
	private DecisionGlossaryService dgs;
	private ReasonGlossaryService rgs;
	private RoleplayService rps;
	
	/**
	 * All pre available Items.
	 */
	private List<ActionGlossary> actionGlossary;
	private List<DecisionGlossary> decisionGlossary;
	private List<ReasonGlossary> reasonGlossary;
	private List<Document> documentsScanned;
	private Roleplay roleplay;
	//private List<User> allUsers;
	
	/**
	 * Selected Objects.
	 */
	private ActionGlossary selectedAction;
	private DecisionGlossary selectedDecision;
	private ReasonGlossary selectedReason;
	private User selectedUser;
	private ProcessStep processStep;
	private String newActionName;
	private String newDecisionName;
	private String newReasonName;
	private ActionGlossary newAction;
	private DecisionGlossary newDecision;
	private ReasonGlossary newReason;
	
	
	/**
	 * push fields.
	 */
	private String nowRfId;
    private String nowDocumentName;
    private boolean documentExists;


	private String key;
	
	/**
	 * selectFields.
	 */
	private String toSelect;
	private String toUnSelect;
	private List<Document> selectedDocuments;

	/**
	 * createNewDocument fields.
	 */
	private String newDocumentName;
	private boolean newDocument;
	
	/**
	 * default constructor.
	 */
	public WizardController() {
	}
	
	/**
	 * initializes attributes.
	 */
	@PostConstruct
	public void initialize() {
		this.dbs = new DbService();
		this.ags = new ActionGlossaryService();
		this.dgs = new DecisionGlossaryService();
		this.rgs = new ReasonGlossaryService();
		this.rps = new RoleplayService();
		bps = BpmnProcessingService.getInstance();
		
		this.selectedAction = new ActionGlossary();
		this.selectedDecision = new DecisionGlossary();
		this.selectedReason = new ReasonGlossary();
		this.roleplay = new Roleplay();
		this.selectedUser = new User();
		this.documentsScanned = new ArrayList<Document>();
		this.selectedDocuments = new ArrayList<Document>();
		
		/**
		 * init from DB
		 */
		this.actionGlossary = ags.getAllActionGlossarys();
		this.decisionGlossary = dgs.getAllDecisionGlossarys();
		this.reasonGlossary = rgs.getAllReasonGlossarys();		
		this.roleplay = rps.getAllRoleplays().get(0);
	}
	
	/**
	 * invoked when clicked on hinzufuegen.
	 * @return string
	 */
	public String formSubmit() {
		
		/**
		 * Complete the selectedClass Object by the submited object ID
		 */
		if (this.getRoleplay().getRolePlayStatus().equals(Constants.ROLEPLAY_PASSIVE)) {
			return "#";
		}
		this.completeAction(this.selectedAction);
		this.completeDecision(this.selectedDecision);
		this.completeReason(this.selectedReason);
				
		this.processStep = new ProcessStep();
		this.processStep.setUser(this.selectedUser);
		this.processStep.setName(this.roleplay.getRolePlayName());
		this.processStep.setRoundNo(BpmnProcessingService.getInstance().getRoundNr());
		
		if (this.selectedAction.getActionGlossaryID() != 0) {
			this.processStep.setAction(this.selectedAction);
		}
		if (this.selectedDecision.getDecisionGlossaryID() != 0) {
			this.processStep.setDecision(this.selectedDecision);
		}
		if (this.selectedReason.getReasonGlossaryID() != 0) {
			this.processStep.setReason(this.selectedReason);
		}
		this.processStep.setRoleplay(this.roleplay);
		this.processStep.setCreated(new Date());
		this.processStep.setUpdated(new Date());
		this.processStep.setDocSteps(new ArrayList<Document_ProcessStep>());
				
		/**
		 * add ProcessStep to BPMN-View.
		 */
		writeProcessStepToBPMN();
		
		/**
		 * save ProcessStep to DB.
		 */
		this.processStep = this.dbs.persistProcessStep(this.processStep);
		
		/**
		 * Add DocumentProcessstep for each document.
		 */
		for (Document document : this.selectedDocuments) {
			Document_ProcessStep docStep = new Document_ProcessStep();
			docStep.setDocument(document);
			docStep.setProcessStep(this.processStep);
			docStep.setCreated(new Date());
			docStep.setUpdated(new Date());
			dbs.persistDocStep(docStep);
			this.processStep.addDocStep(docStep);
		}
		this.resetValues();
		return "#";
	}
	
	/**
	 * resets local values after creating new ProcessStep.
	 */
	public void resetValues() {
		this.selectedAction = new ActionGlossary();
		this.selectedDecision = new DecisionGlossary();
		this.selectedReason = new ReasonGlossary();
		this.selectedDocuments = new ArrayList<Document>();
	}
	
	/**
	 * adds ProcessStep to BPMN-Diagram.
	 */
	public void writeProcessStepToBPMN() {
		bps.addElement(selectedAction.getName(), selectedDecision.getName(), selectedReason.getName(), this.selectedUser, this.selectedDocuments);
	}
	
	/**
	 * retrieves Data from Rest and pushs event to the client.
	 * @param lane Object with data for push
	 */
	public static void push(Lane lane) {
    	String id = lane.getId();

        TopicKey key = new TopicKey(id);
        TopicsContext topicsContext = TopicsContext.lookup();
        topicsContext.getOrCreateTopic(key);
        try {
        	topicsContext.publish(key, lane);
        } catch (MessageException e) {
            e.printStackTrace();
        }    	
    }
	
	/**
	 * Pushes to all Sessions.
	 * @param skey Key for push
	 */
	public static void pushAll(String skey) {
    	TopicKey key = new TopicKey(skey);
        TopicsContext topicsContext = TopicsContext.lookup();
        topicsContext.getOrCreateTopic(key);
        try {
        	topicsContext.publish(key, "message");
        } catch (MessageException e) {
            e.printStackTrace();
        }    	
    }
	
	/**
	 * gets the name of the recently scanned document.
	 * @param rfidid rfidid
	 * @return String documentname
	 */
	public static String getRecentDocumentName(String rfidid) {
    	DbService db = new DbService();
    	String stmt = String.format(Constants.DOCUMENT_WHERE_RFIDID, rfidid);
    	List<Document> found = db.getSthFromDb(stmt, Document.class);
    	
    	if (found.size() == 1) {
    		Document document = found.get(0);
    		return document.getName();
    	} else {
    		return Constants.DOCUMENT_NOT_FOUND;
    	}
    }
	
	/**
	 * gets the name of the recently scanned document.
	 * @param rfidid rfidid
	 * @return found document
	 */
	public static Document getRecentDocument(String rfidid) {
    	DbService db = new DbService();
    	String stmt = String.format(Constants.DOCUMENT_WHERE_RFIDID, rfidid);
    	List<Document> found = db.getSthFromDb(stmt, Document.class);
    	
    	if (found.size() == 1) {
    		Document document = found.get(0);
    		return document;
    	} else {
    		return null;
    	}
    }
	
	/**
	 * adds the new document to the scanned List.
	 * @return String url
	 */
	public String addRecentDocument() {
		List<Document> found = this.getRecentDocuments(this.nowRfId);
    	if (found.size() == 1) {
    		Document document = found.get(0);
    		this.documentsScanned.add(document);
    		this.documentExists = true;
    		return "#";
    	} else {
    		this.documentExists = false;
    		return null;
    	}
    }
	
	/**
	 * selects one document from the side and adds it to the selected list.
	 * @return String url
	 */
	public String addDocument() {
		List<Document> found = this.getRecentDocuments(this.toSelect);
		if (found.size() == 1) {
    		Document document = found.get(0);
    		this.selectedDocuments.add(document);
    		int position = -1;	
    		for (int i = 0; i < this.documentsScanned.size(); i++) {
    			if (this.documentsScanned.get(i).getRfidID().equals(this.toSelect)) {
    				position = i;
    			}
    		}
    		if (position != -1) {
    			this.documentsScanned.remove(position);
    		}
    		return "#";
    	} else {
    		return "#";
    	}
	}
	
	/**
	 * removes document from the selected-list.
	 * @return String url
	 */
	public String unSelectDocument() {
		Document document = new Document();
		int position = -1;	
		for (int i = 0; i < this.selectedDocuments.size(); i++) {
			if (this.selectedDocuments.get(i).getRfidID().equals(this.toUnSelect)) {
				position = i;
				document = this.selectedDocuments.get(i);
			}
		}
		if (position != -1) {
			this.selectedDocuments.remove(position);
			if (document != null) {
				this.documentsScanned.add(document);
			}
		}
		return "#";
	}
	
	/**
	 * gets document for given rfidid.
	 * @param rfidid rfidid
	 * @return list of all documents
	 */
	public List<Document> getRecentDocuments(String rfidid) {
		String stmt = String.format(Constants.DOCUMENT_WHERE_RFIDID, rfidid);
    	List<Document> found = this.dbs.getSthFromDb(stmt, Document.class);
    	return found;
	}
	
	/**
	 * creates new document.
	 * @return String url
	 */
	public String createNewDocument() {
		Document document = new Document();
		document.setCreated(new Date());
		document.setUpdated(new Date());
		document.setMailBoxID(this.selectedUser.getMailbox().getMailBoxID());
		document.setName(this.newDocumentName);
		document.setRfidID(this.nowRfId);
		this.dbs.createNewSth(document);
		this.documentsScanned.add(document);
		this.newDocument = false;
		this.newDocumentName = "";
		this.bin.addDocumentToRoleplay(document);
		return "#";
	}
	
	/**
	 * marks next scanned document as a new one.
	 * @return String url
	 */
	public String newToTrue() {
		this.setNewDocument(true);
		return "#";
	}
	
	/**
	 * next document isn't a new one.
	 * @return String url
	 */
	public String newToFalse() {
		this.setNewDocument(false);
		return "#";
	}
	
	/**
	 * adds recently scanned document to documentsScanned-List.
	 * @param document new Document
	 * @return String url
	 */
	@Deprecated
	public String newDocumentArrived(Document document) {
    	if (!this.documentExists) {
    		return "#";
    	}
    	this.documentsScanned.add(document);
    	this.documentExists = true;
    	this.nowDocumentName = "a";
    	this.nowRfId = "b";
    	return "#";
    }
	
	/**
	 * selects one user.
	 * @return String url
	 */
	public String selectUser() {
    	this.completeSelectedUser();
    	this.key = this.selectedUser.getMailbox().getPushkey();
    	try {
    		int position = -1;
	    	for (int j = 0; j < this.uLC.getAllUsers().size(); j++) {
	    		if (this.uLC.getAllUsers().get(j).getUserID() == this.selectedUser.getUserID()) {
	    			position = j;
	    		}
	    	}
	    	if (position > -1) {
	    		this.uLC.getAllUsers().remove(position);
	    		this.uLC.getSelectedUsers().add(this.selectedUser);
	    		WizardController.pushAll(Constants.USER_SELECT_TOPIC);
	    		SettingsController.pushAll(Constants.USER_SELECT_TOPIC);
	    	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	
    	/**
    	 * Create Pool in BPMN file
    	 */
		bps.createPool(this.roleplay.getRolePlayName());
		bps.addLane(this.selectedUser);
    	
    	return "#";
    }
	
	/**
	 * fetchs details for current User from db.
	 * @return current User
	 */
    public User completeSelectedUser() {
    	for (User user : this.uLC.getAllUsers()) {			
			if (selectedUser.getUserID() == user.getUserID()) {
				selectedUser = user;
			}
		}
    	return this.selectedUser;
    }
	
    /**
	 * get Action Id from Object and complete the object.
     * @param action new action
     */
	public void completeAction(ActionGlossary action) {
		for (int i = 0; i < actionGlossary.size(); i++) {			
			if (action.getActionGlossaryID().equals(actionGlossary.get(i).getActionGlossaryID())) {
				action.setName(actionGlossary.get(i).getName());
				action.setCreated(actionGlossary.get(i).getCreated());
				action.setUpdated(actionGlossary.get(i).getUpdated());
			}
		}
	}
	
	/**
	 * get Decision Id from Object and complete the object.
	 * @param decision decision
	 */
	public void completeDecision(DecisionGlossary decision) {
		for (int i = 0; i < decisionGlossary.size(); i++) {
			if (decision.getDecisionGlossaryID().equals(decisionGlossary.get(i).getDecisionGlossaryID())) {
				decision.setName(decisionGlossary.get(i).getName());
				decision.setCreated(decisionGlossary.get(i).getCreated());
				decision.setUpdated(decisionGlossary.get(i).getUpdated());
			}
		}
	}
	
	/**
	 * get Reason Id from Object and complete the object.
	 * @param reason reason
	 */
	public void completeReason(ReasonGlossary reason) {
		for (int i = 0; i < reasonGlossary.size(); i++) {
			if (reason.getReasonGlossaryID().equals(reasonGlossary.get(i).getReasonGlossaryID())) {
				reason.setName(reasonGlossary.get(i).getName());
				reason.setCreated(reasonGlossary.get(i).getCreated());
				reason.setUpdated(reasonGlossary.get(i).getUpdated());
			}
		}
	}
	
	/**
	 * Takes the actionName String and completes the object.
	 * @return String url
	 */
	public String createNewAction() {
		if (!this.newActionName.isEmpty()) {
			this.newAction = new ActionGlossary(this.newActionName);
			dbs.createNewSth(this.newAction);
			this.actionGlossary = ags.getAllActionGlossarys();
		}
		return "#";
	}
	
	/**
	 * Takes the decisionName String and completes the object.
	 * @return String url
	 */
	public String createNewDecision() {
		if (!this.newDecisionName.isEmpty()) {
			this.newDecision = new DecisionGlossary(this.newDecisionName);
			dbs.createNewSth(this.newDecision);
			this.decisionGlossary = dgs.getAllDecisionGlossarys();
		}
		return "#";
	}
	
	/**
	 * Takes the ReasonName String and completes the object.
	 * @return String url
	 */
	public String createNewReason() {
		if (!this.newReasonName.isEmpty()) {
			this.newReason = new ReasonGlossary(this.newReasonName);
			dbs.createNewSth(this.newReason);
			this.reasonGlossary = rgs.getAllReasonGlossarys();
		}
		return "#";
	}
	
	/**
	 * checks wether user is empty.
	 * @return true or false
	 */
	public Boolean getUserEmpty() {
		if (this.selectedUser.getRole().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getStatusTopic() {
		return Constants.STATUS_TOPIC;
	}
	
	public String getUserTopic() {
		return Constants.USER_SELECT_TOPIC;
	}
	
	public List<Document> getAllDocuments() {
		return this.dbs.getSthFromDb(Constants.ALL_FROM_DOCUMENT, Document.class);
	}

	public List<ActionGlossary> getActionGlossary() {
		return actionGlossary;
	}

	public void setActionGlossary(List<ActionGlossary> actionGlossary) {
		this.actionGlossary = actionGlossary;
	}

	public List<DecisionGlossary> getDecisionGlossary() {
		return decisionGlossary;
	}

	public void setDecisionGlossary(List<DecisionGlossary> decisionGlossary) {
		this.decisionGlossary = decisionGlossary;
	}

	public List<ReasonGlossary> getReasonGlossary() {
		return reasonGlossary;
	}

	public void setReasonGlossary(List<ReasonGlossary> reasonGlossary) {
		this.reasonGlossary = reasonGlossary;
	}

	public ActionGlossary getSelectedAction() {
		return selectedAction;
	}

	public void setSelectedAction(ActionGlossary selectedAction) {
		this.selectedAction = selectedAction;
	}

	public DecisionGlossary getSelectedDecision() {
		return selectedDecision;
	}

	public void setSelectedDecision(DecisionGlossary selectedDecision) {
		this.selectedDecision = selectedDecision;
	}

	public ReasonGlossary getSelectedReason() {
		return selectedReason;
	}

	public void setSelectedReason(ReasonGlossary selectedReason) {
		this.selectedReason = selectedReason;
	}

	public Roleplay getRoleplay() {
		return rps.getAllRoleplays().get(0);
	}

	public void setRoleplay(Roleplay roleplay) {
		this.roleplay = roleplay;
	}

	public ProcessStep getProcessStep() {
		return processStep;
	}

	public void setProcessStep(ProcessStep processStep) {
		this.processStep = processStep;
	}

	public String getNowRfId() {
		return nowRfId;
	}

	public void setNowRfId(String nowRfId) {
		this.nowRfId = nowRfId;
	}

	public String getNowDocumentName() {
		return nowDocumentName;
	}

	public void setNowDocumentName(String nowDocumentName) {
		this.nowDocumentName = nowDocumentName;
	}

	public boolean isDocumentExists() {
		return documentExists;
	}

	public void setDocumentExists(boolean documentExists) {
		this.documentExists = documentExists;
	}
	
	public List<Document> getDocumentsScanned() {
		return this.documentsScanned;
	}
	
	public void setDocumentsScanned(List<Document> documentsScanned) {
		this.documentsScanned = documentsScanned;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	/**
	 * all users from uLC.
	 * @return List<User>
	 */
	public List<User> getAllUsers() {
		return this.uLC.getAllUsers();
	}
	
	/**
	 * sets ulC.AllUsers.
	 * @param allUsers List of all users
	 */
	public void setAllUsers(List<User> allUsers) {
		this.uLC.setAllUsers(allUsers);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getToSelect() {
		return toSelect;
	}

	public void setToSelect(String toSelect) {
		this.toSelect = toSelect;
	}

	public List<Document> getSelectedDocuments() {
		return selectedDocuments;
	}

	public void setSelectedDocuments(List<Document> selectedDocuments) {
		this.selectedDocuments = selectedDocuments;
	}

	public String getToUnSelect() {
		return toUnSelect;
	}

	public void setToUnSelect(String toUnSelect) {
		this.toUnSelect = toUnSelect;
	}

	public String getNewActionName() {
		return newActionName;
	}

	public void setNewActionName(String newActionName) {
		this.newActionName = newActionName;
	}

	public ActionGlossary getNewAction() {
		return newAction;
	}

	public void setNewAction(ActionGlossary newAction) {
		this.newAction = newAction;
	}

	public String getNewDecisionName() {
		return newDecisionName;
	}

	public void setNewDecisionName(String newDecisionName) {
		this.newDecisionName = newDecisionName;
	}

	public String getNewReasonName() {
		return newReasonName;
	}

	public void setNewReasonName(String newReasonName) {
		this.newReasonName = newReasonName;
	}

	public DecisionGlossary getNewDecision() {
		return newDecision;
	}

	public void setNewDecision(DecisionGlossary newDecision) {
		this.newDecision = newDecision;
	}

	public ReasonGlossary getNewReason() {
		return newReason;
	}

	public void setNewReason(ReasonGlossary newReason) {
		this.newReason = newReason;
	}

	public boolean isNewDocument() {
		return newDocument;
	}

	public void setNewDocument(boolean newDocument) {
		this.newDocument = newDocument;
	}

	public String getNewDocumentName() {
		return newDocumentName;
	}

	public void setNewDocumentName(String newDocumentName) {
		this.newDocumentName = newDocumentName;
	}
}
