package lv.welding.orders.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import lv.welding.orders.models.ProductEntity;
import lv.welding.orders.models.base.GenericJPADAO;

public class ProductJpaDao extends GenericJPADAO<ProductEntity, Long> implements ProductDao {
	
	public ProductJpaDao() {
		super(ProductEntity.class);
	}
	
	@SuppressWarnings("unchecked")
	public ProductEntity getProduct(String productNumber) {
		List<ProductEntity> result;
		
		Query query = getEntityManager().createQuery("select product from ProductEntity product where product.pno = :productNumber");
		query.setParameter("productNumber", productNumber);
		
		result = query.getResultList();
		
		if(result.isEmpty())
			return null;
		
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<ProductEntity> getAllProducts() {
		List<ProductEntity> results;
		Query query = getEntityManager().createQuery("select p from ProductEntity p");
		results = query.getResultList();

        // Sort by product number
        Collections.sort(results, new Comparator<ProductEntity>() {
            public int compare(ProductEntity x1, ProductEntity x2) {
                if(x1 == null || x1.getPno() == null)
                    return -1;
                if(x2 == null || x2.getPno() == null)
                    return 1;
                return x1.getPno().compareTo(x2.getPno());
            }
        });

		return results;
	}

	@Override
	public List<ProductEntity> getProductsForModel(String productModelName, String productSizeName, String productMaterialName) {
		Query query = getEntityManager().createQuery("select p from ProductEntity p where p.productModel.name = :productModelName and p.productSize.name = :productSizeName and p.productMaterial.name = :productMaterialName");
		query.setParameter("productModelName", productModelName);
		query.setParameter("productSizeName", productSizeName);
		query.setParameter("productMaterialName", productMaterialName);

		return query.getResultList();
	}

	@Override
	public Long getProductCountForModel(String productModelName, String productSizeName, String productMaterialName) {
		String w1 = productModelName == null || productModelName == "" ? "1=1" : "p.productModel.name = :productModelName";
		String w2 = productSizeName == null || productSizeName == "" ? "1=1" : "p.productSize.name = :productSizeName";
		String w3 = productMaterialName == null || productMaterialName == "" ? "1=1" : "p.productMaterial.name = :productMaterialName";
		Query query = getEntityManager().createQuery("select count(*) from ProductEntity p where " + w1 + " and " + w2 + " and " + w3);
		if(!w1.equals("1=1"))query.setParameter("productModelName", productModelName);
		if(!w2.equals("1=1"))query.setParameter("productSizeName", productSizeName);
		if(!w3.equals("1=1"))query.setParameter("productMaterialName", productMaterialName);

		return (Long)query.getSingleResult();
	}
	
	
}
