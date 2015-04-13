package lv.welding.orders.services.impl;

import com.google.gson.Gson;
import lv.welding.orders.dao.InvoiceDao;
import lv.welding.orders.dao.ProductDao;
import lv.welding.orders.models.InvoiceEntity;
import lv.welding.orders.models.ProductEntity;
import lv.welding.orders.models.charts.Customer;

import java.util.*;

/**
 * Created by Rihards on 27.08.2014.
 */
public class ChartServiceImpl {

    private Gson gson = new Gson();
    private InvoiceDao invoiceDao;
    private ProductDao productDao;

    private List<InvoiceEntity> invoices;

    public void refreshInvoices() {
        invoices = invoiceDao.getConvertedInvoices();
    }

    public String getCustomerStatistics(Integer count) {
        Map<String,Integer> customerMap = new HashMap<String, Integer>();
        List<Customer> customers = new ArrayList<Customer>();
        if(invoices == null)
            refreshInvoices();
        for(InvoiceEntity i: invoices) {
            if(i.getBuyerInfo() != null && i.getBuyerInfo().getName() != null && i.getCurrency() != null) {
                int add;
                // Convert currency to EUR
                if(i.getCurrency().equalsIgnoreCase("eur")) {
                    add = i.getTotalAmount().intValue();
                } else if(i.getCurrency().equalsIgnoreCase("nok")) {
                    add = (int)(i.getTotalAmount().intValue() * 0.122739564);
                } else continue;

                    int current = 0;
                    if (customerMap.get(i.getBuyerInfo().getName()) != null)
                        current = customerMap.get(i.getBuyerInfo().getName());
                    customerMap.put(i.getBuyerInfo().getName(), current + add);
            }
        }

        Iterator it = customerMap.entrySet().iterator();
        Customer c;
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            c = new Customer((String)pairs.getKey(), (Integer)pairs.getValue());
            customers.add(c);
            it.remove();
        }

        Collections.sort(customers, new Comparator<Customer>() {
            public int compare(Customer x1, Customer x2) {
                if(x1.getMoneySpent() < x2.getMoneySpent())
                    return 1;
                if(x1.getMoneySpent() > x2.getMoneySpent())
                    return -1;
                return 0;
            }
        });

        if(count == 0 || count == null)
            return gson.toJson(customers);

        List<Customer> customersCountList = new ArrayList<Customer>();
        for(int i=0;i<count;i++)
            customersCountList.add(customers.get(i));

        return gson.toJson(customersCountList);
    }

    public String getTop10Customers() {
        return getCustomerStatistics(10);
    }

    /**
     * @since 13.09.2014
     * @return months in a year as JSON
     */
    public String getMonthList() {
        List<String> months = new ArrayList<String>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        return gson.toJson(months);
    }

    public String getProductList() {
        List<String> products = new ArrayList<String>();
        try {
            for(ProductEntity product: productDao.getAllProducts()) {
                products.add(product.getPno());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gson.toJson(products);
    }









    public InvoiceDao getInvoiceDao() {
        return invoiceDao;
    }

    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }
}
