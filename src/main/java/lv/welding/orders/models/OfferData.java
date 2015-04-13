package lv.welding.orders.models;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OfferData {
	
	private String company;
	private String OfferNumber;
	private String marking;
	private String odate;
	private String ddate;
	private String dcompany;
	private String daddres;
	private String dterms;
	private String currency;
	private String cperson;
	private String cphone;
	private String cfax;
	private String cemail;
	private String spec;
	private List<Product> p = new ArrayList<Product>();
	private String tot;
	private String spec2;
	
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getMarking() {
		return marking;
	}
	public void setMarking(String marking) {
		this.marking = marking;
	}
	public String getOdate() {
		return odate;
	}
	public void setOdate(String odate) {
		//Mon Jul 15 03:00:00 EEST 2013
		
		this.odate = odate;
	}
	public String getDdate() {
		return ddate;
	}
	public void setDdate(String ddate) {
		this.ddate = ddate;
	}
	public String getDaddres() {
		return daddres;
	}
	public void setDaddres(String daddres) {
		this.daddres = daddres;
	}
	public String getDterms() {
		return dterms;
	}
	public void setDterms(String dterms) {
		this.dterms = dterms;
	}
	public String getCurrency() {
		if(currency == null)
			currency = "EUR";
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCperson() {
		return cperson;
	}
	public void setCperson(String cperson) {
		this.cperson = cperson;
	}
	public String getCphone() {
		return cphone;
	}
	public void setCphone(String cphone) {
		this.cphone = cphone;
	}
	public String getCfax() {
		return cfax;
	}
	public void setCfax(String cfax) {
		this.cfax = cfax;
	}
	public String getCemail() {
		return cemail;
	}
	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public List<Product> getP() {
		return p;
	}
	public void setP(List<Product> p) {
		this.p = p;
	}
	public String getSpec2() {
		return spec2;
	}
	public void setSpec2(String spec2) {
		this.spec2 = spec2;
	}
	public String getTot() {
		return tot;
	}
	public void setTot(String tot) {
		this.tot = tot;
	}
	public String getDcompany() {
		return dcompany;
	}
	public void setDcompany(String dcompany) {
		this.dcompany = dcompany;
	}

}
