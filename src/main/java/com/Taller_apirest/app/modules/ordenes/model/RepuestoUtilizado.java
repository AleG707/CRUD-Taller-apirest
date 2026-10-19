package com.Taller_apirest.app.modules.ordenes.model;

public class RepuestoUtilizado {

    private String repuestoId;
    private String codigo;
    private String nombre;
    private Integer cantidad = 1;
    private Double precioUnitario = 0.0;
    private Double subtotal = 0.0;

    public RepuestoUtilizado() {
    }

    public RepuestoUtilizado(String repuestoId, String codigo, String nombre, Integer cantidad, Double precioUnitario) {
        this.repuestoId = repuestoId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = (cantidad != null && precioUnitario != null) ? cantidad * precioUnitario : 0.0;
    }

    public String getRepuestoId() {
        return repuestoId;
    }

    public void setRepuestoId(String repuestoId) {
        this.repuestoId = repuestoId;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        recalcularSubtotal();
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
        recalcularSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    private void recalcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = cantidad * precioUnitario;
        }
    }
}
