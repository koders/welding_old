package lv.welding.orders.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import com.google.gson.Gson;
import lv.welding.orders.models.*;
import lv.welding.orders.models.base.GenericJPADAO;

public class OrderJpaDao extends GenericJPADAO<OrderEntity, Long> implements OrderDao {

    private List<OrderEntity> allOrders;

	public OrderJpaDao() {
		super(OrderEntity.class);
	}

	@SuppressWarnings("unchecked")
	public List<OrderEntity> getAllOrders() {
		List<OrderEntity> results;
		Query query = getEntityManager().createQuery("select o from OrderEntity o where o.type = 0 or o.type = null");
        //query.setMaxResults(100);
		results = query.getResultList();
		return results;
	}

    @SuppressWarnings("unchecked")
    public List<OrderEntity> getAllOffers() {
        List<OrderEntity> results;
        Query query = getEntityManager().createQuery("select o from OrderEntity o where o.type = 1");
        //query.setMaxResults(100);
        results = query.getResultList();
        return results;
    }
	
	@SuppressWarnings("unchecked")
	public List<CompanyEntity> getCompanyList() {
		List<CompanyEntity> results;
		Query query = getEntityManager().createQuery("select company from CompanyEntity company");
		results = query.getResultList();
		return results;
	}

	@SuppressWarnings("unchecked")
	public CompanyEntity getCompany(String company) {
		List<CompanyEntity> result;
		
		Query query = getEntityManager().createQuery("select company from CompanyEntity company where company.name = :company");
		query.setParameter("company", company);
		result = query.getResultList();
		query.setMaxResults(1);
		
		if(result.isEmpty())
			return null;
		
		return result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public ProductEntity getProduct(String productNumber) {
		List<ProductEntity> result;
		
		Query query = getEntityManager().createQuery("select product from ProductEntity product");
		//query.setParameter("productNumber", productNumber);
		
		result = query.getResultList();
		
		if(result.isEmpty())
			return null;
		
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	public Long getNextConfirmationNumber() {
		List<OrderEntity> result;
		Query query = getEntityManager().createQuery("select order from OrderEntity order by order.orderData.ocnr DESC LIMIT 1");
		result = query.getResultList();
		if(result.isEmpty())
			return null;
		String s = result.get(0).getOrderData().getOcnr();
		return Long.parseLong(s)+1;
	}

	@SuppressWarnings("unchecked")
	public String getCompanyNumber(String company) {
		List<CompanyEntity> result;
		
		Query query = getEntityManager().createQuery("select company from CompanyEntity company where company.name = :company");
		query.setParameter("company", company);
		result = query.getResultList();
		query.setMaxResults(1);
		
		if(result.isEmpty())
			return null;
		
		return result.get(0).getOnr();
	}
	
	@SuppressWarnings("unchecked")
	public OrderEntity getOrderByNumber(String orderNumber) {
		List<OrderEntity> queryResults;
		
		Query query = getEntityManager().createQuery("select order from OrderEntity order");
		queryResults = query.getResultList();
		
		if(queryResults.isEmpty() || orderNumber == null)
			return null;
		
		for(OrderEntity o: queryResults) {
			if(orderNumber.equals(o.getOrderData().getOcnr()))
					return o;
		}
		
		return null;
	}

    @Override
    public List<OrderEntity> getOrdersByProduct(ProductEntity product) {
        if(product == null || product.getPno() == null)
            return null;
        if(allOrders == null)
            allOrders = getAllOrders();
        List<OrderEntity> orders = allOrders;
        orders = convertOrders(orders);
        List<OrderEntity> resultList = new ArrayList<OrderEntity>();
        for(OrderEntity o: orders) {
            try {
                for(Product p: o.getOrderData().getP()) {
                    if(product.getPno().equals(p.getPno())) {
                        if(p.getPcs() != null && p.getPcs() != "")
                            o.setShippedProductCount(Integer.valueOf(p.getPcs()));
                        resultList.add(o);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    public List<OrderEntity> convertOrders(List<OrderEntity> orders) {
        Gson gson = new Gson();
        for (OrderEntity o : orders) {
            OrderData order = gson.fromJson(o.getData(), OrderData.class);
            o.setOrderData(order);
        }
        Collections.sort(orders, new Comparator<OrderEntity>() {
            public int compare(OrderEntity x1, OrderEntity x2) {
                if (x2.getOrderData() == null || x2.getOrderData().getOcnr() == null || x1.getOrderData() == null)
                    return 0;
                return x2.getOrderData().getOcnr().compareTo(x1.getOrderData().getOcnr());
            }
        });

        return orders;
    }

    @Override
    public List<OrderEntity> getConvertedOrders() {
        return convertOrders(getAllOrders());
    }
}
