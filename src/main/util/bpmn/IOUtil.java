package main.util.bpmn;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 *
 * @author Joerg Hilscher
 * This class will help you to read and write BPMN-files from the "webapp"-folder
 * This class implements the singleton pattern.
 * 
 */
public class IOUtil {

	/**
	 * Relative path from the webapp-folder.
	 */
	private static String path;

	/**
	 * Filename of the input file.
	 */
	private static String sourceFileName;

	/**
	 * Filename of the output file.
	 */
	private static String destinationFileName;

	/**
	 * Disc.
	 */
	private static String disc = "/";
	
	/**
	 * Context.
	 */
	private static ServletContext servletContext;

	/**
	 * Instance of this class. Used for singleton implementation.
	 */
	private static IOUtil instance;

	/**
	 * InputStream to load the resource.
	 */
	private static InputStream inputStream;
	
	/**
	 * Saves and loads the resource.
	 * @param resource		The resource to save
	 * @throws IOException
	 */
	public void saveToBpmnFile(Resource resource) throws IOException {

		loadServletContext();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		resource.save(out, null); 

		// Gets Path of Webapp Folder
		final String rootPath = servletContext.getRealPath("/");

		FileOutputStream output = new FileOutputStream(rootPath + disc + path + destinationFileName);

		output.write(out.toByteArray());
		output.close();
	}

	/**
	 * Loads a .bpmn file.
	 * @return	the resource out of the file
	 * @throws IOException
	 */
	public Resource loadFromBpmnFile() throws IOException {

        URI uri = URI.createURI(path + destinationFileName);
        Resource resource = new Bpmn2ResourceFactoryImpl().createResource(uri);

        loadServletContext();

        inputStream = servletContext.getResourceAsStream(path + sourceFileName);

        // If Stream is null, then the file doesn't exists
        if (inputStream == null)
        	throw new FileNotFoundException();

        resource.load(inputStream, Collections.EMPTY_MAP);

        inputStream.close();

        return resource;
	}

	/**
	 * Returns the only instance of IOUtil.
	 * Sets the parameters.
	 * @param PATH
	 * @param sourceFileName
	 * @param destinationFileName
	 * @return instance of IOUtil
	 */
	private static IOUtil initIOUtil() {

		if (instance == null)
			instance = new IOUtil();

		path = BpmnConstants.PATH;
		sourceFileName = BpmnConstants.INPUT_FILE_NAME;
		destinationFileName = BpmnConstants.OUTPUT_FILE_NAME;

		return instance;
	}

	private static void loadServletContext () {
		
		if (IOUtil.servletContext != null)
			return;
		
		// Init servletContext: this context will get files from the webapp-folder
		IOUtil.servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	}
	
	/**
	 * GetInstance().
	 * @return the only IOUtil instance.
	 */
	public static IOUtil getInstance() {
		if (instance == null)
			initIOUtil();	
		return instance;
	}

	/**
	 * private Constructor.
	 */
	private IOUtil() {

	}

	public static String getSourceFileName() {
		return sourceFileName;
	}

	public static void setSourceFileName(String sourceFileName) {
		IOUtil.sourceFileName = sourceFileName;
	}

	public static String getDestinationFileName() {
		return destinationFileName;
	}

	public static void setDestinationFileName(String destinationFileName) {
		IOUtil.destinationFileName = destinationFileName;
	}

	public static InputStream getInputStream() {
		return inputStream;
	}

	public static void setInputStream(InputStream inputStream) {
		IOUtil.inputStream = inputStream;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		IOUtil.servletContext = servletContext;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		IOUtil.path = path;
	}

	public static String getDisc() {
		return disc;
	}

	public static void setDisc(String disc) {
		IOUtil.disc = disc;
	}

	
}
