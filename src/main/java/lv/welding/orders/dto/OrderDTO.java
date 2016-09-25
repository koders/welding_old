package lv.welding.orders.dto;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import lv.welding.orders.dao.OrderDao;
import lv.welding.orders.dao.ProductDao;
import lv.welding.orders.models.CompanyEntity;
import lv.welding.orders.models.OrderData;
import lv.welding.orders.models.OrderEntity;
import lv.welding.orders.models.Product;
import lv.welding.orders.models.ProductEntity;
import lv.welding.orders.services.MainService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lv.welding.orders.services.StockService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	
	private OrderDao orderDao;
	private ProductDao productDao;
	private MainService service;
    private StockService stockService;
	
	private List<OrderEntity> allOrders = null;
	private List<OrderEntity> allOffers = null;
	private List<OrderEntity> filteredOrders = null;
    private List<OrderEntity> filteredOffers = null;
	private Long confirmationNumber;
	private Long offerNumber;
	private String find;
	private String findOffer;

	private CompanyEntity companySelected = new CompanyEntity();
	private CompanyEntity deliveryAddress = new CompanyEntity();
	private List<Product> products = new ArrayList<Product>();
	private OrderEntity currentOrder = new OrderEntity();
	private Date deliveryDate;
	private Date orderDate;
	private OrderEntity orderToDelete;
	
	private Double totalProductPrice = 0.0;
	private boolean edit;
    private boolean offer;
	
	
	
	//-------------------------- UTIL --------------------------------------
	
	
	public String convertSymbols(String s) {
		if(s == null)
			return null;
		s = s.replace("�", "\\u0100");
		s = s.replace("�", "\\u0101");
		s = s.replace("�", "\\u010C");
		s = s.replace("�", "\\u010D");
		s = s.replace("�", "\\u0112");
		s = s.replace("�", "\\u0113");
		s = s.replace("�", "\\u0122");
		s = s.replace("�", "\\u0123");
		s = s.replace("�", "\\u012A");
		s = s.replace("�", "\\u012B");
		s = s.replace("�", "\\u0136");
		s = s.replace("�", "\\u0137");
		s = s.replace("�", "\\u013B");
		s = s.replace("�", "\\u013C");
		s = s.replace("�", "\\u0145");
		s = s.replace("�", "\\u0146");
		s = s.replace("�", "\\u0160");
		s = s.replace("�", "\\u0161");
		s = s.replace("�", "\\u016A");
		s = s.replace("�", "\\u016B");
		s = s.replace("�", "\\u017D");
		s = s.replace("�", "\\u017E");
		return s;
	}
	
	public void redirectTo(String url) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/welding/" + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void msg(String message) {
		FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(message));
	}
	
	public void flashMsg(String message) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,message,null));
	}
	
	public void refreshOrders() {
		setAllOrders(null);
        if(find == null || find == "" || find.startsWith("all"))
            this.filteredOrders = getAllOrders();
	}

    public void refreshOffers() {
        setAllOffers(null);
        if(findOffer == null || findOffer == "" || findOffer.startsWith("all"))
            this.filteredOffers = getAllOffers();
    }
	
	//-------------------------- COMPANY --------------------------------------
	
	
	public List<String> companyComplete(String query) {
		
		List<CompanyEntity> allCompanies = orderDao.getCompanyList();
        List<String> filteredCompanies = new ArrayList<String>();
        
        for(CompanyEntity c: allCompanies) {
        	if(c.getName().toUpperCase().contains(query.toUpperCase()))
        		filteredCompanies.add(c.getName());
        }
          
        return filteredCompanies;
	}
	
	public void selectCompany() {
		companySelected = orderDao.getCompany(companySelected.getName());
		companySelected.setOnr(companySelected.getOnr());
	}
	
	
	
	//-------------------------- PRODUCTS --------------------------------------
	
	
	
	public void addProduct() {
		products.add(new Product());
	}
	
	public void removeProduct(Product product) {
		products.remove(product);
		editPrice();
	}
	
	public void checkProduct(Product p) {
		if(p == null || p.getPno() == null || p.getPno().equals("")) 
			return;
		
		ProductEntity p1 = productDao.getProduct(p.getPno());
		if(p1 != null)
			p.setDesc(p1.getDesc());
	}
	
	public void editPrice(){
		totalProductPrice = 0.0;
		List<Product> pr;
		pr = products;
		
		for(Product p: pr) {
			try {
				Double.parseDouble(p.getPrice());
				Double.parseDouble(p.getPcs());
			} catch (Exception e) {
				continue;
			}
			totalProductPrice += Double.parseDouble(p.getPrice()) * Double.parseDouble(p.getPcs());
		}
	}
	
	public BigDecimal calculatePrice(Product product) {
		if(product == null || product.getPcs() == null || product.getPrice() == null || product.getPcs().equals("") || product.getPrice().equals(""))
			return new BigDecimal(0.00);
		Double result = Double.parseDouble(product.getPcs()) * Double.parseDouble(product.getPrice());
		return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP);
	}

  public BigDecimal calculateStockPrice(Product product) {
    if(product == null || product.getPcs() == null || product.getStockPrice() == null || product.getPcs().equals("") || product.getStockPrice().equals(""))
      return new BigDecimal(0.00);
    Double result = Double.parseDouble(product.getPcs()) * Double.parseDouble(product.getStockPrice());
    return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP);
  }
	
	public void saveNewProducts() {
		for(Product p: products) {
			if(productDao.getProduct(p.getPno()) == null) {
				ProductEntity p1 = new ProductEntity();
				p1.setPno(p.getPno());
				p1.setDesc(p.getDesc());
				productDao.save(p1);
			}
		}
	}
	
	
	
	
	//-------------------------- ORDERS --------------------------------------
	
	
	
	
	public void save() {
		setCurrentOrderForSave();
		currentOrder.getOrderData().setSpec(convertSymbols(currentOrder.getOrderData().getSpec()));
		String data = gson.toJson(currentOrder.getOrderData());		
		data = data.replace("\\\\", "\\");
		currentOrder.setData(data);
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		currentOrder.setCreatedBy(user.getUsername());
		orderDao.save(currentOrder);
		service.nextNumber(companySelected);
		service.saveNewPerson(currentOrder.getOrderData().getCperson());
		saveNewProducts();
		refreshOrders();
		flashMsg(isOffer() ? "Offer successfully saved!" : "Order successfully saved!");
		//reset();
	}
	
	public void setCurrentOrderForSave() {
        currentOrder.setType(isOffer() ? 1 : 0);
		currentOrder.setCt(new Timestamp(System.currentTimeMillis()));
		currentOrder.setMt(currentOrder.getCt());
		currentOrder.getOrderData().setCompany(companySelected.getName());
		currentOrder.getOrderData().setOrdernr(companySelected.getOnr());
		currentOrder.getOrderData().setP(products);
		currentOrder.getOrderData().setOcnr(isOffer() ? offerNumber.toString() : confirmationNumber.toString());
		
		//setDatesForSave();
	}
	
	public void saveEdited() {
		setEditedOrderForSave();
		currentOrder.getOrderData().setSpec(convertSymbols(currentOrder.getOrderData().getSpec()));
		String data = gson.toJson(currentOrder.getOrderData());	
		data = data.replace("\\\\", "\\");
		currentOrder.setData(data);
		orderDao.update(currentOrder);
		//service.nextNumber(companySelected);
		service.saveNewPerson(currentOrder.getOrderData().getCperson());
		saveNewProducts();
		refreshOrders();
		//reset();	
		flashMsg(isOffer() ? "Offer updated!" : "Order updated!");
	}
	
	public void setEditedOrderForSave() {
		currentOrder.setMt(new Timestamp(System.currentTimeMillis()));
		currentOrder.getOrderData().setCompany(companySelected.getName());
		currentOrder.getOrderData().setOrdernr(companySelected.getOnr());
		currentOrder.getOrderData().setP(products);
		currentOrder.getOrderData().setOcnr(isOffer() ? offerNumber.toString() : confirmationNumber.toString());
		//setDatesForSave();
	}
	
	public void setOrderForEdit(OrderEntity order) {
		if(order == null) {
			currentOrder = new OrderEntity();
			setProducts(new ArrayList<Product>());
			companySelected = new CompanyEntity();
			return;
		}
		this.currentOrder = order;
		setProducts(currentOrder.getOrderData().getP());
		companySelected.setName(currentOrder.getOrderData().getCompany());
		companySelected.setOnr(currentOrder.getOrderData().getOrdernr());
		deliveryAddress = service.getCompanyByName(currentOrder.getOrderData().getDcompany());
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		try {
			if(currentOrder.getOrderData().getDdate() != null)
				deliveryDate = df.parse(currentOrder.getOrderData().getDdate());
			if(currentOrder.getOrderData().getOdate() != null)
			orderDate = df.parse(currentOrder.getOrderData().getOdate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		edit = true;
	}
	
	public void delete() {
		/*
		 * if(orderToDelete.getRole() == 0) {
			orderDao.delete(orderToDelete);
			msg("Order successfully deleted!");
		}
		else {
		*/
			orderToDelete.setT(3);
			orderDao.update(orderToDelete);
			msg("Order successfully truncated!");
		//}
		refreshOrders();
	}
	
	public void reset() {
		companySelected = new CompanyEntity();
		deliveryAddress = new CompanyEntity();
		products = new ArrayList<Product>();
		currentOrder = new OrderEntity();
	}
	
	public String style(OrderEntity order) {
		if(order == null) return "";
		Integer phase = order.getT();
		return "phase" + phase.toString();
	}

    public String styleOffer(OrderEntity order) {
        if(order == null) return "";
        Integer phase = order.getT() + 1;
        return "phase" + phase.toString();
    }
	
	public void nextPhase() {
        if(currentOrder.getT() == 0)
            editStock(products);
		currentOrder.setT(currentOrder.getT() + 1);
	}

    public void acceptOffer() {
        currentOrder.setT(currentOrder.getT() + 1);
    }

    private void editStock(List<Product> products) {
        for(ProductEntity pe: stockService.getProducts()) {
            for (Product p : products) {
                try {
                    if(p == null)
                        continue;
                    if(pe.getPno().equals(p.getPno())) {
                        Integer count = Integer.valueOf(p.getPcs());
                        pe.setAddToStock(-count);
                        pe.setTotalShipped(pe.getTotalShipped() + count);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        stockService.updateStock();
    }
	
	public void search() {
		if(find == null || find.equals("") || find.startsWith("all")) {
			setFilteredOrders(getAllOrders());
			return;
		}
		List<OrderEntity> ol = new ArrayList<OrderEntity>();
		find = find.toLowerCase();
		for(OrderEntity o: allOrders) {
			if(find.startsWith("new") && o.getT() == 0)
				ol.add(o);
			if(find.startsWith("in production") && o.getT() == 1)
				ol.add(o);
			if(find.startsWith("completed") && o.getT() == 2)
				ol.add(o);
			if(find.startsWith("deleted") && o.getT() == 3)
				ol.add(o);
			if(find.equals(o.getOrderData().getOcnr()))
				ol.add(o);
			if(o.getOrderData().getCompany() != null && o.getOrderData().getCompany().toLowerCase().contains(find))
				ol.add(o);
			if(find.equals(o.getOrderData().getOrdernr()))
				ol.add(o);
			if(find.equals(o.getOrderData().getOdate()))
				ol.add(o);
			if(find.equals(o.getOrderData().getDdate()))
				ol.add(o);
		}
		setFilteredOrders(ol);
	}

    public void searchOffer() {
        if(findOffer == null || findOffer.equals("") || findOffer.startsWith("all")) {
            setFilteredOffers(getAllOffers());
            return;
        }
        List<OrderEntity> ol = new ArrayList<OrderEntity>();
        findOffer = findOffer.toLowerCase();
        for(OrderEntity o: allOrders) {
            if(findOffer.startsWith("new") && o.getT() == 0)
                ol.add(o);
            if(findOffer.startsWith("in production") && o.getT() == 1)
                ol.add(o);
            if(findOffer.startsWith("completed") && o.getT() == 2)
                ol.add(o);
            if(findOffer.startsWith("deleted") && o.getT() == 3)
                ol.add(o);
            if(findOffer.equals(o.getOrderData().getOcnr()))
                ol.add(o);
            if(o.getOrderData().getCompany() != null && o.getOrderData().getCompany().toLowerCase().contains(find))
                ol.add(o);
            if(findOffer.equals(o.getOrderData().getOrdernr()))
                ol.add(o);
            if(findOffer.equals(o.getOrderData().getOdate()))
                ol.add(o);
            if(findOffer.equals(o.getOrderData().getDdate()))
                ol.add(o);
        }
        setFilteredOffers(ol);
    }
	
	public String getStatusName(Integer status) {
		if(status == 0)return "New";
		if(status == 1)return "In Production";
		if(status == 2)return "Completed";
		if(status == 3)return "Deleted";
		return "Unknown";
	}

    public String getStatusNameOffer(Integer status) {
        if(status == 0)return "Offered";
        if(status == 1)return "Accepted";
        return "Unknown";
    }
	
	
	
	//-------------------------- DATES --------------------------------------
	
	
	
	public void setDatesForSave() {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		if(orderDate != null)
			currentOrder.getOrderData().setOdate(df.format(orderDate));
		if(deliveryDate != null)
			currentOrder.getOrderData().setDdate(df.format(deliveryDate));
	}
	
	public String dateOnly(Date date) {
		if(date == null)
			return null;
		return new SimpleDateFormat("dd.MM.yyyy").format(date);
	}
	
	public String timeOnly(Date date) {
		if(date == null)
			return null;
		return new SimpleDateFormat("HH:mm:ss").format(date);
	}
	
	
	
	
	
	
	//-------------------------- ADDRESS ------------------------------------
	
	
	
	 public List<CompanyEntity> completeAddress(String query) {
		 List<CompanyEntity> companies = orderDao.getCompanyList();
		 List<CompanyEntity> filteredCompanies = new ArrayList<CompanyEntity>();
		 
		 for(CompanyEntity a: companies) {
		         if(a.getName().toUpperCase().contains(query.toUpperCase())) {
		                 String s = a.getName() + ",\n" + a.getAddress();
		                 s = s.replace("\n", " ");
		                 filteredCompanies.add(a);
		         }
		 }
		 
		 return filteredCompanies;
	 }
	
	public void selectDeliveryAddress() {
		deliveryAddress = service.getCompanyByName(deliveryAddress.getName());
		if(deliveryAddress == null)
			return;
		String s = deliveryAddress.getName() + ",\n" + deliveryAddress.getAddress();
        s = s.replace("\n", " ");
		currentOrder.getOrderData().setDaddres(s);
		currentOrder.getOrderData().setDcompany(deliveryAddress.getName());
	}
	
	public String findAddress(String company) {
		return service.getCompanyAddress(company);
	}
	
	public String formatAddress(String address) {
		if(address == null)
			return "";
		CompanyEntity company = service.getCompanyByAddress(address);
		if(company == null)
			return "";
		String formattedAddress = company.getName() + ",\n" + company.getAddress();
		formattedAddress = formattedAddress.replace("\n", " ");
		return formattedAddress;
	}

    public List<OrderEntity> getOrdersByProduct(ProductEntity product) {
        return orderDao.getOrdersByProduct(product);
    }
	
	
	
	
	
	
	
	
	//Getters and setters -------------------------------------------------------------------
	

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public OrderEntity getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(OrderEntity currentOrder) {
		if(currentOrder == null) {
			this.currentOrder = new OrderEntity();
			setProducts(new ArrayList<Product>());
			setCompanySelected(new CompanyEntity());
			this.currentOrder.getOrderData().setCurrency("EUR");
			this.currentOrder.setT(0);
			//setOrderDate(null);
			//setDeliveryDate(null);
			return;
		}
		
		this.currentOrder = currentOrder;
		setProducts(this.currentOrder.getOrderData().getP());
		companySelected.setName(this.currentOrder.getOrderData().getCompany());
		companySelected.setOnr(this.currentOrder.getOrderData().getOrdernr());
	}

	public CompanyEntity getCompanySelected() {
		return companySelected;
	}

	public void setCompanySelected(CompanyEntity companySelected) {
		this.companySelected = companySelected;
	}

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public Double getTotalProductPrice() {
		editPrice();
		Double t = new BigDecimal(totalProductPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
		currentOrder.getOrderData().setTot(t.toString());
		return t;
	}

	public void setTotalProductPrice(Double totalProductPrice) {
		this.totalProductPrice = totalProductPrice;
	}

	public List<OrderEntity> getAllOrders() {
		if(allOrders == null)
			refreshOrders();
		return allOrders;
	}

	public void setAllOrders(List<OrderEntity> allOrders) {
		this.allOrders = allOrders;
		if(allOrders == null) {
			this.allOrders = orderDao.getAllOrders();
			Gson gson = new Gson();
			for(OrderEntity o:this.allOrders) {
				OrderData order = gson.fromJson(o.getData(),OrderData.class);
				o.setOrderData(order);
			}
			Collections.sort(this.allOrders,new Comparator<OrderEntity>() {
				public int compare(OrderEntity x1, OrderEntity x2) {
					if(x2.getOrderData() == null || x2.getOrderData().getOcnr() == null || x1.getOrderData() == null)
						return 0;
					try {
						return Long.parseLong(x2.getOrderData().getOcnr()) > Long.parseLong(x1.getOrderData().getOcnr()) ? 1 : -1;
					} catch (NumberFormatException e) {
						return 0;
					}
				}
			});
		}
	}

    public List<OrderEntity> getAllOffers() {
        if(allOffers == null)
            refreshOffers();
        return allOffers;
    }

    public void setAllOffers(List<OrderEntity> allOffers) {
        this.allOffers = allOffers;
        if(allOffers == null) {
            this.allOffers = orderDao.getAllOffers();
            Gson gson = new Gson();
            for(OrderEntity o:this.allOffers) {
                OrderData order = gson.fromJson(o.getData(),OrderData.class);
                o.setOrderData(order);
            }
            Collections.sort(this.allOffers,new Comparator<OrderEntity>() {
                public int compare(OrderEntity x1, OrderEntity x2) {
                    if(x2.getOrderData() == null || x2.getOrderData().getOcnr() == null || x1.getOrderData() == null)
                        return 0;
									try {
										return Long.parseLong(x2.getOrderData().getOcnr()) > Long.parseLong(x1.getOrderData().getOcnr()) ? 1 : -1;
									} catch (NumberFormatException e) {
											return 0;
									}
								}
            });
        }
    }
	
	public List<OrderEntity> getFilteredOrders() {
		return filteredOrders;
	}

	public void setFilteredOrders(List<OrderEntity> filteredOrders) {
		this.filteredOrders = filteredOrders;
	}

    public List<OrderEntity> getFilteredOffers() {
        return filteredOffers;
    }

    public void setFilteredOffers(List<OrderEntity> filteredOffers) {
        this.filteredOffers = filteredOffers;
    }

    public Long getConfirmationNumber() {
		if(currentOrder != null && currentOrder.getOrderData() != null && currentOrder.getOrderData().getOcnr() != null)
			setConfirmationNumber(Long.parseLong(currentOrder.getOrderData().getOcnr()));
		else {
			getAllOrders();
			setConfirmationNumber(Long.parseLong(allOrders.get(0).getOrderData().getOcnr()) +1);
		}
		return confirmationNumber;
	}

    public Long getOfferNumber() {
        if(currentOrder != null && currentOrder.getOrderData() != null && currentOrder.getOrderData().getOcnr() != null)
            this.offerNumber = Long.parseLong(currentOrder.getOrderData().getOcnr());
        else {
            getAllOffers();
            if(allOffers == null || allOffers.size() == 0)
                this.offerNumber = 1L;
            else
                this.offerNumber = Long.parseLong(allOffers.get(0).getOrderData().getOcnr()) +1;
        }
        return offerNumber;
    }

    public void setOfferNumber(Long offerNumber) {
        this.offerNumber = offerNumber;
    }

    public void setConfirmationNumber(Long confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public OrderEntity getOrderToDelete() {
		return orderToDelete;
	}

	public void setOrderToDelete(OrderEntity orderToDelete) {
		this.orderToDelete = orderToDelete;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public MainService getService() {
		return service;
	}

	public void setService(MainService service) {
		this.service = service;
	}

	public String getFind() {
		return find;
	}

	public void setFind(String find) {
		this.find = find;
	}

	public CompanyEntity getDeliveryAddress() {
		if(deliveryAddress == null)
			deliveryAddress = new CompanyEntity();
		return deliveryAddress;
	}

    public String getFindOffer() {
        return findOffer;
    }

    public void setFindOffer(String findOffer) {
        this.findOffer = findOffer;
    }

    public void setDeliveryAddress(CompanyEntity deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

    public StockService getStockService() {
        return stockService;
    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }
}
