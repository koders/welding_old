package lv.welding.orders.dao;

import java.util.List;

import lv.welding.orders.models.UserEntity;
import lv.welding.orders.models.base.GenericDAO;

public interface UserDao extends GenericDAO<UserEntity, Long> {

	public UserEntity getUserByUsername(String userName);
	public List<UserEntity> getAllUsers();
}
