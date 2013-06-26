package main.service.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import main.domain.wizard.ReasonGlossary;
import main.util.Constants;
import main.util.EMF;
/**
 * Service Class for the table ReasonGlossary
 * @author Christoph Gerdon
 */

public class ReasonGlossaryService {
	
	private EntityManagerFactory emf;
	
	private EntityManager em;
	
public List<ReasonGlossary> getAllReasonGlossarys() {
//returns all ReasonGlossary		
		this.em = EMF.createEntityManager();

		List<ReasonGlossary> contents = new ArrayList<ReasonGlossary>();
		Query query = em.createQuery(Constants.REASONGLOSSARY_ALL);
		contents = query.getResultList();
		
		return contents;

	}
	
	public ReasonGlossary getReasonGlossaryById(int ReasonGlossaryID) {
//returns a single ReasonGlossary		
		this.em = EMF.createEntityManager();
		
		List<ReasonGlossary> content = new  ArrayList<ReasonGlossary>();
		Query query = em.createQuery(Constants.REASONGLOSSARY_ID);
		query.setParameter("reasonGlossaryID", ReasonGlossaryID);
		content = query.getResultList();
		System.out.println(content.get(0));
		return content.get(0);

	}
	
	public boolean deleteReasonGlossaryById(int reasonGlossaryID) {
//deletes a single ReasonGlossary		
		try {
			System.out.println(reasonGlossaryID);
			this.em = EMF.createEntityManager();
			ReasonGlossary reasonglossary = em.find(ReasonGlossary.class, reasonGlossaryID);
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			em.remove(reasonglossary);
			tx.commit();
			return true;
			
		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean updateReasonGlossary(int reasonGlossaryID, String ReasonGlossaryName) {
//updates a ReasonGlossary
		try {
			
			this.em = EMF.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			Query query = em.createQuery(Constants.UPDATE_REASONGLOSSARY);
			query.setParameter("reasonGlossaryID", reasonGlossaryID);
			query.setParameter("name", ReasonGlossaryName);
			query.setParameter("reasonglossaryupdated", new Date());
			query.executeUpdate();
			tx.commit();
			return true;
			
		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

	}

}
