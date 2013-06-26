package main.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import main.domain.bpmn.Document;
import main.domain.bpmn.Lane;
import main.ui.settings.DocumentBin;
import main.ui.wizard.WizardController;
import main.util.Constants;


/**
 * Ressource for REST.
 * @author christianroser
 *
 */
@Named
@Path("/rest")
public class TestRessource {
	
	@Inject
	private DocumentBin bin;
		
	/**
	 * constructs TestRessource.
	 */
	public TestRessource() {
	}

	/**
	 * receives JSON-Data and calls push-Method.
	 * @param rolle object with jsonData
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public void receiveFromScanner(Lane rolle) {
		try {
			Document document = WizardController.getRecentDocument(rolle.getRfidid());
			if (rolle.getId().equals(Constants.TRASH_BIN)) {
	    		if (document != null) {
	    			bin.addDocumentToBin(document);
	    		}
	    	} else {
				if (document != null) {
					bin.addDocumentToRoleplay(document);
				}
			}
			rolle.setName(WizardController.getRecentDocumentName(rolle.getRfidid()));
			WizardController.push(rolle);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
