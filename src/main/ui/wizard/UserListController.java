package main.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import main.domain.bpmn.User;
import main.service.db.DbService;
import main.util.Constants;

/**
 * stores all users, selected and unselected.
 * @author christianroser
 *
 */
@Named
@ApplicationScoped
public class UserListController {
	private List<User> allUsers;
	private List<User> selectedUsers;
	private DbService dbs = new DbService();
	
	/**
	 * initializes userlists.
	 */
	@PostConstruct
	public void initialize() {
		this.allUsers = new ArrayList<User>();
		this.selectedUsers = new ArrayList<User>();
		for (User user : dbs.getSthFromDb(Constants.ALL_FROM_USER, User.class)) {
			this.allUsers.add(user);
		}
	}
	/**
	 * Returns the actual selected user.
	 * @param id to select
	 * @return User
	 */
	public User getSelectedUserById(int id) {
		for (User user : this.selectedUsers) {
			if (user.getUserID() == id) {
				return user;
			}
		}
		return null;
	}
	
	public List<User> getAllUsers() {
		return allUsers;
	}
	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}
	public List<User> getSelectedUsers() {
		return selectedUsers;
	}
	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
}
