package lv.welding.orders.dao;

import lv.welding.orders.models.CategoryEntity;
import lv.welding.orders.models.base.GenericJPADAO;

import javax.persistence.Query;
import java.util.List;

public class CategoryJpaDao extends GenericJPADAO<CategoryEntity, Long> implements CategoryDao {

	public CategoryJpaDao() {
		super(CategoryEntity.class);
	}

    @SuppressWarnings("unchecked")
    public CategoryEntity getCategory(String categoryName) {
        List<CategoryEntity> result;

        Query query = getEntityManager().createQuery("select c from CategoryEntity c where c.name = :categoryName");
        query.setParameter("categoryName", categoryName);

        result = query.getResultList();

        if(result.isEmpty())
            return null;

        return result.get(0);
    }

	@SuppressWarnings("unchecked")
	public List<CategoryEntity> getAllCategories() {
		List<CategoryEntity> results;
		Query query = getEntityManager().createQuery("select c from CategoryEntity c");
		results = query.getResultList();
		return results;
	}
	
	
}
