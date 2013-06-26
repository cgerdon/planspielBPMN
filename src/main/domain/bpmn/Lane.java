package main.domain.bpmn;

import java.io.Serializable;
/**
 *
 * @author christianroser
 *
 */
public class Lane implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8902806983161063026L;

	/**
	 *
	 */
	private String id;
	private String rfidid;
	private String name;
	
	/**
	 * Defaultconstructor, needed for Entity-Bean
	 */
	public Lane() {

	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRfidid() {
		return this.rfidid;
	}
	public void setRfidid(String name) {
		this.rfidid = name;
	}

}
