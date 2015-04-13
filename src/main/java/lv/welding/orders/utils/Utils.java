package lv.welding.orders.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rihards on 16.08.2014.
 */
public class Utils {

    public static void msg(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(message));
    }

    public static void errorMsg(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invoice can't be saved", message));
    }

    public static void flashMsg(String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        flash.setRedirect(true);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,message,null));
    }

    public static void redirectTo(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/welding/" + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(date);
    }

    public static boolean checkIfNullOrEmpty(Object o) {
        if(o == null || o.equals(""))
            return true;
        return false;
    }

    /**
     * Extracts month from date format dd.MM.yyyy
     * @since 13.09.2014
     * @return month number as int ranged from 1 to 12, returns 0 if failed to convert date
     */
    public static int getMonthFromDate(String date) {
        try {
            Date d = new SimpleDateFormat("dd.MM.yyyy").parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return cal.get(Calendar.MONTH) + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
