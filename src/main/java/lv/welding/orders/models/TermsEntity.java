package lv.welding.orders.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lv.welding.orders.models.base.BaseEntity;

@Entity
@Table(name="terms")
public class TermsEntity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String terms;
	@Column(name="[desc]")
	private String desc;

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
