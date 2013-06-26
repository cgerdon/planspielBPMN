package main.ui.settings;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

import main.domain.bpmn.Document;
import main.domain.bpmn.Roleplay;
import main.domain.bpmn.User;
import main.domain.wizard.Document_ProcessStep;
import main.domain.wizard.ProcessStep;
import main.service.Settings.XMLActionsReader;
import main.service.Settings.XMLDecisionsReader;
import main.service.Settings.XMLDocumentsReader;
import main.service.Settings.XMLMailboxReader;
import main.service.Settings.XMLReasonsReader;
import main.service.Settings.XMLRoleplayReader;
import main.service.Settings.XMLUserReader;
import main.service.bpmn.BpmnProcessingService;
import main.service.db.ProcessstepService;
import main.service.db.RoleplayService;
import main.ui.wizard.UserListController;
import main.ui.wizard.WizardController;
import main.util.Constants;

	/**
	 * @author Felix
	 * Controller class for the settings index.html.
	 */
	@Named("settings")
	@SessionScoped
	public class SettingsController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3361047851889823796L;
	private Logger log = Logger.getLogger(SettingsController.class);;
	/**
	 * Variable of the type UserListController.
	 */
	@Inject
	private UserListController uLC;
	/**
	 * Variable of the type XMLActionsReader.
	 */
	private XMLActionsReader actionsreader;
	/**
	 * Variable of the type XMLDocumentsReader.
	 */
	private XMLDocumentsReader documentsreader;
	/**
	 * Variable of the type XMLUserReader.
	 */
	private XMLUserReader userreader;
	/**
	 * Variable of the type XMLDecisionsReader.
	 */
	private XMLDecisionsReader decisionsreader;
	/**
	 * Variable of the type XMLReasonsReader.
	 */
	private XMLReasonsReader reasonsreader;
	/**
	 * Variable of the type XMLMailboxReader.
	 */
	private XMLMailboxReader mailboxreader;
	/**
	 * Variable of the type XMLRoleplayReader.
	 */
	private XMLRoleplayReader roleplayreader;
	/**
	 * Variable of the type BpmnProcessingService.
	 */
	private static BpmnProcessingService service;
	/**
	 * Variable of the type RoleplayService.
	 */
	private RoleplayService roleplay;
	/**
	 * For loading a certain roleplay.
	 */
	private int rolePlayId = -1;
	/**
	 * id of user to unselect.
	 */
	private int toUnSelect;
	
	/**
	 * unselects user.
	 * @return String url
	 */
	public String unSelectUser() {
		User user = uLC.getSelectedUserById(this.toUnSelect);
		try {
    		int position = -1;
	    	for (int j = 0; j < this.uLC.getSelectedUsers().size(); j++) {
	    		if (this.uLC.getSelectedUsers().get(j).equals(user)) {
	    			position = j;
//	    			user = this.uLC.getSelectedUsers().get(j);
	    		}
	    	}
	    	if (position > -1) {
	    		this.uLC.getSelectedUsers().remove(position);
	    		this.uLC.getAllUsers().add(user);
	    		WizardController.pushAll(Constants.USER_SELECT_TOPIC);
	    		SettingsController.pushAll(Constants.USER_SELECT_TOPIC);
	    	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
		
		return "#";
	}

	/**
	 * Calls the setXML for actions.
	 * @return actionsreader
	 * @throws IOException if xml not found or invalid
	 */
	public String setActionsXML() throws IOException {
		actionsreader = new XMLActionsReader();
		return actionsreader.setXML();
	}
	
	/**
	 * Calls the setXML for documents.
	 * @return documentsreader
	 * @throws IOException if xml not found or invalid
	 */
	public String setDocumentsXML() throws IOException {
		documentsreader = new XMLDocumentsReader();
		return documentsreader.setXML();
	}
	
	/**
	 * Calls setUserXML and setMailboxXML.
	 * @return String
	 * @throws IOException if xml not found or invalid
	 */
	public String setUserAndMailboxXML() throws IOException {
		setMailboxXML();
		setUserXML();
		/**
		 * reset userLists
		 */
		this.uLC.initialize();
		return "#";
	}
	
	/**
	 * Calls the setXML for User.
	 * @return userreader
	 * @throws IOException if xml not found or invalid
	 */
	public String setUserXML() throws IOException {
		userreader = new XMLUserReader();
		this.uLC.initialize();
		return userreader.setXML();
	}
	
	/**
	 * Calls the setXML for Decision.
	 * @return decisionsreader
	 * @throws IOException if xml not found or invalid
	 */
	public String setDecisionsXML() throws IOException {
		decisionsreader = new XMLDecisionsReader();
		return decisionsreader.setXML();
	}
	
	/**
	 * Calls the setXML for Reason.
	 * @return reasonsreader
	 * @throws IOException if xml not found or invalid
	 */
	public String setReasonsXML() throws IOException {
		reasonsreader = new XMLReasonsReader();
		return reasonsreader.setXML();
	}
	
	/**
	 * Calls the setXML for Mailbox.
	 * @return mailboxreader
	 * @throws IOException if xml not found or invalid
	 */
	public String setMailboxXML() throws IOException {
		mailboxreader = new XMLMailboxReader();
		return mailboxreader.setXML();
	}
	/**
	 * Calls the setXML for Roleplay.
	 * @return roleplayreader
	 * @throws IOException if xml not found or invalid
	 */
	public String setRoleplayXML() throws IOException {
		roleplayreader = new XMLRoleplayReader();
		return roleplayreader.setXML();
	}
	/**
	 * Calls BpmnProcessingService to start a new Round.
	 * @return roleplayreader
	 * @throws IOException if xml not found or invalid
	 */
	public String setNewRound() throws IOException {
		service = BpmnProcessingService.getInstance();
		service.startNewRound();
		return "#";
	}
	/**
	 * pushes to Settings.
	 * @param skey key for all clients
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
	 * Clears the BPMN output.
	 * @return String
	 */
	public String clearBpmn() {
		service = BpmnProcessingService.getInstance();
		service.clearBpmn();
		return "#";
	}
	/**
	 * gets selected Users.
	 * @return List of all selected users
	 */
	public List<User> getSelectedUsers() {
		return this.uLC.getSelectedUsers();
	}
	
	/**
	 * sets selected users.
	 * @param selectedUsers list with selected users
	 */
	public void setSelectedUsers(List<User> selectedUsers) {
		this.uLC.setSelectedUsers(selectedUsers);
	}
	
	/**
	 * gets User Topic.
	 * @return String usertopickey
	 */
	public String getUserTopic() {
		return Constants.USER_SELECT_TOPIC;
	}
	
	/**
	 * Loads the bpmn from the db.
	 * @return String url
	 */
	public String loadBpmn() {
		
		if (rolePlayId < 1) {
			return "";
		}
		
		ProcessstepService pS = new ProcessstepService();
		List<ProcessStep> steps = pS.getProcessStepByRoleplayId(rolePlayId);
		
		RoleplayService rS = new RoleplayService();
		Roleplay roleplay = rS.getRoleplayById(rolePlayId);
		
		service = BpmnProcessingService.getInstance();
		
		// clear the old bpmn
		service.clearBpmn();
		
		service.createPool(roleplay.getRolePlayName());
		
		int currentRoundNo = 0;
		// iterate through process steps
		for (ProcessStep step : steps) {
			
			// when new round.
			if (step.getRoundNo() > currentRoundNo) {
				service.startNewRound();
			}
			
			currentRoundNo = step.getRoundNo();
			
			List<Document> docs = new ArrayList<Document>();
			for (Document_ProcessStep dstep : step.getDocSteps()) {
				docs.add(dstep.getDocument());
			}
			
			String action = step.getAction() != null ? step.getAction().getName() : null;
			String decicion = step.getDecision() != null ? step.getDecision().getName() : null;
			String reason = step.getReason() != null ? step.getReason().getName() : null;
			
			service.addElement(action, 
					decicion, 
					reason, 
					step.getUser(), 
					docs);
			
			
		}
		
		return "#"; 
	}
	/**
	 * Calls the Method to change the roleplay status.
	 * @return actionsreader
	 * @throws IOException
	 */
	public String changeStatus() {
		log.info("changeStatus gerufen");
		roleplay = new RoleplayService();
		List<Roleplay> roleplays = roleplay.getAllRoleplays();
		String status = "";
		String name = "";
		int currroundno = 0;
		int id = 0;
		for (int i = 0; i < roleplays.size(); i++) {
			status = roleplays.get(i).getRolePlayStatus();
			id = roleplays.get(i).getRolePlayID();
			name = roleplays.get(i).getRolePlayName();
			currroundno = roleplays.get(i).getRolePlayCurrentRoundNo();
			log.info(status + " " + id + " " + name + " " + currroundno);
		}
		if (status.equals(Constants.ROLEPLAY_ACTIVE)) {
			log.info("Status ist Aktiv");
			roleplay.updateRoleplay(id, name, Constants.ROLEPLAY_PASSIVE, currroundno);
		} else {
			log.info("Status ist Pausiert");
			roleplay.updateRoleplay(id, name, Constants.ROLEPLAY_ACTIVE, currroundno);
		}
		WizardController.pushAll(Constants.STATUS_TOPIC);
		return "#";
	}

	public int getRolePlayId() {
		return rolePlayId;
	}

	public void setRolePlayId(int rolePlayId) {
		this.rolePlayId = rolePlayId;
	}

	public int getToUnSelect() {
		return toUnSelect;
	}

	public void setToUnSelect(int toUnSelect) {
		this.toUnSelect = toUnSelect;
	}
}
