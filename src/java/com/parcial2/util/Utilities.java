/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parcial2.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import org.primefaces.component.inputtext.InputText;

/**
 *
 * @author jayson
 */
public class Utilities {
    
    public static void addInfoMessage(String msg){
        addInfoMessage(null,msg);
    }
    
    public static void addInfoMessage(String clientId, String msg){
        FacesContext.getCurrentInstance().addMessage(clientId, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }
    public static void addWarningMessage(String msg){
        addWarningMessage(null,msg);
    }
    
    public static void addWarningMessage(String clientId, String msg){
        FacesContext.getCurrentInstance().addMessage(clientId, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
    }
    
    public static String checkString(Object input) throws Exception{
        if(input==null){
            return null;
        }
        UIInput inputValue=(UIInput) input;
        if(inputValue.getValue() == null){
            return null;
        }
        if(inputValue.getValue().equals("")){
            return null;
        }
        try{
            return String.valueOf(inputValue.getValue().toString());
        }catch(Exception ex){
            throw new Exception("Valor del campo no es valido");
        }
    }
    
    public static Integer checkInteger(Object input) throws Exception{
        if(input==null){
            return null;
        }
        UIInput inputValue=(UIInput) input;
        if(inputValue.getValue() == null){
            return null;
        }
        if(inputValue.getValue().equals("")){
            return null;
        }
        try{
            return Integer.parseInt(inputValue.getValue().toString());
        }catch(Exception ex){
            throw new Exception("Valor del campo no es valido");
        }
    }
    
    public static Boolean checkBoolean(Object input) throws Exception{
        boolean res;
        if(input==null){
            return null;
        }
        UIInput inputValue=(UIInput) input;
        if(inputValue.getValue() == null){
            return null;
        }
        if(inputValue.getValue().equals("")){
            return null;
        }
        try{
            res=(Integer.parseInt(inputValue.getValue().toString())==1?true:false);
            return res;
        }catch(Exception ex){
            throw new Exception("Valor del campo no es valido");
        }
    }
}
