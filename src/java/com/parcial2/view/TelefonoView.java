/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parcial2.view;

import com.parcial2.dao.MarcaDAOImpl;
import com.parcial2.dao.TelefonoDAOImpl;
import com.parcial2.modelo.Marca;
import com.parcial2.modelo.Telefono;
import com.parcial2.util.Utilities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 *
 * @author jayson
 */
@ManagedBean
@ViewScoped
public class TelefonoView {

    private Integer idTelefono;
    private List<SelectItem> listaMarcas;
    private List<Telefono> listaTelefonos;
    private List<Telefono> listaTelefonos2;
    private InputText txtModelo;
    private Date fechaCompra;
    private InputText txtUbicacion;
    private InputText txtExtension;
    private boolean activo;
    private TelefonoDAOImpl telefonoDAOImpl;
    private MarcaDAOImpl marcaDAOImpl;
    private Telefono telefono;
    private SelectOneMenu cboMarca;
    private SelectOneMenu cboMarca2;
    private SelectOneMenu cboEstado;
    private boolean habilitar=true;
    /**
     * Creates a new instance of TelefonoView
     */
    public TelefonoView() {
        listaMarcas=new ArrayList();
        listaTelefonos=new ArrayList();
        listaTelefonos2=new ArrayList();
        telefono=new Telefono();
        telefonoDAOImpl=new TelefonoDAOImpl();
        marcaDAOImpl=new MarcaDAOImpl();
    }
    
    @PostConstruct
    public void init(){
        initialize();
        action_clear();
        deshabilitar(true);
        llenarListadoTelefonos();
        llenarListadoMarcas();
    }
    
    private void initialize(){
        txtModelo=new InputText();
        txtUbicacion=new InputText();
        cboEstado=new SelectOneMenu();
        cboMarca=new SelectOneMenu();
        txtExtension=new InputText();
        fechaCompra=new Date();
    }
    
    private void deshabilitar(boolean valor){
        txtModelo.setDisabled(!valor);
        txtUbicacion.setDisabled(valor);
        cboEstado.setDisabled(valor);
        cboMarca.setDisabled(!valor);
        txtExtension.setDisabled(valor);
        habilitar=valor;
    }
    
    public void txt_id(){
        try{
            if((Utilities.checkInteger(cboMarca)!=null)){
                if(Utilities.checkString(txtModelo)!=null){
                    String sql="model.activo=1 and model.modelo='"+txtModelo.getValue()+"' and model.marca.idmarca="+cboMarca.getValue();
                    telefono=(telefonoDAOImpl.findByCriteria(sql).size()>0?telefonoDAOImpl.findByCriteria(sql).get(0):null);
                    if(telefono!=null){
                        txtModelo.setValue(telefono.getModelo());
                        txtUbicacion.setValue(telefono.getUbicacion());
                        cboEstado.setValue(telefono.isActivo());
                        cboMarca.setValue(telefono.getMarca().getIdmarca());
                        txtExtension.setValue(telefono.getExtension());
                        fechaCompra=telefono.getFechaCompra();
                        deshabilitar(false);
                    }else{
                        deshabilitar(false);
                    }
                }
            }
        }catch(Exception ex){
            telefono=null;
            ex.printStackTrace();
        }
    }
    
    public void action_save(){
        if(telefono==null){
            action_create();
        }else{
            action_update();
        }
        init();
    }
    
    public void action_create(){
        try{
            telefono=new Telefono();
            telefono.setModelo(Utilities.checkString(txtModelo));
            telefono.setUbicacion(Utilities.checkString(txtUbicacion));
            telefono.setActivo(Utilities.checkBoolean(cboEstado));
            telefono.setIdTelefono(0);
            telefono.setExtension(Utilities.checkInteger(txtExtension));            
            Marca marca=marcaDAOImpl.findById(Utilities.checkInteger(cboMarca));
            telefono.setMarca(marca);
            telefono.setFechaCompra(fechaCompra);
            telefonoDAOImpl.create(telefono);
            Utilities.addInfoMessage("Se ha grabado Exitosamente");
            action_clear();
        }catch(Exception e){            
            Utilities.addWarningMessage(e.getMessage());
        }
    }
    public void action_update(){
        try{
            telefono.setModelo(Utilities.checkString(txtModelo));
            telefono.setUbicacion(Utilities.checkString(txtUbicacion));
            telefono.setActivo(Utilities.checkBoolean(cboEstado));            
            telefono.setExtension(Utilities.checkInteger(txtExtension));            
            Marca marca=marcaDAOImpl.findById(Utilities.checkInteger(cboMarca));
            telefono.setMarca(marca);
            telefono.setFechaCompra(fechaCompra);
            telefonoDAOImpl.update(telefono);
            Utilities.addInfoMessage("Se ha actualizado Exitosamente");
            action_clear();
        }catch(Exception e){            
            Utilities.addWarningMessage(e.getMessage());
        }
    }
    public void action_delete(){
        try{
            telefonoDAOImpl.delete(telefono);
            Utilities.addInfoMessage("Se ha eliminado Exitosamente");
        }catch(Exception e){            
            Utilities.addWarningMessage(e.getMessage());
        }
    }
    
    public void action_clear(){
        telefono = new Telefono();
        txtModelo.setValue("");
        txtUbicacion.setValue("");
        cboEstado.setValue("");
        cboMarca.setValue("");
        fechaCompra=new Date();
        txtExtension.setValue("");
        deshabilitar(true);
    }
    
    public void action_clearR(){
        
        cboMarca2.setValue("");
        listaTelefonos2=new ArrayList();
    }
    
    private void llenarListadoTelefonos(){
        try{
            List<Telefono> listado = telefonoDAOImpl.findByCriteria("model.activo in (0,1)");
            if(listado.size()>0){
                for(Telefono tel : listado){
                    if(tel.isActivo()){
                        tel.setDescEstado("Activo");
                    }else{
                        tel.setDescEstado("Inactivo");
                    }
                }
            }
            setListaTelefonos(listado);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private void llenarListadoMarcas(){
        try{
            List<Marca> listado = marcaDAOImpl.findByCriteria(null);
            if(listado.size()>0){
                for (Marca m : listado) {
                    listaMarcas.add(new SelectItem(m.getIdmarca(), m.getNomMarca()));
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public String action_buscar(){
        try{
            String sql1="", sql2="";
            if(Utilities.checkInteger(cboMarca2)!=null)
                sql1="model.marca.idmarca="+cboMarca2.getValue();
            if(Utilities.checkString(txtModelo)!=null){
                if(!sql1.equals("")){
                    sql2=" and model.modelo like '%"+txtModelo.getValue()+"%'";
                }else{
                    sql2="model.modelo like '%"+txtModelo.getValue()+"%'";
                }
            }
                    
            System.out.println(sql1);
            List<Telefono> listado = telefonoDAOImpl.findByCriteria(sql1+sql2);
            if(listado.size()>0){
                for(Telefono tel : listado){
                    if(tel.isActivo()){
                        tel.setDescEstado("Activo");
                    }else{
                        tel.setDescEstado("Inactivo");
                    }
                }
            }
            setListaTelefonos2(listado);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    public List<SelectItem> getListaMarcas() {
        return listaMarcas;
    }

    public void setListaMarcas(List<SelectItem> listaMarcas) {
        this.listaMarcas = listaMarcas;
    }

    public List<Telefono> getListaTelefonos() {
        return listaTelefonos;
    }

    public void setListaTelefonos(List<Telefono> listaTelefonos) {
        this.listaTelefonos = listaTelefonos;
    }

    public List<Telefono> getListaTelefonos2() {
        return listaTelefonos2;
    }

    public void setListaTelefonos2(List<Telefono> listaTelefonos2) {
        this.listaTelefonos2 = listaTelefonos2;
    }

    public InputText getTxtModelo() {
        return txtModelo;
    }

    public void setTxtModelo(InputText txtModelo) {
        this.txtModelo = txtModelo;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public InputText getTxtUbicacion() {
        return txtUbicacion;
    }

    public void setTxtUbicacion(InputText txtUbicacion) {
        this.txtUbicacion = txtUbicacion;
    }

    public InputText getTxtExtension() {
        return txtExtension;
    }

    public void setTxtExtension(InputText txtExtension) {
        this.txtExtension = txtExtension;
    }

    public SelectOneMenu getCboMarca() {
        return cboMarca;
    }

    public void setCboMarca(SelectOneMenu cboMarca) {
        this.cboMarca = cboMarca;
    }

    public SelectOneMenu getCboMarca2() {
        return cboMarca2;
    }

    public void setCboMarca2(SelectOneMenu cboMarca2) {
        this.cboMarca2 = cboMarca2;
    }

    public SelectOneMenu getCboEstado() {
        return cboEstado;
    }

    public void setCboEstado(SelectOneMenu cboEstado) {
        this.cboEstado = cboEstado;
    }

    public boolean isHabilitar() {
        return habilitar;
    }

    public void setHabilitar(boolean habilitar) {
        this.habilitar = habilitar;
    }
    
    
}
