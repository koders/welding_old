package lv.welding.orders.dto;

import lv.welding.orders.dao.ProductDao;
import lv.welding.orders.models.InvoiceEntity;
import lv.welding.orders.models.ProductEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductStatistics {

    private ProductDao productDao;
    private InvoiceDTO invoiceService;
    private List<InvoiceEntity> allInvoices;
    private Date fromDate;
    private Date toDate;
    private List<ProductEntity> productsUsed;
    private Map<ProductEntity,Map<String,Map<String,Double>>> productStatistics;

    public Double getFullProductStatistics(ProductEntity product) {
        return null;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Map<ProductEntity, Map<String, Map<String, Double>>> getProductStatistics() {
        return productStatistics;
    }

    public void setProductStatistics(Map<ProductEntity, Map<String, Map<String, Double>>> productStatistics) {
        this.productStatistics = productStatistics;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductEntity> getProductsUsed() {
        setAllInvoices(invoiceService.getInvoices());

        if(fromDate == null)
            fromDate = new Date();
        if(toDate == null)
            toDate = new Date();

        Integer yearFrom = fromDate.getYear() + 1900;
        Integer monthFrom = fromDate.getMonth();

        Integer yearTo = toDate.getYear() + 1900;
        Integer monthTo = toDate.getMonth();

        Integer yearI = yearFrom;
        Integer monthI = monthFrom;

        for(;monthI<= monthTo && yearI <= yearTo; monthI++) {
            for(InvoiceEntity invoice: allInvoices) {
                String invoiceMonth = invoice.getDeliveryDate().substring(3);
                //if(invoiceMonth.equals())
            }
        }

        return productsUsed;
    }

    public void setProductsUsed(List<ProductEntity> productsUsed) {
        this.productsUsed = productsUsed;
    }

    public List<InvoiceEntity> getAllInvoices() {
        return allInvoices;
    }

    public void setAllInvoices(List<InvoiceEntity> allInvoices) {
        this.allInvoices = allInvoices;
    }

    public InvoiceDTO getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(InvoiceDTO invoiceService) {
        this.invoiceService = invoiceService;
    }
}
