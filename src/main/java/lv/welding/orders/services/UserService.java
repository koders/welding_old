package lv.welding.orders.services;

import java.util.List;

import lv.welding.orders.models.UserEntity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
	public void deleteUser(UserEntity user);
	public boolean createUserIfNotExist(UserEntity userEntity) throws Exception;
	public UserEntity loadUserByUsername1(String userName) throws UsernameNotFoundException;
	public List<UserEntity> getAllUsers();
}
