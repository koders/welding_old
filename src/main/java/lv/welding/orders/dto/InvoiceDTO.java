package lv.welding.orders.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import lv.welding.orders.models.*;
import lv.welding.orders.utils.Utils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import lv.welding.orders.dao.CompanyDao;
import lv.welding.orders.dao.InvoiceDao;
import lv.welding.orders.dao.OrderDao;
import lv.welding.orders.dao.ProductDao;
import lv.welding.orders.services.MainService;

import com.google.gson.Gson;


public class InvoiceDTO {

	private Gson gson = new Gson();
	private InvoiceDao invoiceDao;
	private OrderDao orderDao;
	private CompanyDao companyDao;
	private ProductDao productDao;
	private UserDTO userDto;

	private MainService service;

	private List<InvoiceEntity> invoices;
	private List<CountryEntity> countries;
	@SuppressWarnings("unused")
	private List<String> countriesString;
	private CompanyEntity sellerInfo;
	private CompanyEntity buyerInfo;
	private CompanyEntity deliveryInfo;

	private List<OrderEntity> orders;
	private List<CompanyEntity> companies;
	private List<Product> products = new ArrayList<Product>();
	private OrderModel ordersModel;
	private OrderEntity order = new OrderEntity();
	private InvoiceEntity invoice;
	private InvoiceEntity stockInvoice;
	private InvoiceEntity invoiceToDelete;
	private DualListModel<String> months;
    private String category;

	private boolean edit;
	private boolean c1;
	private boolean c2;
	private boolean c3;
	private String saveAction;
    private String packingListCurrentPage;
    private String packingListTotalPages;
    private boolean packingListGenerated = false;




	public String compressedText(String string, int symbols) {
		if(string.length() <= 50) return string;
		return string.substring(0,symbols) + "...";
	}

	public void reset() {
		setInvoice(new InvoiceEntity());
		setOrder(new OrderEntity());
		setProducts(new ArrayList<Product>());
		setEdit(false);

		invoice.setNumber(getNextNumber());
		invoice.setYear(getYear());
	}

	public BigDecimal setScale(Double number, int scale) {
		return new BigDecimal(number).setScale(scale, RoundingMode.HALF_UP);
	}

	public List<InvoiceDataTableModel> getFilteredInvoices(String country) {
		getInvoices();
		List<InvoiceDataTableModel> results = new ArrayList<InvoiceDataTableModel>();
		if(invoices.isEmpty())
			return results;

		if(country == null || country.equals("") || country.equalsIgnoreCase("All Invoices")) {
            results.addAll(convertInvoicesToTableModel(invoices));
		}
		else {
            results.addAll(convertInvoicesToTableModel(getInvoicesByCategory(country)));
		}

        if(country != null && country.equalsIgnoreCase("dashboard")) {
            List<InvoiceEntity> invoicesForDashboard = getLastInvoices(10L);
            return convertInvoicesToTableModel(invoicesForDashboard);
        }
		return results;
	}

    private List<InvoiceDataTableModel> convertInvoicesToTableModel(List<InvoiceEntity> invoices) {
        List<InvoiceDataTableModel> results = new ArrayList<InvoiceDataTableModel>();
        if(invoices == null || invoices.size() == 0)
            invoices = invoiceDao.getInvoices();

        for(int i = invoices.get(0).getYear().intValue(); i>=invoices.get(invoices.size()-1).getYear().intValue(); i--) {
            InvoiceDataTableModel t = new InvoiceDataTableModel();
            t.setYear(i);
            for(InvoiceEntity j: invoices) {
                if(j.getYear().intValue() == i) {
                    t.getInvoices().add(j);
                    if(j.getCurrency() != null && j.getCurrency().toLowerCase().equals("eur"))
                        t.setTotalEur(t.getTotalEur() + j.getTotalAmount());
                    if(j.getCurrency() != null && j.getCurrency().toLowerCase().equals("nok"))
                        t.setTotalNok(t.getTotalNok() + j.getTotalAmount());
                }
            }
            if(!t.getInvoices().isEmpty()){
                results.add(t);
            }
        }
        return results;
    }

	public List<InvoiceDataTableModel> getInvoicesForReport(List<String> months) {
		getInvoices();
		List<InvoiceDataTableModel> results = new ArrayList<InvoiceDataTableModel>();
		if(invoices.isEmpty() || months == null)
			return results;
		for(Integer i = invoices.get(0).getYear().intValue(); i>=invoices.get(invoices.size()-1).getYear().intValue(); i--) {
			InvoiceDataTableModel t = new InvoiceDataTableModel();
			t.setYear(i);
			for(InvoiceEntity j: invoices) {
				if(months.contains(getMonthFromInvoice(j)) && j.getYear() != null && j.getYear().toString().equals(i.toString())) {
                    t.getInvoices().add(j);
                    if(j.getCurrency() != null && j.getCurrency().toLowerCase().equals("eur"))
                        t.setTotalEur(t.getTotalEur() + j.getTotalAmount());
                    if(j.getCurrency() != null && j.getCurrency().toLowerCase().equals("nok"))
                        t.setTotalNok(t.getTotalNok() + j.getTotalAmount());
                }
			}
            if(!t.getInvoices().isEmpty())
			    results.add(t);
		}
		return results;
	}

    private List<InvoiceEntity> getLastInvoices(Long count) {
        List<InvoiceEntity> results = new ArrayList<InvoiceEntity>();
        for(InvoiceEntity i: invoices) {
            results.add(i);
            if(results.size() >= count)
                break;
        }
        return results;
    }

	public String replaceNewlines(String s) {
		s = s.replace("\n","<br />");
		s = s.replace("\\n","<br />");
		return s;
	}

	public List<CompanyEntity> completeCompany(String company) {
		if(company == null || company == "")
			return null;
		List<CompanyEntity> results = new ArrayList<CompanyEntity>();
		for(CompanyEntity i: getCompanies()) {
			if(i == null || i.getName() == null)
				continue;
			if(i.getName().toLowerCase().contains(company.toLowerCase()))
				results.add(i);
		}
		return results;
	}

	@SuppressWarnings("deprecation")
	public Long getYear() {
		Date d = new Date();
		return (long)d.getYear() + 1900;
	}

	public Long getNextNumber() {
		if(invoices == null || invoices.isEmpty())
			return 1L;
		Long number = invoices.get(0).getNumber();
		if(getYear().equals(invoices.get(0).getYear()))
			return number + 1;
		else
			return 1L;
	}

	public void orderSelected() {
		setProducts(getOrder().getOrderData().getP());
		setDataFromOrder();
		editPrice();
	}

	public void detachOrder() {
		setOrder(new OrderEntity());
	}

	public void resetOrderData() {
		setOrder(new OrderEntity());
		setProducts(new ArrayList<Product>());
	}

	public void addProduct() {
		products.add(new Product());
	}

	public void removeProduct(Product product) {
		products.remove(product);
		editPrice();
	}

	public void editPrice(){
		getInvoice().setAmount(0.0);

		for(Product p: getProducts()) {
			try {
				Double.parseDouble(p.getPrice());
				Double.parseDouble(p.getPcs());
			} catch (Exception e) {
				continue;
			}
			getInvoice().setAmount(getInvoice().getAmount() + Double.parseDouble(p.getPrice()) * Double.parseDouble(p.getPcs()));
		}
	}

    public void editPriceByInvoice(InvoiceEntity invoice){
        invoice.setStockAmount(0.0);

        for(Product p: invoice.getProductData()) {
            try {
                Double.parseDouble(p.getStockPrice());
                Double.parseDouble(p.getPcs());
            } catch (Exception e) {
                continue;
            }
            invoice.setStockAmount(invoice.getStockAmount() + Double.parseDouble(p.getStockPrice()) * Double.parseDouble(p.getPcs()));
        }
    }

	public String validateInvoice() {
		StringBuilder error = new StringBuilder();
		if(getInvoice().getSellerInfo() == null || getInvoice().getSellerInfo().getName() == null)
			error.append("Seller field must not be empty\n");
		if(getInvoice().getBuyerInfo() == null || getInvoice().getBuyerInfo().getName() == null)
			error.append("Buyer field must not be empty\n");
		if(Utils.checkIfNullOrEmpty(getInvoice().getPurchaseNumber()))
			error.append("Purchase number must not be empty\n");
		if(Utils.checkIfNullOrEmpty(getInvoice().getCurrency()))
			error.append("Currency must not be empty\n");
		if(Utils.checkIfNullOrEmpty(getInvoice().getHsCode()))
			error.append("HS CODE must not be empty\n");
		if(Utils.checkIfNullOrEmpty(getInvoice().getDeliveryDate()))
			error.append("Delivery date must not be empty\n");
		if(Utils.checkIfNullOrEmpty(getInvoice().getTermsPayment()))
			error.append("Terms of payment must not be empty\n");
		if(Utils.checkIfNullOrEmpty(getInvoice().getDueDate()))
			error.append("Due date must not be empty\n");
		if(Utils.checkIfNullOrEmpty(getInvoice().getPvn()))
			error.append("VAT directive must not be empty\n");
		return error.toString();
	}

	public void save() {
		String error = validateInvoice();
		if(!error.isEmpty()) {
			Utils.errorMsg(error);
			setSaveAction("");
			return;
		}
		if(edit) {
			saveEdited();
			return;
		}
		invoice.setNumber(getNextNumber());
		invoice.setYear(getYear());
		invoice.setOrderNumber(getOrder().getOrderData().getOcnr());
		invoice.setMarking(getOrder().getOrderData().getMarking());
		invoice.setRef(getOrder().getOrderData().getCperson());
		setData();
		invoice.setCreated(new Date());
		if(getOrder().getOrderData().getOcnr() != null) {
			setDataFromOrder();
		}
		service.saveNewProducts(products);
		service.saveNewPerson(invoice.getContactPerson());
		service.saveNewPerson(invoice.getRef());
		//invoice.setCreatedBy(userDto.getUserName());
		invoiceDao.save(getInvoice());
		Utils.flashMsg("Invoice successfully saved!");
		setSaveAction("back");

        // After saving set for editing
//        getInvoices();
//        for(InvoiceEntity i: invoices) {
//            if(i.getMarking() != null && i.getMarking().equals(invoice.getMarking()) && i.getOrderNumber() != null && i.getOrderNumber().equals(invoice.getOrderNumber())) {
//                setInvoiceForEdit(i);
//                break;
//            }
//        }
		setInvoiceForEdit(invoice);
        setEdit(true);
	}

	public void setData() {
		if(invoice.getSellerInfo() != null)
			invoice.setSellerInfo(getCompanyData(invoice.getSellerInfo().getName()));
		invoice.setSeller(gson.toJson(invoice.getSellerInfo()));
		if(invoice.getBuyerInfo() != null)
			invoice.setBuyerInfo(getCompanyData(invoice.getBuyerInfo().getName()));
		invoice.setBuyer(gson.toJson(invoice.getBuyerInfo()));
		if(invoice.getDeliveryInfo() != null)
			invoice.setDeliveryInfo(getCompanyData(invoice.getDeliveryInfo().getName()));
		invoice.setDelivery(gson.toJson(invoice.getDeliveryInfo()));
    for(Product p: products) {
      if(p.getStockPrice() == null) {
        p.setStockPrice(p.getPrice());
        p.setStockTPrice(p.getTprice());
      }
    }
		invoice.setProductData(products);
		invoice.setProducts(gson.toJson(products));
		invoice.setTotalAmount(invoice.getAmount() + invoice.getAmount() * invoice.getVat());
		//invoice.setPvn(convertSymbols(invoice.getPvn()));
		//invoice.setPvnSpecify(convertSymbols(invoice.getPvnSpecify()));
		//invoice.setSpecification(convertSymbols(invoice.getSpecification()));
	}

	public void setDataFromOrder() {
		//invoice.getDeliveryInfo().setAddress(trimOrderAddress(order.getOrderData().getDaddres()));
    invoice.setDelivery(gson.toJson(invoice.getDeliveryInfo()));
    invoice.setPurchaseNumber(getOrder().getOrderData().getOrdernr());
    invoice.setMarking(getOrder().getOrderData().getMarking());
		invoice.setCurrency(getOrder().getOrderData().getCurrency());
		invoice.setRef(getOrder().getOrderData().getCperson());
	}

	public void saveEdited() {
		setData();
		if(getOrder().getOrderData().getOcnr() != null) {
			setDataFromOrder();
		}
		service.saveNewProducts(products);
    service.saveNewPerson(invoice.getContactPerson());
    service.saveNewPerson(invoice.getRef());
		invoiceDao.update(invoice);
    Utils.flashMsg("Invoice updated!");
		setSaveAction("back");
	}

    public void saveStockInvoice() {
        invoice = stockInvoice;
        products = stockInvoice.getProductData();
        saveEdited();
        recalculateStockAmount(stockInvoice);
    }

    public void recalculateStockAmount(InvoiceEntity stockInvoice) {
        stockInvoice.setStockAmount(0.00);
        for(Product p : stockInvoice.getProductData()) {
            stockInvoice.setStockAmount(stockInvoice.getStockAmount() + Double.parseDouble(p.getStockPrice()) * Double.parseDouble(p.getPcs()));
        }
    }

	public void delete() {
		invoiceDao.delete(invoiceToDelete);
		Utils.msg("Invoice successfully deleted!");
	}

	public void setInvoiceForEdit(InvoiceEntity invoice) {
		resetOrderData();
		if(invoice == null) {
			this.invoice = new InvoiceEntity();
			setProducts(new ArrayList<Product>());
			return;
		}
		this.invoice = invoice;
		this.order = getOrderByNumber(invoice.getOrderNumber());
		setProducts(new ArrayList<Product>(invoice.getProductData()));
		edit = true;
	}

	public Double calculateTotalWeight(Product p) {
		try {
			Double a = Double.parseDouble(p.getWeight());
			Double b = Double.parseDouble(p.getPcs());
			return a * b;
		} catch (Exception e) {
			return 0.0;
		}
	}

	public void calculateNettoWeight() {
		getInvoice().setNettoWeight(0.0);
		for(Product p: getProducts()) {
			getInvoice().setNettoWeight(getInvoice().getNettoWeight() + calculateTotalWeight(p));
		}
	}

	public void calculateBruttoWeight() {
		calculateNettoWeight();
		Double add = 0.0;
		if(getInvoice().isPackingPlastic())
			add += getInvoice().getPlasticWeight();
		if(getInvoice().isPackingOther())
			add += getInvoice().getOtherWeight();
		getInvoice().setBruttoWeight(getInvoice().getNettoWeight() + add);
	}

    public void calculateNettoWeight(List<Product> products) {
        getInvoice().setNettoWeight(0.0);
        for(Product p: products) {
            getInvoice().setNettoWeight(getInvoice().getNettoWeight() + calculateTotalWeight(p));
        }
    }

    public void calculateBruttoWeight(List<Product> products) {
        calculateNettoWeight(products);
        Double add = 0.0;
        if(getInvoice().isPackingPlastic())
            add += getInvoice().getPlasticWeight();
        if(getInvoice().isPackingOther())
            add += getInvoice().getOtherWeight();
        getInvoice().setBruttoWeight(getInvoice().getNettoWeight() + add);
    }


				public BigDecimal calculateTotalAmount(InvoiceEntity invoice) {
					BigDecimal sum = new BigDecimal(0);
					if(invoice == null || invoice.getProductData() == null) {
						return sum;
					}
					for(Product p : invoice.getProductData()) {
						try {
							BigDecimal currentSum = calculatePrice(p) ;
							if( currentSum != null ){
								sum.add(currentSum);
							}
						} catch(Exception e){
								System.out.print(e);
						}
					}
					return sum;
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

	public String getDeliveryNameAndAddress(InvoiceEntity invoice) {
		return invoice.getDeliveryInfo().getName() + ", " + invoice.getDeliveryInfo().getAddress();
	}

	public OrderEntity getOrderByNumber(String orderNumber) {
		if(orderNumber == null)
			return null;
		if(orders == null)
			setOrders(orderDao.getAllOrders());
		for(OrderEntity o: orders) {
			if(orderNumber.equals(o.getOrderData().getOcnr()))
				return o;
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void resetMonths() {
		List<String> newMonths = new ArrayList<String>();
		Date date = new Date(System.currentTimeMillis());
		for(Integer i=2010;i<=(date.getYear() + 1900);i++) {
			for(Integer j=1;j<=12;j++) {
				String t = "";
				if(j < 10)
					t = "0";
				t += j.toString() + "." + i.toString();
				newMonths.add(t);
			}
		}
		setMonths(new DualListModel<String>(newMonths, new ArrayList<String>()));
	}

	public String getMonthFromInvoice(InvoiceEntity invoice) {
		return invoice.getDeliveryDate().substring(3);
	}

    /**
     * Function that returns Pakcing List pagination info, used in header and page title
     * @since 24.08.2014
     * @return Empty string if Total pages are less than 2, packingListCurrentPage + " of " + packingListTotalPages otherwise.
     */
	public String getPackingListPaginationInfo() {
        if(packingListTotalPages == null || packingListTotalPages.equals("") || packingListTotalPages.equals("1"))
            return "";
        return packingListCurrentPage + " of " + packingListTotalPages;
    }

    /**
     * Filters invoices for specific category (filtering linear, in JAVA) [see InvoiceDao.getInvoicesByCountry(String country) for filtering in hibernate query]
     * @since 24.08.2014
     * @param category
     * @return invoices for specific category
     */
    public List<InvoiceEntity> getInvoicesByCategory(String category) {
        List<InvoiceEntity> results = new ArrayList<InvoiceEntity>();
        if(category == null)
            return results;
        for(InvoiceEntity i: invoices) {
            if(category.equalsIgnoreCase(i.getCountry()))
                results.add(i);
        }

        return results;
    }

    public List<InvoiceDataTableModel> getInvoicesByProduct(ProductEntity product) {
        return convertInvoicesToTableModel(invoiceDao.getInvoicesByProduct(product));
    }

    /**
     * Utility method which keeps packing list dialog opened when back button pressed
     * @since 06.09.2014
     */
    public void generatePackingList() {
        //clearPackingListPages();
        packingListGenerated = true;
    }

    /**
     * Utility method to clear packing list dialog current page and total pages.
     * @since 06.09.2014
     */
    public void clearPackingListPages () {
        packingListGenerated = false;
        packingListCurrentPage = "";
        packingListTotalPages = "";
    }
















	public CompanyEntity getCompanyData(String name) {
		return companyDao.getCompany(name);
	}

	public List<InvoiceEntity> getInvoices() {
		invoices = invoiceDao.getInvoices();
		for(InvoiceEntity i: invoices) {
			i.setSellerInfo(gson.fromJson(i.getSeller(),CompanyEntity.class));
			i.setBuyerInfo(gson.fromJson(i.getBuyer(),CompanyEntity.class));
			i.setDeliveryInfo(gson.fromJson(i.getDelivery(),CompanyEntity.class));
			Product[] p = gson.fromJson(i.getProducts(), Product[].class);
			i.setProductData(Arrays.asList(p));
		}
		Collections.sort(invoices,new Comparator<InvoiceEntity>() {
			public int compare(InvoiceEntity x1, InvoiceEntity x2) {
				return x2.getCreated().compareTo(x1.getCreated());
			}
		});
		return invoices;
	}

	public void setInvoices(List<InvoiceEntity> invoices) {
		this.invoices = invoices;
	}

	public InvoiceDao getInvoiceDao() {
		return invoiceDao;
	}

	public void setInvoiceDao(InvoiceDao invoiceDao) {
		this.invoiceDao = invoiceDao;
	}

	public OrderEntity getOrder() {
		if(order == null)
			setOrder(new OrderEntity());
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public List<OrderEntity> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderEntity> orders) {
		for(OrderEntity o: orders) {
			OrderData order = gson.fromJson(o.getData(),OrderData.class);
			o.setOrderData(order);
		}

		Collections.sort(orders,new Comparator<OrderEntity>() {
			public int compare(OrderEntity x1, OrderEntity x2) {
				if(x2.getOrderData() == null || x2.getOrderData().getOcnr() == null || x1.getOrderData() == null)
					return 0;
				return x2.getOrderData().getOcnr().compareTo(x1.getOrderData().getOcnr());
			}
		});
		this.orders = orders;
	}

	public OrderModel getOrdersModel() {
		setOrders(orderDao.getAllOrders());
		setOrdersModel(new OrderModel(orders));
		return ordersModel;
	}

	public void setOrdersModel(OrderModel ordersModel) {
		this.ordersModel = ordersModel;
	}
	public List<CountryEntity> getCountries() {
		setCountries(invoiceDao.getCountries());
		return countries;
	}
	public void setCountries(List<CountryEntity> countries) {
		this.countries = countries;
	}
	public InvoiceEntity getInvoice() {
		if(invoice == null)
			setInvoice(new InvoiceEntity());
		return invoice;
	}

	public void setInvoice(InvoiceEntity invoice) {
		this.invoice = invoice;
	}

	public List<String> getCountriesString() {
		List<String> results = new ArrayList<String>();
		for(CountryEntity c: getCountries()) {
			results.add(c.getName());
		}
		Collections.sort(results);
		return results;
	}

	public void setCountriesString(List<String> countriesString) {
		this.countriesString = countriesString;
	}

	public CompanyDao getCompanyDao() {
		return companyDao;
	}

	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}

	public List<CompanyEntity> getCompanies() {
		setCompanies(companyDao.getCompanyList());
		return companies;
	}

	public void setCompanies(List<CompanyEntity> companies) {
		this.companies = companies;
	}

	public CompanyEntity getSellerInfo() {
		return sellerInfo;
	}

	public void setSellerInfo(CompanyEntity sellerInfo) {
		if(sellerInfo != null && sellerInfo.getName() != null) {
			this.sellerInfo = companyDao.getCompany(sellerInfo.getName());
			return;
		}
		if(sellerInfo == null)
			this.sellerInfo = new CompanyEntity();
		else
			this.sellerInfo = sellerInfo;
	}

	public CompanyEntity getBuyerInfo() {
		return buyerInfo;
	}

	public void setBuyerInfo(CompanyEntity buyerInfo) {
		if(buyerInfo != null && buyerInfo.getName() != null) {
			this.buyerInfo = companyDao.getCompany(buyerInfo.getName());
			return;
		}
		if(buyerInfo == null)
			this.buyerInfo = new CompanyEntity();
		else
			this.buyerInfo = buyerInfo;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public CompanyEntity getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(CompanyEntity deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	public InvoiceEntity getInvoiceToDelete() {
		return invoiceToDelete;
	}

	public void setInvoiceToDelete(InvoiceEntity invoiceToDelete) {
		this.invoiceToDelete = invoiceToDelete;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isC1() {
		return c1;
	}

	public void setC1(boolean c1) {
		this.c1 = c1;
	}

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public String getSaveAction() {
		return saveAction;
	}

	public void setSaveAction(String saveAction) {
		this.saveAction = saveAction;
	}

	public String saveAction() {
		return getSaveAction();
	}

	public boolean isC2() {
		return c2;
	}

	public void setC2(boolean c2) {
		this.c2 = c2;
	}

	public boolean isC3() {
		return c3;
	}

	public void setC3(boolean c3) {
		this.c3 = c3;
	}

	public MainService getService() {
		return service;
	}

	public void setService(MainService service) {
		this.service = service;
	}

	public UserDTO getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDTO userDto) {
		this.userDto = userDto;
	}

	public DualListModel<String> getMonths() {
		if(months == null) {
			resetMonths();
		}
		return months;
	}

	public void setMonths(DualListModel<String> months) {
		this.months = months;
	}

    public InvoiceEntity getStockInvoice() {
        return stockInvoice;
    }

    public void setStockInvoice(InvoiceEntity stockInvoice) {
        this.stockInvoice = stockInvoice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPackingListCurrentPage() {
        return packingListCurrentPage;
    }

    public void setPackingListCurrentPage(String packingListCurrentPage) {
        this.packingListCurrentPage = packingListCurrentPage;
    }

    public String getPackingListTotalPages() {
        return packingListTotalPages;
    }

    public void setPackingListTotalPages(String packingListTotalPages) {
        this.packingListTotalPages = packingListTotalPages;
    }

    public boolean isPackingListGenerated() {
        return packingListGenerated;
    }

    public void setPackingListGenerated(boolean packingListGenerated) {
        this.packingListGenerated = packingListGenerated;
    }
}
