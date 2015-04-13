package lv.welding.orders.dao;

import lv.welding.orders.models.ProductModelEntity;
import lv.welding.orders.models.base.GenericDAO;

import java.util.List;

public interface ProductModelDao extends GenericDAO<ProductModelEntity, Long> {
	public List<ProductModelEntity> getAllProductModels();
	public ProductModelEntity getProductModel(String productModelName);
}
