package main.service.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import main.domain.wizard.ProcessStep;
import main.util.Constants;
import main.util.EMF;
/**
 * Service Class for the table ProcessStep
 * @author Christoph Gerdon
 */

public class ProcessstepService {
	
	private EntityManagerFactory emf;
	
	private EntityManager em;
	
public List<ProcessStep> getAllProcessStep() {
//returns a list of ProcessSteps		
		this.em = EMF.createEntityManager();

		List<ProcessStep> contents = new ArrayList<ProcessStep>();
		Query query = em.createQuery(Constants.PROCESSSTEP_ALL);
		contents = query.getResultList();
		
		return contents;

	}
	
	public ProcessStep getProcessStepById(int processstepid) {
//returns a single ProcessStep		
		this.em = EMF.createEntityManager();
		
		List<ProcessStep> content = new  ArrayList<ProcessStep>();
		Query query = em.createQuery(Constants.PROCESSSTEP_ID);
		query.setParameter("processstepid", processstepid);
		content = query.getResultList();
		System.out.println(content.get(0));
		return content.get(0);

	}
	
	/**
	 * Returns all processsteps from a certain roleplay.
	 * @param roleplayId
	 * @return List of process-steps
	 */
	public List<ProcessStep> getProcessStepByRoleplayId(int roleplayId) {
		
		this.em = EMF.createEntityManager();
		
		List<ProcessStep> contents = new  ArrayList<ProcessStep>();
		Query query = em.createQuery(Constants.PROCESSSTEP_ROLEPLAYID);
		query.setParameter("roleplayid", roleplayId);
		contents = query.getResultList();
		
		return contents;

	}
	
	public boolean deleteProcessStepById(int processStepId) {
		
		
		try {
			
			this.em = EMF.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			Query query = em.createQuery(Constants.DELETE_PROCESSSTEP);
			query.setParameter("processstepid", processStepId);
			query.executeUpdate();
			tx.commit();
			return true;
			
		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

	}
}
