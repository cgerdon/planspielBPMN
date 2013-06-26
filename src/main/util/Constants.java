package main.util;

/**
 * All Constants belong to this class.
 * @author christianroser
 *
 */
public class Constants {
	/**
	 * Statements.
	 */
	public static final String HQL_TEST = "select l from Lane l";
	public static final String ROLEPLAY_ALL = "select r from Roleplay r";
	public static final String ROLEPLAY_ID = "select r from Roleplay r where r.rolePlayID = :roleplayid";
	public static final String DELETE_ROLEPLAY = "delete from Roleplay r where r.rolePlayID = :roleplayid";
	public static final String UPDATE_ROLEPLAY = "update Roleplay r SET r.rolePlayName = :roleplayname," 
			+ " r.rolePlayStatus = :roleplaystatus, r.rolePlayCurrentRoundNo = :roleplaycurrentroundno," 
			+ " r.rolePlayUpdated = :roleplayupdated where r.rolePlayID = :roleplayid";
	public static final String MAILBOX_ALL = "select m from Mailbox m";
	public static final String MAILBOX_ID = "select m from Mailbox m where m.mailBoxID = :mailBoxID";
	public static final String DELETE_MAILBOX = "delete from mailbox m where m.mailBoxID = :mailBoxID";
	public static final String ALL_FROM_USER = "select u from User u";
	public static final String ALL_FROM_DOCUMENT_WHERE = "select d from Document d where d.mailBoxID = %s";
	public static final String ALL_FROM_DOCUMENT = "select d from Document d";
	public static final String DOCUMENT_WHERE_RFIDID = "select d from Document d where d.rfidID = '%s'";
	public static final String ACTIONGLOSSARY_ALL = "select a from ActionGlossary a";
	public static final String ACTIONGLOSSARY_ID = "select a from ActionGlossary a where " 
			+ "a.actionGlossaryID = :actionGlossaryID";
	public static final String DELETE_ACTIONGLOSSARY = "delete from Actionglossary a where " 
			+ "a.actionGlossaryID = :actionGlossaryID";
	public static final String UPDATE_ACTIONGLOSSARY = "update ActionGlossary a SET a.name = :name, " 
			+ "a.updated = :actionglossaryupdated where a.actionGlossaryID = :actionGlossaryID";
	public static final String DECISIONGLOSSARY_ALL = "select a from DecisionGlossary a";
	public static final String DECISIONGLOSSARY_ID = "select a from DecisionGlossary a where " 
			+ "a.decisionGlossaryID = :decisionGlossaryID";
	public static final String DELETE_DECISIONGLOSSARY = "delete from DecisionGlossary a " 
			+ "where a.decisionGlossaryID = :decisionGlossaryID";
	public static final String UPDATE_DECISIONGLOSSARY = "update DecisionGlossary a SET a.name = :name, " 
			+ "a.updated = :decisionglossaryupdated where a.decisionGlossaryID = :decisionGlossaryID";
	public static final String REASONGLOSSARY_ALL = "select a from ReasonGlossary a";
	public static final String REASONGLOSSARY_ID = "select a from ReasonGlossary a where " 
			+ "a.reasonGlossaryID = :reasonGlossaryID";
	public static final String DELETE_REASONGLOSSARY = "delete from ReasonGlossary a where "
			+ "a.reasonGlossaryID = :reasonGlossaryID";
	public static final String UPDATE_REASONGLOSSARY = "update ReasonGlossary a SET a.name = :name, " 
			+ "a.updated = :reasonglossaryupdated where a.reasonGlossaryID = :reasonGlossaryID";
	public static final String PROCESSSTEP_ALL = "select p from ProcessStep p order by p.created";
	public static final String PROCESSSTEP_ID = "select p from ProcessStep p where p.processStepID = :processstepid";
	public static final String PROCESSSTEP_ROLEPLAYID = "select p from ProcessStep p where " 
			+ "p.roleplay.rolePlayID = :roleplayid";
	public static final String DELETE_PROCESSSTEP = "delete from ProcessStep p where p.processStepID = :processstepid";
	public static final String USER_WHERE_ID = "Select u from User u where u.userID = %d";
	
	/**
	 * errormsgs.
	 */
	public static final String DOCUMENT_NOT_FOUND = "Dokument nicht vorhanden";

	/**
	 * topickeys.
	 */
	public static final String STATUS_TOPIC = "st4tus";
	public static final String USER_SELECT_TOPIC = "us3r";

	/**
	 * roleplay status.
	 */
	public static final String ROLEPLAY_PASSIVE = "Pausiert";
	public static final String ROLEPLAY_ACTIVE = "Aktiv";
	
	/**
	 * trashbin.
	 */
	public static final String TRASH_BIN = "bin";
}
