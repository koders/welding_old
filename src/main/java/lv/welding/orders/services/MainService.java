package lv.welding.orders.services;

import java.util.List;

import lv.welding.orders.models.CompanyEntity;
import lv.welding.orders.models.OrderEntity;
import lv.welding.orders.models.Product;

public interface MainService {
	public void nextNumber(CompanyEntity company);
	public void saveNewPerson(String person);
	public void saveNewProducts(List<Product> products);
	public CompanyEntity getCompanyByName(String company);
	public CompanyEntity getCompanyByAddress(String address);
	public String getCompanyAddress(String company);
}
