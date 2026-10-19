package com.Taller_apirest.app.modules.proveedores.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "proveedores")
public class Proveedor {

    @Id
    private String id;
    private String nit;
    private String razonSocial;
    private String contacto;
    private String telefono;
    private String email;
    private String ciudad;
    private List<SuministroProveedor> suministros = new ArrayList<>();
    private boolean activo = true;

    public Proveedor() {
    }

    public Proveedor(String id, String nit, String razonSocial, String contacto, String telefono, String email, String ciudad) {
        this.id = id;
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
        this.ciudad = ciudad;
        this.activo = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public List<SuministroProveedor> getSuministros() {
        return suministros;
    }

    public void setSuministros(List<SuministroProveedor> suministros) {
        this.suministros = suministros;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
