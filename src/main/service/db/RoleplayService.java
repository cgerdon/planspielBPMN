package main.service.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import main.domain.bpmn.Roleplay;
import main.util.Constants;
import main.util.EMF;
/**
 * Service Class for the table Roleplay
 * @author Christoph Gerdon
 */

public class RoleplayService {

	private EntityManagerFactory emf;

	private EntityManager em;

	public List<Roleplay> getAllRoleplays() {
//returns all Roleplays
		this.em = EMF.createEntityManager();

		List<Roleplay> contents = new ArrayList<Roleplay>();
		Query query = em.createQuery(Constants.ROLEPLAY_ALL);
		contents = query.getResultList();

		return contents;

	}

	public Roleplay getRoleplayById(int rolePlayID) {
//returns a single Roleplay
		this.em = EMF.createEntityManager();

		List<Roleplay> content = new ArrayList<Roleplay>();
		Query query = em.createQuery(Constants.ROLEPLAY_ID);
		query.setParameter("roleplayid", rolePlayID);
		content = query.getResultList();
		System.out.println(content.get(0));
		return content.get(0);

	}

	public boolean deleteRoleplayById(int rolePlayID) {
//deletes a single Roleplay
		try {

			this.em = EMF.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			Query query = em.createQuery(Constants.DELETE_ROLEPLAY);
			query.setParameter("roleplayid", rolePlayID);
			query.executeUpdate();
			tx.commit();
			return true;

		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean updateRoleplay(int rolePlayID, String rolePlayName,
			String rolePlayStatus, int rolePlayCurrentRoundNo) {
//updates a Roleplay
		try {

			this.em = EMF.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			Query query = em.createQuery(Constants.UPDATE_ROLEPLAY);
			query.setParameter("roleplayid", rolePlayID);
			query.setParameter("roleplayname", rolePlayName);
			query.setParameter("roleplaystatus", rolePlayStatus);
			query.setParameter("roleplaycurrentroundno", rolePlayCurrentRoundNo);
			query.setParameter("roleplayupdated", new Date());
			query.executeUpdate();
			tx.commit();
			return true;

		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

	}
}
