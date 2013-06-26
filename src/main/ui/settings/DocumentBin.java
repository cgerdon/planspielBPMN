package main.ui.settings;

import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import main.domain.bpmn.Document;
import main.service.bpmn.BpmnProcessingService;

/**
 * stores all Documents in circulation and all documents in trashbin.
 * @author christianroser
 *
 */
@Named
@ApplicationScoped
public class DocumentBin {
	private Hashtable<String, Document> documentsInRoleplay;
	private Hashtable<String, Document> documentsInBin;
	private static BpmnProcessingService service;
	
	/**
	 * initializes fields.
	 */
	@PostConstruct
	public void initialize() {
		this.documentsInRoleplay = new Hashtable<String, Document>();
		this.documentsInBin = new Hashtable<String, Document>();
	}
	
	/**
	 * checks, wether document is in roleplay.
	 * @param document document to check
	 * @return true or false if exists or not
	 */
	public Boolean documentExistsInRoleplay(Document document) {
		if (this.documentsInRoleplay.get(document.getRfidID()) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * checks, wether document is in bin.
	 * @param document for bin
	 * @return true or false if in bin or not
	 */
	public Boolean documentExistsInBin(Document document) {
		if (this.documentsInBin.get(document.getRfidID()) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * if document is not in roleplay, put it there.
	 * @param document to add to roleplay
	 * @return true or false
	 */
	public Boolean addDocumentToRoleplay(Document document) {
		if (!this.documentExistsInRoleplay(document)) {
			this.documentsInRoleplay.put(document.getRfidID(), document);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * if document is not in bin, put it there.
	 * @param document to add to bin
	 * @return true or false
	 */
	public Boolean addDocumentToBin(Document document) {
		if (!this.documentExistsInBin(document)) {
			this.documentsInBin.put(document.getRfidID(), document);
			if (this.documentsInBin.equals(this.documentsInRoleplay)) {
				service = BpmnProcessingService.getInstance();
				service.startNewRound();
				this.initialize();
			}
			return true;
		} else {
			if (this.documentsInBin.equals(this.documentsInRoleplay)) {
				service = BpmnProcessingService.getInstance();
				service.startNewRound();
				this.initialize();
			}
			return false;
		}
	}
	
	public Hashtable<String, Document> getDocumentsInRoleplay() {
		return documentsInRoleplay;
	}
	public void setDocumentsInRoleplay(Hashtable<String, Document> documentsInRoleplay) {
		this.documentsInRoleplay = documentsInRoleplay;
	}
	public Hashtable<String, Document> getDocumentsInBin() {
		return documentsInBin;
	}
	public void setDocumentsInBin(Hashtable<String, Document> documentsInBin) {
		this.documentsInBin = documentsInBin;
	}
}
