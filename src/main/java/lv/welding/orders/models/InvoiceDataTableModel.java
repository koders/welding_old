package lv.welding.orders.models;

import java.util.ArrayList;
import java.util.List;


public class InvoiceDataTableModel {
	
	private Integer year;
	private Double totalEur = 0.0;
	private Double totalNok = 0.0;
	private List<InvoiceEntity> invoices = new ArrayList<InvoiceEntity>();
	
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public List<InvoiceEntity> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<InvoiceEntity> invoices) {
		this.invoices = invoices;
	}
	public Double getTotalEur() {
		return totalEur;
	}
	public void setTotalEur(Double totalEur) {
		this.totalEur = totalEur;
	}
	public Double getTotalNok() {
		return totalNok;
	}
	public void setTotalNok(Double totalNok) {
		this.totalNok = totalNok;
	}
	
}
