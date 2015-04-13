package lv.welding.orders.dao;

import java.util.List;

import lv.welding.orders.models.CountryEntity;
import lv.welding.orders.models.base.GenericDAO;

public interface CountryDao extends GenericDAO<CountryEntity, Long> {

	public CountryEntity getCountry(String country);
	public List<CountryEntity> getCountries();
}
