package lv.welding.orders.dao;

import lv.welding.orders.models.ProductSizeEntity;
import lv.welding.orders.models.base.GenericJPADAO;

import javax.persistence.Query;
import java.util.List;

public class ProductSizeJpaDao extends GenericJPADAO<ProductSizeEntity, Long> implements ProductSizeDao {

	public ProductSizeJpaDao() {
		super(ProductSizeEntity.class);
	}

    @Override
    public List<ProductSizeEntity> getAllProductSizes() {
        List<ProductSizeEntity> results;
        Query query = getEntityManager().createQuery("select ps from ProductSizeEntity ps");
        results = query.getResultList();
        return results;
    }

    @Override
    public ProductSizeEntity getProductSize(String productSizeName) {
        List<ProductSizeEntity> result;

        Query query = getEntityManager().createQuery("select ps from ProductSizeEntity ps where ps.name = :productSizeName");
        query.setParameter("productSizeName", productSizeName);

        result = query.getResultList();

        if(result.isEmpty())
            return null;

        return result.get(0);
    }
}
