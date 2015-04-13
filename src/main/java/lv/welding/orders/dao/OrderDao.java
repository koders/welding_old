package lv.welding.orders.dao;

import java.util.List;

import lv.welding.orders.models.CompanyEntity;
import lv.welding.orders.models.OrderEntity;
import lv.welding.orders.models.ProductEntity;
import lv.welding.orders.models.base.GenericDAO;

public interface OrderDao extends GenericDAO<OrderEntity, Long> {

	public Long getNextConfirmationNumber();
	public List<OrderEntity> getAllOrders();
	public List<OrderEntity> getAllOffers();
	public List<CompanyEntity> getCompanyList();
	public CompanyEntity getCompany(String company);
	public ProductEntity getProduct(String productNumber);
	public String getCompanyNumber(String company);
	public OrderEntity getOrderByNumber(String orderNumber);

    /**
     * Filters only orders, which contain selected product
     * @since 16.08.2014
     * @param product the product which order must contain
     * @return List with orders which contain selected product
     */
    public List<OrderEntity> getOrdersByProduct(ProductEntity product);

    /**
     * Converts order data inside order, which is stored as json in database
     * @since 16.08.2014
     * @return List of converted orders
     */
    public List<OrderEntity> getConvertedOrders();
}
