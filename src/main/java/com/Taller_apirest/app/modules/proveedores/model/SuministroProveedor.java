package com.Taller_apirest.app.modules.proveedores.model;

public class SuministroProveedor {

    private String codigo;
    private String nombre;
    private String categoria;
    private Double precioCosto;
    private Integer stockBodega;

    public SuministroProveedor() {
    }

    public SuministroProveedor(String codigo, String nombre, String categoria, Double precioCosto, Integer stockBodega) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioCosto = precioCosto;
        this.stockBodega = stockBodega;
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

    public Double getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(Double precioCosto) {
        this.precioCosto = precioCosto;
    }

    public Integer getStockBodega() {
        return stockBodega;
    }

    public void setStockBodega(Integer stockBodega) {
        this.stockBodega = stockBodega;
    }
}
