package lv.welding.orders.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import lv.welding.orders.models.CompanyEntity;

@FacesConverter(value="companyConverter")
public class CompanyConverter implements Converter {
	  
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
    	CompanyEntity c = new CompanyEntity();
    	c.setName(submittedValue);
        return c;  
    }  
  
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
        if (value == null || value.equals("") || ((CompanyEntity) value).getName() == null) {  
            return "";  
        } else {  
            return String.valueOf(((CompanyEntity) value).getName());  
        }  
    }
}
