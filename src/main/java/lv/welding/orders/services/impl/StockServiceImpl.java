package lv.welding.orders.services.impl;

import lv.welding.orders.dao.OrderDao;
import lv.welding.orders.dao.ProductDao;
import lv.welding.orders.models.OrderEntity;
import lv.welding.orders.models.Product;
import lv.welding.orders.models.ProductEntity;
import lv.welding.orders.services.StockService;
import lv.welding.orders.utils.Utils;
import org.primefaces.event.SelectEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Rihards on 15.08.2014.
 */
public class StockServiceImpl implements StockService {

    private final static Logger Log = Logger.getLogger(StockServiceImpl.class.getName());

    ProductDao productDao;
    OrderDao orderDao;

    List<ProductEntity> products;
    ProductEntity selectedProduct;

    public void clearFields() {
        getProducts();
        for(ProductEntity p: products) {
            if(p == null)
                continue;
            p.setAddToStock(0);
        }
    }

    public void updateStock() {
        getProducts();
        try {
            for(ProductEntity p: products) {
                if(p == null)
                    continue;
                if(p.getAddToStock() != 0) {
                    p.setInStock(p.getInStock() + p.getAddToStock());
                    productDao.update(p);
                }
            }
            clearFields();
            Utils.msg("Stock successfully updated!");
        } catch (Exception e) {
            Utils.msg("There was an error while updating stock...\nPlease contact administrator");
            e.printStackTrace();
        }
    }

    public void calculateShippedTotalAllProducts(String fromDateString) {
        getProducts();
        Date fromDate = null;
        try {
            // In case conversion fails use default
            fromDate = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2014");
            fromDate = new SimpleDateFormat("dd.MM.yyyy").parse(fromDateString);
        } catch (ParseException e) {
            Log.log(Level.WARNING, "Failed to convert date " + fromDateString);
        }
        List<OrderEntity> orders = orderDao.getConvertedOrders();
        HashMap<String, Integer> results = new HashMap<String, Integer>();
        boolean check;
        for(OrderEntity order: orders) {
            check = false;
            try {
                check = new SimpleDateFormat("dd.MM.yyyy").parse(order.getOrderData().getDdate()).after(fromDate);
            } catch (Exception e) {
                if(order != null && order.getOrderData() != null)
                    Log.log(Level.WARNING, "Failed to convert date\norder: " + order.getOrderData().getOcnr() + "\ndate: " + order.getOrderData().getDdate());
                else
                    Log.log(Level.SEVERE, "order == null or orderData == null");
            }
            if(!check)
                continue;
            for(Product product: order.getOrderData().getP()) {
                try {
                    Integer current = results.get(product.getPno());
                    if(current == null)
                        current = 0;
                    results.put(product.getPno(), current + Integer.valueOf(product.getPcs()));
                } catch (NumberFormatException e) {
                    Log.log(Level.SEVERE, order.getOrderData().getOcnr());
                    e.printStackTrace();
                }
            }
        }

        for(ProductEntity p: products) {
            p.setTotalShipped(results.get(p.getPno()));
            productDao.update(p);
        }

        Utils.msg("Successfully calculated!");
    }

    public void resetShippedTotal() {
        getProducts();
        for(ProductEntity p: products) {
            p.setTotalShipped(0);
            productDao.update(p);
        }
        Utils.msg("Reset successfully done!");
    }




    public void onRowSelect(SelectEvent event) {
        Utils.msg("Product " + ((ProductEntity)event.getObject()).getPno() + "selected");
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductEntity> getProducts() {
        if(products == null)
            products = productDao.getAllProducts();
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    public ProductEntity getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(ProductEntity selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
}
