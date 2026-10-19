package com.Taller_apirest.app.modules.ordenes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "cotizaciones")
public class Cotizacion {

    @Id
    private String id;
    private String codigoCotizacion;
    private String clienteId;
    private String clienteNombre;
    private String vehiculoId;
    private String placa;
    private List<String> serviciosSolicitados = new ArrayList<>();
    private Double totalEstimado = 0.0;
    private EstadoCotizacion estado = EstadoCotizacion.PENDIENTE;
    private String observaciones;
    private String respuestaCliente;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private LocalDateTime fechaRespuesta;

    public Cotizacion() {
    }

    public Cotizacion(String id, String codigoCotizacion, String clienteId, String clienteNombre, String vehiculoId, String placa, List<String> serviciosSolicitados, Double totalEstimado, String observaciones) {
        this.id = id;
        this.codigoCotizacion = codigoCotizacion;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.vehiculoId = vehiculoId;
        this.placa = placa;
        this.serviciosSolicitados = (serviciosSolicitados != null) ? serviciosSolicitados : new ArrayList<>();
        this.totalEstimado = totalEstimado;
        this.observaciones = observaciones;
        this.estado = EstadoCotizacion.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoCotizacion() {
        return codigoCotizacion;
    }

    public void setCodigoCotizacion(String codigoCotizacion) {
        this.codigoCotizacion = codigoCotizacion;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(String vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public List<String> getServiciosSolicitados() {
        return serviciosSolicitados;
    }

    public void setServiciosSolicitados(List<String> serviciosSolicitados) {
        this.serviciosSolicitados = serviciosSolicitados;
    }

    public Double getTotalEstimado() {
        return totalEstimado;
    }

    public void setTotalEstimado(Double totalEstimado) {
        this.totalEstimado = totalEstimado;
    }

    public EstadoCotizacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoCotizacion estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getRespuestaCliente() {
        return respuestaCliente;
    }

    public void setRespuestaCliente(String respuestaCliente) {
        this.respuestaCliente = respuestaCliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
}
