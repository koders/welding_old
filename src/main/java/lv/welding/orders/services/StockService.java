package lv.welding.orders.services;

import lv.welding.orders.models.ProductEntity;

import java.util.List;

public interface StockService {

    public void updateStock();
    public void clearFields();
    public List<ProductEntity> getProducts();
}
