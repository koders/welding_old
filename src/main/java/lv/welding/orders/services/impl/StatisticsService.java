package lv.welding.orders.services.impl;

import lv.welding.orders.models.*;
import lv.welding.orders.dto.AdministrationDTO;
import lv.welding.orders.dto.InvoiceDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Rihards on 23.06.2014..
 */
public class StatisticsService {
    AdministrationDTO administrationDTO;
    InvoiceDTO invoiceDTO;

    DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private String fromDateString = "01.01.1970";
    private String toDateString = format.format(new Date());
    private List<InvoiceEntity> invoices;
    private Map<String, Map<String, Map<String, Integer>>> productStatisticsCache;

    public List<Product> findProductData(List<InvoiceEntity> invoices, List<ProductEntity> products) {
        List<Product> results = new ArrayList<Product>();
        for(InvoiceEntity i: invoices) {
            for(Product p: i.getProductData()) {
                for(ProductEntity p1: products) {
                    if(p.getPno() != null && p.getPno().equals(p1.getPno()))
                       results.add(p);
                }
            }
        }

        return results;
    }

    public List<InvoiceEntity> findInvoicesForYear(Integer year) {
        List<InvoiceEntity> results = null;
        for(InvoiceDataTableModel idtm: invoiceDTO.getFilteredInvoices("All Invoices")) {
            if(idtm.getYear().equals(year))
                results = idtm.getInvoices();
        }
        return results;
    }

    public List<ProductEntity> findCategoryProducts(CategoryEntity category) {
        List<ProductEntity> results = new ArrayList<ProductEntity>();
        results.addAll(category.getProducts());
        for(CategoryEntity c: category.getSubCategories()) {
            results.addAll(findCategoryProducts(c));
        }
        return results;
    }

    public Long calculateCount(CategoryEntity category,Integer year) {
        Long result = 0L;
        List<InvoiceEntity> invoices = findInvoicesForYear(year);

        List<ProductEntity> categoryProducts = findCategoryProducts(category);

        List<Product> products = findProductData(invoices, categoryProducts);

        for(Product p: products) {
            try {
                result += Long.valueOf(p.getPcs());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public Double calculateWeight(CategoryEntity category, Integer year) {
        Double result = 0.0;
        if(category == null)
            return result;
        List<InvoiceEntity> invoices = findInvoicesForYear(year);

        List<ProductEntity> categoryProducts = findCategoryProducts(category);

        List<Product> products = findProductData(invoices, categoryProducts);
        if(category.getSubCategories() != null)
            for(CategoryEntity c: category.getSubCategories())
                products.addAll(findProductData(invoices, c.getProducts()));

        for(Product p: products) {
            try {
                result += Double.valueOf(p.getWeight()) * Double.valueOf(p.getPcs());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<InvoiceEntity> findInvoicesForInterval(Date dateFrom, Date dateTo) {
        if(dateFrom == null) {
            try {
                dateFrom = format.parse("01.01.1970");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(dateTo == null) {
            dateTo = new Date();
        }
        List<InvoiceEntity> results = new ArrayList<InvoiceEntity>();
        for(InvoiceDataTableModel idtm: invoiceDTO.getFilteredInvoices("All Invoices")) {
            for(InvoiceEntity i: idtm.getInvoices()) {
                Date deliveryDate = null;
                try {
                    deliveryDate = format.parse(i.getDeliveryDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(deliveryDate.after(dateFrom) && deliveryDate.before(dateTo)) {
                    results.add(i);
                }
            }
        }
        return results;
    }

    public Integer getProductCount(ProductModelEntity productModel, ProductSizeEntity productSize, ProductMaterialEntity productMaterial) {
        Date dateFrom = parseDate(toDateString);
        Date dateTo = parseDate(fromDateString);
        return getProductCount(dateFrom, dateTo, productModel, productSize, productMaterial);
    }

    public Date parseDate(String dateString) {
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return date;
    }

    public Integer getProductCount(Date dateFrom, Date dateTo, ProductModelEntity productModel, ProductSizeEntity productSize, ProductMaterialEntity productMaterial) {
        if(invoices == null) {
            invoices = findInvoicesForInterval(dateFrom, dateTo);
        }
        List<ProductEntity> modelProducts = administrationDTO.getModelProducts(productModel, productSize, productMaterial);
        Integer result = 0;
        int add = 0;
        for(InvoiceEntity i: invoices) {
            for(Product p: i.getProductData()) {
                for(ProductEntity p1: modelProducts) {
                    if(p.getPno() != null && p.getPno().equals(p1.getPno())) {
                        add = 0;
                        try {
                            add = Integer.parseInt(p.getPcs());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        result += add;
                    }
                }
            }
        }
        return result;
    }

    public Integer totalProductCountForSize(ProductSizeEntity productSize, ProductMaterialEntity productMaterial) {
        int result = 0;
        for(Map.Entry<String, Map<String, Integer>> entry: productStatisticsCache.get(productMaterial.getName()).entrySet()) {
            result += entry.getValue().get(productSize.getName());
        }
        return result;
    }

    public Integer totalProductCountForModel(ProductModelEntity productModel, ProductMaterialEntity productMaterial) {
        int result = 0;
        for(Map.Entry<String, Integer> entry: productStatisticsCache.get(productMaterial.getName()).get(productModel.getName()).entrySet()) {
            result += entry.getValue();
        }
        return result;
    }

    public Integer totalProductCountForMaterial(ProductMaterialEntity productMaterial) {
        int result = 0;
        for(Map.Entry<String, Map<String, Integer>> entry: productStatisticsCache.get(productMaterial.getName()).entrySet()) {
            for(Map.Entry<String, Integer> entry1: entry.getValue().entrySet()) {
                result += entry1.getValue();
            }
        }
        return result;
    }

    public Integer getProductCountFromCache(ProductModelEntity productModel, ProductSizeEntity productSize, ProductMaterialEntity productMaterial) {
        if(productStatisticsCache == null) {
            initProductStatisticsCache();
        }

        return productStatisticsCache.get(productMaterial.getName()).get(productModel.getName()).get(productSize.getName());
    }

    private void initProductStatisticsCache() {
        productStatisticsCache = new HashMap<String, Map<String, Map<String, Integer>>>();
        for(ProductMaterialEntity pma: administrationDTO.getProductMaterials()) {
            productStatisticsCache.put(pma.getName(), new HashMap<String, Map<String, Integer>>());
            for(ProductModelEntity pmo: administrationDTO.getProductModels()) {
                productStatisticsCache.get(pma.getName()).put(pmo.getName(), new HashMap<String, Integer>());
                for(ProductSizeEntity ps: administrationDTO.getProductSizes()) {
                    productStatisticsCache.get(pma.getName()).get(pmo.getName()).put(ps.getName(), getProductCount(pmo, ps, pma));
                }
            }
        }
    }

    public void refreshStatistics() {
        invoices = findInvoicesForInterval(parseDate(fromDateString), parseDate(toDateString));
        initProductStatisticsCache();
    }

    public AdministrationDTO getAdministrationDTO() {
        return administrationDTO;
    }

    public void setAdministrationDTO(AdministrationDTO administrationDTO) {
        this.administrationDTO = administrationDTO;
    }

    public InvoiceDTO getInvoiceDTO() {
        return invoiceDTO;
    }

    public void setInvoiceDTO(InvoiceDTO invoiceDTO) {
        this.invoiceDTO = invoiceDTO;
    }

    public String getFromDateString() {
        return fromDateString;
    }

    public void setFromDateString(String fromDateString) {
        this.fromDateString = fromDateString;
    }

    public String getToDateString() {
        return toDateString;
    }

    public void setToDateString(String toDateString) {
        this.toDateString = toDateString;
    }

    public List<InvoiceEntity> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceEntity> invoices) {
        this.invoices = invoices;
    }
}
