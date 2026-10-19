package com.Taller_apirest.app.modules.admin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "repuestos_catalogo")
public class RepuestoCatalogo {

    @Id
    private String id;
    private String codigo;
    private String nombre;
    private String categoria;
    private String marca;
    private Double precioVenta;
    private Integer stockMinimo = 5;
    private Integer stockActual = 0;
    private String proveedorDefault;

    public RepuestoCatalogo() {
    }

    public RepuestoCatalogo(String id, String codigo, String nombre, String categoria, String marca, Double precioVenta, Integer stockMinimo, Integer stockActual, String proveedorDefault) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.marca = marca;
        this.precioVenta = precioVenta;
        this.stockMinimo = stockMinimo;
        this.stockActual = stockActual;
        this.proveedorDefault = proveedorDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public String getProveedorDefault() {
        return proveedorDefault;
    }

    public void setProveedorDefault(String proveedorDefault) {
        this.proveedorDefault = proveedorDefault;
    }
}
