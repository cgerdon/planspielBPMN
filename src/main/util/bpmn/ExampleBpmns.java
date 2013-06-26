package main.util.bpmn;

import java.io.IOException;
import java.util.ArrayList;

import main.domain.bpmn.Document;
import main.domain.bpmn.User;
import main.service.bpmn.BpmnProcessingService;
import main.service.bpmn.models.Grid;

import org.eclipse.bpmn2.di.BPMNDiagram;

/**
 * 
 * @author Joerg This class can init some example bpmns
 * 
 */
public class ExampleBpmns {

	private static BpmnProcessingService bpmnService;

	/**
	 * process out of the input file.
	 */
	@SuppressWarnings("unused")
	private static org.eclipse.bpmn2.Process process;

	/**
	 * BpmnDiagram out of the input file.
	 */
	@SuppressWarnings("unused")
	private static BPMNDiagram bpmnDiagram;

	/**
	 * the gird.
	 */
	@SuppressWarnings("unused")
	private static Grid grid;

	public ExampleBpmns() throws IOException {

		bpmnService = BpmnProcessingService.getInstance();

		process = BpmnProcessingService.getProcess();
		bpmnDiagram = BpmnProcessingService.getBpmnDiagram();
		grid = BpmnProcessingService.getGrid();
	}

	@SuppressWarnings("serial")
	public final void mock() {

		User user1 = new User("Rechnungspruefer", 1);
		User user2 = new User("Chef", 2);

		final Document d1 = new Document("RE", "1", 1);
		
		bpmnService.addElement("Nimmt RE an", null, null, user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Prueft RE", "Bezahlt RE", "Stimmt mit AUF", user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Archiviert RE", null, null , user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.startNewRound();
		
		bpmnService.addElement("Nimmt RE an", null, null, user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Prueft RE", "Gibt RE an Chef", "Stimmt nicht mit AUF", user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Prueft RE", "Gibt RE frei", "RE war abgesprochen.", user2, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Bezahlt RE", null, null , user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Archiviert RE", null, null , user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.startNewRound();
		
		bpmnService.addElement("Nimmt RE an", null, null, user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Prueft RE", "Gibt RE an Chef", "Stimmt nicht mit AUF", user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Prueft RE", "Lehnt RE ab", "War nicht abgesprochen.", user2, new ArrayList<Document>() { { add(d1); } });
			
		bpmnService.addElement("Gibt RE mit Korrektur an Lieferant", null, null , user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.startNewRound();
	}
	
	@SuppressWarnings("serial")
	public final void simulateWizardInput() {
		bpmnService.createPool("TestPool");

		User user1 = new User("Sachbearbeiter", 1);
		User user2 = new User("Chef", 2);
		User user3 = new User("HR", 3);
		
		
		bpmnService.addLane(user1);
		bpmnService.addLane(user2);
		bpmnService.addLane(user3);
		
		final Document d1 = new Document("Bewerbung", "1", 1);

		final Document d2 = new Document("Bewerber Info", "2", 2);
		
		bpmnService.addElement("Bewerbung Entgegennehmen", null, null , user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Bewerbung Pruefen", "Bewerber Absagen", "Bewerber ist nicht qualifiziert" , user1, 
				new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.startNewRound();
				

		bpmnService.addElement("Bewerbung Entgegennehmen", null, null , user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Bewerbung Pruefen", "Bewerber-Info Erstellen", "Bewerber ist vielversprechend" , user1, 
				new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Bewerber-Info Pruefen", "Ok Geben", "Hat an der HSKA studiert" , user2, 
				new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Termin fuer Gespreach Koordinieren", null, null , user1, 
				new ArrayList<Document>() { { add(d1); add(d2); } });
		
		bpmnService.addElement("Termin Zusagen", null, null , user2, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Termin Zusagen", null, null , user3, new ArrayList<Document>() { { add(d2); } });
		
		bpmnService.addElement("Bewerber Einladen", null, null , user1, new ArrayList<Document>() { { add(d1); add(d2); } });
		
		bpmnService.startNewRound();
		
		
		bpmnService.addElement("Bewerbung Entgegennehmen", null, null , user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Bewerbung Pruefen", "Bewerber-Info Erstellen", "Bewerber ist vielversprechend" , user1, 
				new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Bewerber-Info Pruefen", "Bewerber Ablehnen", "Hat nicht an der HSKA studiert" , user2, 
				new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.addElement("Bewerber Absagen", null, null , user1, new ArrayList<Document>() { { add(d1); } });
		
		bpmnService.startNewRound();
	}
	
}
