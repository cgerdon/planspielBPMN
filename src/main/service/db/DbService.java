package main.service.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import main.domain.bpmn.Document;
import main.domain.bpmn.Roleplay;
import main.domain.bpmn.User;
import main.domain.wizard.Document_ProcessStep;
import main.domain.wizard.ProcessStep;
import main.util.Constants;
import main.util.EMF;

/**
 * connects to Postgresql.
 * @author christianroser
 *
 */

public class DbService {

	/**
	 * Entitymanager for persisting Objects to DB.
	 */
	private EntityManager em;
	
	/**
	 * returns Result of given param stmt as a List with type of object clazz.
	 * @param stmt jpql statement
	 * @param clazz Class of return
	 * @param <T> generic datatype
	 * @return list of clazz
	 */
	public <T> List<T> getSthFromDb(String stmt, Class<T> clazz) {
		
		this.em = EMF.createEntityManager();

		List<T> contents = new ArrayList<T>();
		Query query = em.createQuery(stmt);
		contents = query.getResultList();
		
		return contents;
	}	

	/**
	 * starts new transaction and creates new entry in Lane.
	 * @param obj some Object
	 * @param <T> generic datatype
	 * @return List of obj
	 */
	public <T> String createNewSth(T obj) {
		try {
			
			this.em = EMF.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
				this.em.persist(obj);
			tx.commit();
			
		} catch (SecurityException | IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "#";

	}
	
	/**
	 * persists DocumentProcessStep.
	 * @param docStep docStep
	 * @return persisted docStep
	 */
	public Document_ProcessStep persistDocStep(Document_ProcessStep docStep) {
		this.em = EMF.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
			String stmt = String.format(Constants.DOCUMENT_WHERE_RFIDID, docStep.getDocument().getRfidID());
			Query docQuery = em.createQuery(stmt);
			Query stepQuery = em.createQuery(Constants.PROCESSSTEP_ID);
			stepQuery.setParameter("processstepid", docStep.getProcessStep().getProcessStepID());
			
			Document document = (Document) docQuery.getSingleResult();
			if (document != null) {
				docStep.setDocument(document);
			}
			ProcessStep step = (ProcessStep) stepQuery.getSingleResult();
			if (step != null) {
				docStep.setProcessStep(step);
			}
			em.persist(docStep);
		tx.commit();
		
		return docStep;
	}
	
	/**
	 * persists ProcessStep.
	 * @param processStep processStep
	 * @return persisted porcessStep
	 */
	public ProcessStep persistProcessStep(ProcessStep processStep) {
		Boolean ok = false;
		
		this.em = EMF.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
			String userStmt = String.format(Constants.USER_WHERE_ID, processStep.getUser().getUserID());
			Query userQuery = em.createQuery(userStmt);
			Query rolePlayQuery = em.createQuery(Constants.ROLEPLAY_ID);
			rolePlayQuery.setParameter("roleplayid", processStep.getRoleplay().getRolePlayID());
			User tempUser = (User) userQuery.getSingleResult();
			Roleplay tempRoleplay = (Roleplay) rolePlayQuery.getSingleResult();
			if (tempUser != null) {
				processStep.setUser(tempUser);
			}
			if (tempRoleplay != null) {
				processStep.setRoleplay(tempRoleplay);
			}
			
			em.persist(processStep);
		tx.commit();
		
		return processStep;
	}

}
