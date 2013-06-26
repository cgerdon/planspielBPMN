package main.service.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import main.domain.bpmn.User;
import main.service.db.DbService;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

	/**
	 * Reads the User.XML and persists its values in the database.
	 * @author Felix
	 */
public class XMLUserReader {
	private static Logger log = Logger.getLogger(XMLUserReader.class);
	/**
	 * Variable of the type user.
	 */
	private User user;
	
	/**
	 * Variable of the type ServletContext - used to find the Webapp folder.
	 */
	private static ServletContext servletContext;

	/**
	 * Calls the method to read the XML and writes the results into the database.
	 * @return "#"
	 */
	public String setXML() {
		log.info("setXML gerufen");
		try {
		
		List<String> roles = readXML();
		DbService db;
		db = new DbService();
		
		
		for (int i = 0; i < roles.size(); i = i + 2) {
			String name = roles.get(i);
			int mailboxid = Integer.parseInt(roles.get(i + 1));
			user = new User(name, mailboxid);
			db.createNewSth(user);
			
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "#";
	}
	/**
	 * Reads the XML file and persists the objects in the table user.
	 * @return List<String> user
	 */
	public List<String> readXML() {
	
		try {
			//initiates Servlet
			servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	
			// Gets Path of Webapp Folder
			final String rootPath = servletContext.getRealPath("/settings");
			List<String> roles = new ArrayList<String>();
		
			NodeList textFNList = null;
			DocumentBuilderFactory docBuilderFactory =
					DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder =
					docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(rootPath + "/User.xml"));
	
			//normalizes the document
			doc.getDocumentElement().normalize();
	
			//Builds a NodeList of all sets
			NodeList listOfSets = doc.getElementsByTagName("Lane");
			int totalSets = listOfSets.getLength();
			//Writes out the number of sets
			log.info("Anzahl Planspiel Sets: " + totalSets);
			//run through all sets
			for (int i = 0; i < listOfSets.getLength(); i++) {
				Node firstSettingNode = listOfSets.item(i);
	
				if (firstSettingNode.getNodeType() == Node.ELEMENT_NODE) {
	
					Element firstSettingElement = (Element) firstSettingNode;
	
					NodeList firstProcessNameList = firstSettingElement.getElementsByTagName("Name");
					Element firstNameElement = (Element) firstProcessNameList.item(0);
					textFNList = firstNameElement.getChildNodes();
					log.info("User: "
					+ textFNList.item(0).getNodeValue().trim());
					//add the Object to the list
					roles.add(textFNList.item(0).getNodeValue().trim());
					
					NodeList lastCountList = firstSettingElement.getElementsByTagName("Mailboxid");
					Element lastNameElement = (Element) lastCountList.item(0);
					NodeList textLNList = lastNameElement.getChildNodes();
					log.info("Mailboxid: " 
					+ textLNList.item(0).getNodeValue().trim());
					//add the Object to the list
					roles.add(textLNList.item(0).getNodeValue().trim());
				}			
			}
			return roles; 
		} catch (SAXParseException err) {
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
	//			((x == null) ? e : x).printStackTrace();
			if (x == null) {
				e.printStackTrace();
				return error;
			} else { x.printStackTrace(); };
			return error;
		} catch (Throwable t) {
			List<String> error = new ArrayList<String>();
			error.add("error");
			t.printStackTrace();
			return error; 
		}
	}
}

