package lv.welding.orders.dao;

import java.util.List;

import lv.welding.orders.models.CountryEntity;
import lv.welding.orders.models.InvoiceEntity;
import lv.welding.orders.models.ProductEntity;
import lv.welding.orders.models.base.GenericDAO;

public interface InvoiceDao extends GenericDAO<InvoiceEntity, Long> {

	public List<InvoiceEntity> getInvoices();
	public List<CountryEntity> getCountries();

    /**
     * Filters only invoices, which contain selected product
     * @since 24.08.2014
     * @param product the product which invoice must contain
     * @return List with invoices which contain selected product
     */
    public List<InvoiceEntity> getInvoicesByProduct(ProductEntity product);

    /**
     * Filters invoices for specific country (filtering in HIBERNATE QUERY)
     * @since 24.08.2014
     * @param country
     * @return invoices for specific country
     */
    public List<InvoiceEntity> getInvoicesByCountry(String country);

    /**
     * Returrns all invoices already converted and ready for use
     * @since 27.08.2014
     * @return
     */
    public List<InvoiceEntity> getConvertedInvoices();
}
