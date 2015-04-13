package lv.welding.orders.services.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lv.welding.orders.dao.UserDao;
import lv.welding.orders.models.UserEntity;
import lv.welding.orders.services.UserService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceImpl implements UserService, UserDetailsService {

	private UserDao userDao;

	public boolean createUserIfNotExist(UserEntity userEntity) throws Exception {
		
		try {
			if(userDao.getUserByUsername(userEntity.getUsername()) != null)
				throw new Exception("User already in database!");
			userDao.save(userEntity);
		} catch(DataIntegrityViolationException e) {
			if (e.getRootCause() instanceof SQLException) {
				if (((SQLException)e.getRootCause()).getErrorCode() == 1) {
					throw new Exception("User already in database!");
				}
			}
		} catch (Exception e) {
			throw new Exception("User already in database!");
		}
		
		return false;
	}
	
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		UserEntity user = userDao.getUserByUsername(userName);
		
		if(user == null) {
			throw new UsernameNotFoundException("Username does not exist in database!");
		}
		
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		if(user.getRole() == 1)
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		User userDetails = new User(user.getUsername(), user.getPassword(), authorities);
		
		return userDetails;
	}


	public UserEntity loadUserByUsername1(String userName) {
		UserEntity user = userDao.getUserByUsername(userName);
		
		if(user == null) {
			throw new UsernameNotFoundException("Username does not exist in database!");
		}
		return user;
	}


	public List<UserEntity> getAllUsers() {
		
		return userDao.getAllUsers();
	}


	public void deleteUser(UserEntity user) {
		userDao.delete(user);
	}

}
