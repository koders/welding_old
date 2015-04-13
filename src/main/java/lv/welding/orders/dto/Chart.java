package lv.welding.orders.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import lv.welding.orders.models.InvoiceEntity;
import lv.welding.orders.models.Product;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

public class Chart implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final double EurNokRate = 8.08486414;
	private CartesianChartModel sales;
	private CartesianChartModel salesmen;
	private CartesianChartModel products;
	private CartesianChartModel sold;
	private Date soldFrom;
	private Date soldTo;
	private String bestMonth;
	private String bestSalesman;
	private String bestProduct;
	private InvoiceDTO invoiceService;
	private List<InvoiceEntity> invoices; 

	public CartesianChartModel getSales() {
		getInvoices();
		sales = new CartesianChartModel();
		Map<String, Double> invoiceResultsEur = new TreeMap<String, Double>();
		Map<String, Double> invoiceResultsNok = new TreeMap<String, Double>();
		Double best = 0.0;
		Double amount;
		
		for(Integer i=1; i<=12; i++) {
			String s = "";
			if(i<10)
				s = "0" + i.toString() + ".2013";
			else
				s = i.toString() + ".2013";
			invoiceResultsEur.put(s, 0.0);
			invoiceResultsNok.put(s, 0.0);
		}
		
		for(InvoiceEntity invoice: invoices) {
			try {
				String month = invoice.getDeliveryDate().substring(3);
				if(invoice.getCurrency().toLowerCase().equals("eur")) {
					Double current = invoiceResultsEur.get(month);
					if(current == null)
						current = 0.0;
					invoiceResultsEur.put(month, current + invoice.getTotalAmount());
				}
				if(invoice.getCurrency().toLowerCase().equals("nok")) {
					Double current = invoiceResultsNok.get(month);
					if(current == null)
						current = 0.0;
					invoiceResultsNok.put(month, current + invoice.getTotalAmount());
				}
				amount = invoiceResultsNok.get(month);
				if(amount == 0.0)
					amount = invoiceResultsEur.get(month);
				else
					amount = invoiceResultsEur.get(month) + invoiceResultsNok.get(month)/EurNokRate;
				if(best < amount) {
					best = amount;
					bestMonth = month;
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("EUR");
		Iterator iter2 = invoiceResultsEur.entrySet().iterator();
		while (iter2.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter2.next();
			series1.set(mEntry.getKey().toString(),(Double)mEntry.getValue());
		}
		
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("NOK");
        Iterator iter = invoiceResultsNok.entrySet().iterator();
        while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			series2.set(mEntry.getKey().toString(),(Double)mEntry.getValue());
		}
        
        sales.addSeries(series1);
        sales.addSeries(series2);        
        
		return sales;
	}

	public CartesianChartModel getSalesmen() {
		getInvoices();
		salesmen = new CartesianChartModel();
		Map<String, Double> invoiceResultsEur = new TreeMap<String, Double>();
		Map<String, Double> invoiceResultsNok = new TreeMap<String, Double>();
		Double best = 0.0;
		Double amount;
		
		for(InvoiceEntity invoice: invoices) {
			try {
				String ref = invoice.getRef();
				if(invoice.getCurrency().toLowerCase().equals("eur")) {
					Double current = invoiceResultsEur.get(ref);
					if(current == null)
						current = 0.0;
					invoiceResultsEur.put(ref, current + invoice.getTotalAmount());
					if(invoiceResultsNok.get(ref) == null)
						invoiceResultsNok.put(ref, 0.0);
				}
				if(invoice.getCurrency().toLowerCase().equals("nok")) {
					Double current = invoiceResultsNok.get(ref);
					if(current == null)
						current = 0.0;
					invoiceResultsNok.put(ref, current + invoice.getTotalAmount());
					if(invoiceResultsEur.get(ref) == null)
						invoiceResultsEur.put(ref, 0.0);
				}
				amount = invoiceResultsNok.get(ref);
				if(amount == null)
					amount = 0.0;
				if(amount == 0.0)
					amount = invoiceResultsEur.get(ref);
				else
					amount = (invoiceResultsEur.get(ref) == null ? 0.0 : invoiceResultsEur.get(ref))  + invoiceResultsNok.get(ref)/EurNokRate;
				if(best < amount) {
					best = amount;
					bestSalesman= ref;
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("EUR");
		Iterator iter2 = invoiceResultsEur.entrySet().iterator();
		while (iter2.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter2.next();
			series1.set(mEntry.getKey().toString(),(Double)mEntry.getValue());
		}
		
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("NOK");
        Iterator iter = invoiceResultsNok.entrySet().iterator();
        while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			series2.set(mEntry.getKey().toString(),(Double)mEntry.getValue());
		}
        
        salesmen.addSeries(series1);
        salesmen.addSeries(series2);        
        
		
		return salesmen;
	}

    public String getSalesmen1() {
        HashSet<String> refs = new HashSet<String>();
        String result = "'";
        for(InvoiceEntity invoice: invoices) {
            if(!refs.contains(invoice.getRef()))
                refs.add(invoice.getRef());
        }
        for(String ref: refs) {
            result += "'" + ref + "',";
        }
        result = result.substring(0, result.length()-1);
        result += "'";
        result = "test";
        return result;
    }

	public CartesianChartModel getProducts() {
		getInvoices();
		products = new CartesianChartModel();
		Map<String, Double> invoiceResultsEur = new TreeMap<String, Double>();
		Map<String, Double> invoiceResultsNok = new TreeMap<String, Double>();
		Double best = 0.0;
		Double amount;
		
		for(InvoiceEntity invoice: invoices) {
			for(Product p:invoice.getProductData()) {
				try {
					String product = p.getPno();
					if(product == null)
						product = "";
					if(invoice.getCurrency().toLowerCase().equals("eur")) {
						Double current = invoiceResultsEur.get(product);
						if(current == null)
							current = 0.0;
						if(p.getTprice() == null)
							p.setTprice("0.0");
						invoiceResultsEur.put(product, current + Double.parseDouble(p.getTprice()));
						if(invoiceResultsNok.get(product) == null)
							invoiceResultsNok.put(product, 0.0);
					}
					if(invoice.getCurrency().toLowerCase().equals("nok")) {
						Double current = invoiceResultsNok.get(product);
						if(current == null)
							current = 0.0;
						if(p.getTprice() == null)
							p.setTprice("0.0");
						invoiceResultsNok.put(product, current + Double.parseDouble(p.getTprice()));
						if(invoiceResultsEur.get(product) == null)
							invoiceResultsEur.put(product, 0.0);
					}
					amount = invoiceResultsNok.get(product);
					if(amount == null)
						amount = 0.0;
					if(amount == 0.0)
						amount = invoiceResultsEur.get(product);
					else
						amount = (invoiceResultsEur.get(product) == null ? 0.0 : invoiceResultsEur.get(product))  + invoiceResultsNok.get(product)/EurNokRate;
					if(best < amount) {
						best = amount;
						bestProduct= product;
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		invoiceResultsEur = sortMap(invoiceResultsEur);
		invoiceResultsEur = top10(invoiceResultsEur);
		invoiceResultsNok = sortMap(invoiceResultsNok);
		invoiceResultsNok = top10(invoiceResultsNok);
		
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("EUR");
		Iterator iter2 = invoiceResultsEur.entrySet().iterator();
		while (iter2.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter2.next();
			series1.set(mEntry.getKey().toString(),(Double)mEntry.getValue());
		}
		
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("NOK");
        Iterator iter = invoiceResultsNok.entrySet().iterator();
        while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			series2.set(mEntry.getKey().toString(),(Double)mEntry.getValue());
		}
        
        products.addSeries(series1);
        products.addSeries(series2);        
        
		
		return products;
	}
	
	
	@SuppressWarnings("deprecation")
	public CartesianChartModel getSold() {
		getInvoices();
		sold = new CartesianChartModel();
		Map<String, Integer> results = new TreeMap<String, Integer>();
		
		if(soldFrom == null)
			soldFrom = new Date();
		
		if(soldTo == null)
			soldTo = new Date();

		int year1 = soldFrom.getYear() + 1900;
		int year2 = soldTo.getYear() + 1900;
		
		int month1 = soldFrom.getMonth() + 1;
		int month2 = soldTo.getMonth() + 1;
		
		for(int i=year1; i<=year2; i++) {
			
		if(i == year1 && i == year2)
				for(Integer j=month1; j<=month2; j++) {
					String s = "";
					if(j<10)
						s = "0" + j.toString() + "." + i;
					else
						s = j + "." + i;
					results.put(s, 0);
					results.put(s, 0);
				}
			
			if(i == year1 && i != year2)
			for(Integer j=month1; j<=12; j++) {
				String s = "";
				if(j<10)
					s = "0" + j.toString() + "." + i;
				else
					s = j + "." + i;
				results.put(s, 0);
				results.put(s, 0);
			}
			
			if(i != year1 && i != year2)
				for(Integer j=1; j<=12; j++) {
					String s = "";
					if(j<10)
						s = "0" + j.toString() + "." + i;
					else
						s = j + "." + i;
					results.put(s, 0);
					results.put(s, 0);
				}
			
			if(i != year1 && i == year2)
				for(Integer j=1; j<=month2; j++) {
					String s = "";
					if(j<10)
						s = "0" + j.toString() + "." + i;
					else
						s = j + "." + i;
					results.put(s, 0);
					results.put(s, 0);
				}
		}
		
		Date t;
		
		for(InvoiceEntity invoice: invoices) {
			try {
				t = new SimpleDateFormat("dd.MM.yyyy").parse(invoice.getDeliveryDate());
				if(t.compareTo(soldFrom) < 0 || t.compareTo(soldTo) == 1)
					continue;
				String month = invoice.getDeliveryDate().substring(3);
				if(invoice.getCurrency().toLowerCase().equals("eur")) {
					Integer current = results.get(month);
					if(current == null)
						current = 0;
					for(Product p:invoice.getProductData()) {
						current += Integer.parseInt(p.getPcs());
					}
					results.put(month, current);
				}
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("EUR");
		Iterator iter = results.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			series1.set(mEntry.getKey().toString(),(Integer)mEntry.getValue());
		}
        
        sold.addSeries(series1);
        
		return sold;
	}
	
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map sortMap(Map unsortedMap) {
		List<Object> list = new LinkedList<Object>(unsortedMap.entrySet());
		
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
                                       .compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		
		Map<Object, Object> sortedMap = new TreeMap<Object, Object>();
		for (Iterator<Object> it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public Map top10(Map map) {
		Iterator iter = map.entrySet().iterator();
        while (map.size() > 10) {
			Map.Entry entry = (Map.Entry) iter.next();
			iter.remove();
		}
		return map;
	}
	
	
	
	
	
	public InvoiceDTO getInvoiceService() {
		return invoiceService;
	}
	
	public void setInvoiceService(InvoiceDTO invoiceService) {
		this.invoiceService = invoiceService;
	}

	public List<InvoiceEntity> getInvoices() {
		setInvoices(invoiceService.getInvoices());
		return invoices;
	}

	public void setInvoices(List<InvoiceEntity> invoices) {
		this.invoices = invoices;
	}

	public String getBestMonth() {
		return bestMonth;
	}

	public void setBestMonth(String bestMonth) {
		this.bestMonth = bestMonth;
	}

	public String getBestSalesman() {
		return bestSalesman;
	}

	public void setBestSalesman(String bestSalesman) {
		this.bestSalesman = bestSalesman;
	}

	public String getBestProduct() {
		return bestProduct;
	}

	public void setBestProduct(String bestProduct) {
		this.bestProduct = bestProduct;
	}

	public Date getSoldFrom() {
		return soldFrom;
	}

	public void setSoldFrom(Date soldFrom) {
		this.soldFrom = soldFrom;
	}

	public Date getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(Date soldTo) {
		this.soldTo = soldTo;
	}

}
