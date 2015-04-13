package lv.welding.orders.dao;

import java.util.*;

import javax.persistence.Query;

import com.google.gson.Gson;
import lv.welding.orders.models.*;
import lv.welding.orders.models.base.GenericJPADAO;

public class InvoiceJpaDao extends GenericJPADAO<InvoiceEntity, Long> implements InvoiceDao {

    private Gson gson = new Gson();
    private List<InvoiceEntity> allInvoices;

	public InvoiceJpaDao() {
		super(InvoiceEntity.class);
	}

    @Override
	@SuppressWarnings("unchecked")
	public List<InvoiceEntity> getInvoices() {
		List<InvoiceEntity> results;
		Query query = getEntityManager().createQuery("select i from InvoiceEntity i");
		results = query.getResultList();
		return results;
	}

    @Override
    @SuppressWarnings("unchecked")
    public List<InvoiceEntity> getConvertedInvoices() {
        return convertInvoices(getInvoices());
    }

    @Override
	@SuppressWarnings("unchecked")
	public List<CountryEntity> getCountries() {
		List<CountryEntity> results;
		Query query = getEntityManager().createQuery("select c from CountryEntity c");
		results = query.getResultList();
		return results;
	}

    @Override
    public List<InvoiceEntity> getInvoicesByProduct(ProductEntity product) {
        if(product == null || product.getPno() == null)
            return null;
        if(allInvoices == null)
            allInvoices = convertInvoices(getInvoices());
        List<InvoiceEntity> invoices = allInvoices;
        List<InvoiceEntity> resultList = new ArrayList<InvoiceEntity>();
        for(InvoiceEntity i: invoices) {
            try {
                for(Product p: i.getProductData()) {
                    if(product.getPno().equals(p.getPno())) {
                        if(p.getPcs() != null && p.getPcs() != "")
                            i.setShippedProductCount(Integer.valueOf(p.getPcs()));
                        resultList.add(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public List<InvoiceEntity> getInvoicesByCountry(String country) {
        List<InvoiceEntity> results;
        Query query = getEntityManager().createQuery("select i from InvoiceEntity i where i.country = :country");
        query.setParameter("country", country);
        results = query.getResultList();
        results = convertInvoices(results);
        return results;
    }

    private List<InvoiceEntity> convertInvoices(List<InvoiceEntity> invoices) {

        for(InvoiceEntity i: invoices) {
            i.setSellerInfo(gson.fromJson(i.getSeller(),CompanyEntity.class));
            i.setBuyerInfo(gson.fromJson(i.getBuyer(),CompanyEntity.class));
            i.setDeliveryInfo(gson.fromJson(i.getDelivery(),CompanyEntity.class));
            Product[] p = gson.fromJson(i.getProducts(), Product[].class);
            i.setProductData(Arrays.asList(p));
        }
        Collections.sort(invoices, new Comparator<InvoiceEntity>() {
            public int compare(InvoiceEntity x1, InvoiceEntity x2) {
                return x2.getCreated().compareTo(x1.getCreated());
            }
        });

        return invoices;
    }
}
