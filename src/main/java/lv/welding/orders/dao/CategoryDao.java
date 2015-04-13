package lv.welding.orders.dao;

import lv.welding.orders.models.CategoryEntity;
import lv.welding.orders.models.base.GenericDAO;

import java.util.List;

public interface CategoryDao extends GenericDAO<CategoryEntity, Long> {
	public List<CategoryEntity> getAllCategories();
	public CategoryEntity getCategory(String categoryName);
}
