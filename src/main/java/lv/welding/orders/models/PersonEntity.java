package lv.welding.orders.models;

import javax.persistence.Entity;
import javax.persistence.Table;

import lv.welding.orders.models.base.BaseEntity;

@Entity
@Table(name="persons")
public class PersonEntity extends BaseEntity{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
