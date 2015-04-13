package lv.welding.orders.models;

import javax.persistence.Entity;
import javax.persistence.Table;

import lv.welding.orders.models.base.BaseEntity;

@Entity
@Table(name="companies")
public class CompanyEntity extends BaseEntity{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String onr;
	private String address;
	private String properties;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOnr() {
		return onr;
	}
	public void setOnr(String onr) {
		this.onr = onr;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
