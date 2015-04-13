package lv.welding.orders.dto;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;

import lv.welding.orders.models.UserEntity;
import lv.welding.orders.services.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private UserService userService;
    private AdministrationDTO administrationDTO;
	private AuthenticationManager authenticationManager;

	private String userName;
	private String password;
	private Integer role;


	public void userAuthentication(ActionEvent event) throws Exception {

		UserEntity user;

		try {

			user = userService.loadUserByUsername1(userName);

            //Refresh username after loading user from database
			userName = user.getUsername();

            //Authentication
			Authentication request = new UsernamePasswordAuthenticationToken(this.userName, this.password);
			Authentication result = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);
		} catch (UsernameNotFoundException e) {
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null));
			return;
		} catch (AuthenticationException e) {
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,"Wrong password!",null));
			return;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,"Problems connecting to database...",null));
			return;
		}


		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Welcome, " + userName,null));

		//Redirect


		try {
			//facesContext.getExternalContext().redirect("/welding/app/orders");
            administrationDTO.setActiveIndex(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void logout(ActionEvent event) throws IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
	    context.redirect("/welding/app/logout");
	}
	
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

    public AdministrationDTO getAdministrationDTO() {
        return administrationDTO;
    }

    public void setAdministrationDTO(AdministrationDTO administrationDTO) {
        this.administrationDTO = administrationDTO;
    }
}
