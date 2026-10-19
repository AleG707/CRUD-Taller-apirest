package com.Taller_apirest.app.modules.proveedores.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "pedidos_reabastecimiento")
public class PedidoReabastecimiento {

    @Id
    private String id;
    private String codigoPedido;
    private String proveedorId;
    private String proveedorNombre;
    private String repuestoCodigo;
    private String repuestoNombre;
    private Integer cantidadSolicitada;
    private Double precioUnitario = 0.0;
    private Double costoTotal = 0.0;

    private EstadoPedidoReabastecimiento estado = EstadoPedidoReabastecimiento.SOLICITADO;

    private String transportadora;
    private String numeroGuia;
    private String observacionesEntrega;

    private LocalDateTime fechaSolicitud = LocalDateTime.now();
    private LocalDateTime fechaDespacho;
    private LocalDateTime fechaEntrega;

    public PedidoReabastecimiento() {
    }

    public PedidoReabastecimiento(String id, String codigoPedido, String proveedorId, String proveedorNombre,
                                  String repuestoCodigo, String repuestoNombre, Integer cantidadSolicitada, Double precioUnitario) {
        this.id = id;
        this.codigoPedido = codigoPedido;
        this.proveedorId = proveedorId;
        this.proveedorNombre = proveedorNombre;
        this.repuestoCodigo = repuestoCodigo;
        this.repuestoNombre = repuestoNombre;
        this.cantidadSolicitada = cantidadSolicitada;
        this.precioUnitario = precioUnitario;
        this.costoTotal = (cantidadSolicitada != null && precioUnitario != null) ? cantidadSolicitada * precioUnitario : 0.0;
        this.estado = EstadoPedidoReabastecimiento.SOLICITADO;
        this.fechaSolicitud = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public String getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(String proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public String getRepuestoCodigo() {
        return repuestoCodigo;
    }

    public void setRepuestoCodigo(String repuestoCodigo) {
        this.repuestoCodigo = repuestoCodigo;
    }

    public String getRepuestoNombre() {
        return repuestoNombre;
    }

    public void setRepuestoNombre(String repuestoNombre) {
        this.repuestoNombre = repuestoNombre;
    }

    public Integer getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Integer cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
        if (cantidadSolicitada != null && precioUnitario != null) {
            this.costoTotal = cantidadSolicitada * precioUnitario;
        }
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
        if (cantidadSolicitada != null && precioUnitario != null) {
            this.costoTotal = cantidadSolicitada * precioUnitario;
        }
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public EstadoPedidoReabastecimiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedidoReabastecimiento estado) {
        this.estado = estado;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getObservacionesEntrega() {
        return observacionesEntrega;
    }

    public void setObservacionesEntrega(String observacionesEntrega) {
        this.observacionesEntrega = observacionesEntrega;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public LocalDateTime getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(LocalDateTime fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}
