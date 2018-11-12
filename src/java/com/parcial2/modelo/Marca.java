package com.parcial2.modelo;
// Generated May 16, 2016 7:18:01 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Marca generated by hbm2java
 */
public class Marca  implements java.io.Serializable {


     private Integer idmarca;
     private String nomMarca;
     private Integer contrato;
     private Set telefonos = new HashSet(0);

    public Marca() {
    }

    public Marca(String nomMarca, Integer contrato, Set telefonos) {
       this.nomMarca = nomMarca;
       this.contrato = contrato;
       this.telefonos = telefonos;
    }
   
    public Integer getIdmarca() {
        return this.idmarca;
    }
    
    public void setIdmarca(Integer idmarca) {
        this.idmarca = idmarca;
    }
    public String getNomMarca() {
        return this.nomMarca;
    }
    
    public void setNomMarca(String nomMarca) {
        this.nomMarca = nomMarca;
    }
    public Integer getContrato() {
        return this.contrato;
    }
    
    public void setContrato(Integer contrato) {
        this.contrato = contrato;
    }
    public Set getTelefonos() {
        return this.telefonos;
    }
    
    public void setTelefonos(Set telefonos) {
        this.telefonos = telefonos;
    }




}

