package lv.welding.orders.services.impl;

import java.util.ArrayList;
import java.util.List;

import lv.welding.orders.models.ProductForStatistics;
import lv.welding.orders.dto.InvoiceDTO;

public class StatisticsImpl {
	
	InvoiceDTO invoiceService;
	
	List<ProductForStatistics> mud = new ArrayList<ProductForStatistics>();
	List<ProductForStatistics> st37 = new ArrayList<ProductForStatistics>();
	List<ProductForStatistics> stainlessSteel = new ArrayList<ProductForStatistics>();
	List<ProductForStatistics> gw = new ArrayList<ProductForStatistics>();
	List<ProductForStatistics> waterTrap = new ArrayList<ProductForStatistics>();
	List<ProductForStatistics> mudBox = new ArrayList<ProductForStatistics>();

	public void initializeGroups() {
		mud.clear();
		st37.clear();
		stainlessSteel.clear();
		gw.clear();
		waterTrap.clear();
		mudBox.clear();
		
		ProductForStatistics p = new ProductForStatistics("ND 450",0,0);
		mudBox.add(p);
		p = new ProductForStatistics("ND 500",0,0);
		mudBox.add(p);
		
		p = new ProductForStatistics("ND 32",0,0);
		st37.add(p);
		stainlessSteel.add(p);
		p = new ProductForStatistics("ND 40",0,0);
		st37.add(p);
		stainlessSteel.add(p);
		p = new ProductForStatistics("ND 50",0,0);
		st37.add(p);
		stainlessSteel.add(p);
		p = new ProductForStatistics("ND 65",0,0);
		st37.add(p);
		stainlessSteel.add(p);
		p = new ProductForStatistics("ND 80",0,0);
		st37.add(p);
		stainlessSteel.add(p);
		
		p = new ProductForStatistics("ND 100",0,0);
		waterTrap.add(p);
		gw.add(p);
		mud.add(p);
		st37.add(p);
		stainlessSteel.add(p);
		p = new ProductForStatistics("ND 125",0,0);
		waterTrap.add(p);
		gw.add(p);
		mud.add(p);
		st37.add(p);
		stainlessSteel.add(p);
		p = new ProductForStatistics("ND 150",0,0);
		waterTrap.add(p);
		gw.add(p);
		mud.add(p);
		st37.add(p);
		stainlessSteel.add(p);
		
		p = new ProductForStatistics("MUD 150",0,0);
		stainlessSteel.add(p);
		
		p = new ProductForStatistics("ND 175",0,0);
		waterTrap.add(p);
		gw.add(p);
		mud.add(p);
		st37.add(p);
		p = new ProductForStatistics("ND 200",0,0);
		waterTrap.add(p);
		gw.add(p);
		mud.add(p);
		st37.add(p);
		stainlessSteel.add(p);
		p = new ProductForStatistics("ND 250",0,0);
		waterTrap.add(p);
		gw.add(p);
		mud.add(p);
		st37.add(p);
		stainlessSteel.add(p);
		p = new ProductForStatistics("ND 300",0,0);
		waterTrap.add(p);
		gw.add(p);
		mud.add(p);
		st37.add(p);
		stainlessSteel.add(p);
		
		p = new ProductForStatistics("ND 350",0,0);
		st37.add(p);
		stainlessSteel.add(p);
		p = new ProductForStatistics("ND 400",0,0);
		st37.add(p);
		stainlessSteel.add(p);
		
		p = new ProductForStatistics("ND 500",0,0);
		st37.add(p);
		
	}

	public InvoiceDTO getInvoiceService() {
		return invoiceService;
	}

	public void setInvoiceService(InvoiceDTO invoiceService) {
		this.invoiceService = invoiceService;
	}

	public List<ProductForStatistics> getMud() {
		initializeGroups();
		return mud;
	}

	public void setMud(List<ProductForStatistics> mud) {
		this.mud = mud;
	}

	public List<ProductForStatistics> getSt37() {
		return st37;
	}

	public void setSt37(List<ProductForStatistics> st37) {
		this.st37 = st37;
	}

	public List<ProductForStatistics> getStainlessSteel() {
		return stainlessSteel;
	}

	public void setStainlessSteel(List<ProductForStatistics> stainlessSteel) {
		this.stainlessSteel = stainlessSteel;
	}

	public List<ProductForStatistics> getGw() {
		return gw;
	}

	public void setGw(List<ProductForStatistics> gw) {
		this.gw = gw;
	}

	public List<ProductForStatistics> getWaterTrap() {
		return waterTrap;
	}

	public void setWaterTrap(List<ProductForStatistics> waterTrap) {
		this.waterTrap = waterTrap;
	}

	public List<ProductForStatistics> getMudBox() {
		return mudBox;
	}

	public void setMudBox(List<ProductForStatistics> mudBox) {
		this.mudBox = mudBox;
	}

	
} 
