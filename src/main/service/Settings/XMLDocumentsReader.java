package main.service.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import main.domain.bpmn.Document;
import main.service.db.DbService;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

	/**
	 * Reads the Documents.xml and persists its values in the database.
	 * @author Felix
	 */
public class XMLDocumentsReader {
	private static Logger log = Logger.getLogger(XMLDocumentsReader.class);
	/**
	 * Variable of the type Document.
	 */
	private main.domain.bpmn.Document document;
	/**
	 * Variable of the type ServletContext - used to find the Webapp folder.
	 */
	private static ServletContext servletContext;

/**
 * Calls the method to read the XML and writes the results into the database.
 * @return "#"
 */
	public String setXML() {
		List<String> documents = readXML();
		DbService db;
		document = new Document();
		db = new DbService();
		log.info("Dokument wie es in die DB geschrieben wird:");
		for (int i = 0; i < documents.size(); i = i + 2) {
			String rfidid = documents.get(i);
			String dokument = documents.get(i + 1);
			log.info("id: " + rfidid + " Dokument " + document);
			document = new Document(dokument, rfidid);
			db.createNewSth(document);
		}
	return "#";
	}
	/**
	 * Reads the XML file and persists the objects in the table document.
	 * @return List<String> documents
	 */
	public List<String> readXML() {
		try {
			//initiates Servlet
			servletContext = (ServletContext) 
					FacesContext.getCurrentInstance().getExternalContext().getContext();

			// Gets Path of Webapp Folder
			final String rootPath = servletContext.getRealPath("/settings");
			log.info(rootPath);

			List<String> roles = new ArrayList<String>();

			NodeList textFNList = null;
			DocumentBuilderFactory docBuilderFactory =
					DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder =
					docBuilderFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.parse(new File(rootPath + "/Documents.xml"));

			//normalizes the document
			doc.getDocumentElement().normalize();

			//Builds a NodeList of all sets
			NodeList listOfSets = doc.getElementsByTagName("Document");
			int totalSets = listOfSets.getLength();
			//Writes out the number of sets
			log.info("Anzahl Planspiel Sets: " + totalSets);
			//run through all sets
			for (int i = 0; i < listOfSets.getLength(); i++) {
				Node firstSettingNode = listOfSets.item(i);

				if (firstSettingNode.getNodeType() == Node.ELEMENT_NODE) {

					Element firstSettingElement = (Element) firstSettingNode;

					NodeList firstProcessNameList = firstSettingElement.getElementsByTagName("rfidid");
					Element firstNameElement = (Element) firstProcessNameList.item(0);

					textFNList = firstNameElement.getChildNodes();
					
					//add the Object to the list
					roles.add(textFNList.item(0).getNodeValue().trim());

					NodeList lastCountList = firstSettingElement.getElementsByTagName("Name");
					Element lastNameElement = (Element) lastCountList.item(0);

					NodeList textLNList = lastNameElement.getChildNodes();
					
					//add the Object to the list
					roles.add(textLNList.item(0).getNodeValue().trim());
				}
			}
			return roles; 
			}

		//Catches errors that occur in the XML
		catch (SAXParseException err) {
			List<String> error = new ArrayList<String>();
			error.add("error");
			log.error("** Parsing error" + ", line "
					+ err.getLineNumber()
					+ ", uri "
					+ err.getSystemId());
			log.error(" " + err.getMessage());
			return error;
		} catch (SAXException e) {
			List<String> error = new ArrayList<String>();
			error.add("error");
			Exception x = e.getException();
			if (x == null) {
				e.printStackTrace();
				return error;
			}
			else { x.printStackTrace(); 
			};
			return error;
		} catch (Throwable t) {
			List<String> error = new ArrayList<String>();
			error.add("error");
			t.printStackTrace();
			return error; 
			}
	}
}
