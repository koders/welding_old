package lv.welding.orders.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lv.welding.orders.models.base.BaseEntity;

@Entity
@Table(name="users")
public class UserEntity extends BaseEntity{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Column(name="un")
	private String username;
    @Column(name="psw")
	private String password;
    @Column(name="t")
	private Integer role;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	
	
}
