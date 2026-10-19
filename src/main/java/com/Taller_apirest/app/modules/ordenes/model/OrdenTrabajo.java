package com.Taller_apirest.app.modules.ordenes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "ordenes_trabajo")
public class OrdenTrabajo {

    @Id
    private String id;
    private String numeroOrden;
    private String cotizacionId;
    private String clienteId;
    private String clienteNombre;
    private String vehiculoId;
    private String vehiculoPlaca;
    private String vehiculoMarcaModelo;
    private Integer kilometrajeIngreso;

    private String mecanicoId;
    private String mecanicoNombre;

    private EstadoOrden estado = EstadoOrden.ASIGNADA;
    private String motivoIngreso;
    private String diagnosticoTecnico;
    private String reparacionesEfectuadas;

    private List<RepuestoUtilizado> repuestosUtilizados = new ArrayList<>();
    private List<RegistroTiempo> registrosTiempo = new ArrayList<>();

    private Double costoManoObra = 0.0;
    private Double costoRepuestos = 0.0;
    private Double costoTotal = 0.0;

    private LocalDateTime fechaIngreso = LocalDateTime.now();
    private LocalDateTime fechaFinalizacion;

    public OrdenTrabajo() {
    }

    public OrdenTrabajo(String id, String numeroOrden, String cotizacionId, String clienteId, String clienteNombre,
                        String vehiculoId, String vehiculoPlaca, String vehiculoMarcaModelo, Integer kilometrajeIngreso,
                        String mecanicoId, String mecanicoNombre, String motivoIngreso) {
        this.id = id;
        this.numeroOrden = numeroOrden;
        this.cotizacionId = cotizacionId;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.vehiculoId = vehiculoId;
        this.vehiculoPlaca = vehiculoPlaca;
        this.vehiculoMarcaModelo = vehiculoMarcaModelo;
        this.kilometrajeIngreso = kilometrajeIngreso;
        this.mecanicoId = mecanicoId;
        this.mecanicoNombre = mecanicoNombre;
        this.motivoIngreso = motivoIngreso;
        this.estado = EstadoOrden.ASIGNADA;
        this.fechaIngreso = LocalDateTime.now();
    }

    public void recalcularCostos(double tarifaHoraManoObra) {
        // Calcular horas totales
        double horasTotales = 0.0;
        if (registrosTiempo != null) {
            for (RegistroTiempo reg : registrosTiempo) {
                horasTotales += reg.getTotalHorasDecimales();
            }
        }
        this.costoManoObra = Math.round(horasTotales * tarifaHoraManoObra * 100.0) / 100.0;

        // Calcular repuestos totales
        double repuestosTotal = 0.0;
        if (repuestosUtilizados != null) {
            for (RepuestoUtilizado r : repuestosUtilizados) {
                repuestosTotal += (r.getSubtotal() != null ? r.getSubtotal() : 0.0);
            }
        }
        this.costoRepuestos = Math.round(repuestosTotal * 100.0) / 100.0;
        this.costoTotal = this.costoManoObra + this.costoRepuestos;
    }

    public double getHorasTotales() {
        double horas = 0.0;
        if (registrosTiempo != null) {
            for (RegistroTiempo r : registrosTiempo) {
                horas += r.getTotalHorasDecimales();
            }
        }
        return Math.round(horas * 10.0) / 10.0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getCotizacionId() {
        return cotizacionId;
    }

    public void setCotizacionId(String cotizacionId) {
        this.cotizacionId = cotizacionId;
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

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public String getVehiculoMarcaModelo() {
        return vehiculoMarcaModelo;
    }

    public void setVehiculoMarcaModelo(String vehiculoMarcaModelo) {
        this.vehiculoMarcaModelo = vehiculoMarcaModelo;
    }

    public Integer getKilometrajeIngreso() {
        return kilometrajeIngreso;
    }

    public void setKilometrajeIngreso(Integer kilometrajeIngreso) {
        this.kilometrajeIngreso = kilometrajeIngreso;
    }

    public String getMecanicoId() {
        return mecanicoId;
    }

    public void setMecanicoId(String mecanicoId) {
        this.mecanicoId = mecanicoId;
    }

    public String getMecanicoNombre() {
        return mecanicoNombre;
    }

    public void setMecanicoNombre(String mecanicoNombre) {
        this.mecanicoNombre = mecanicoNombre;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public String getMotivoIngreso() {
        return motivoIngreso;
    }

    public void setMotivoIngreso(String motivoIngreso) {
        this.motivoIngreso = motivoIngreso;
    }

    public String getDiagnosticoTecnico() {
        return diagnosticoTecnico;
    }

    public void setDiagnosticoTecnico(String diagnosticoTecnico) {
        this.diagnosticoTecnico = diagnosticoTecnico;
    }

    public String getReparacionesEfectuadas() {
        return reparacionesEfectuadas;
    }

    public void setReparacionesEfectuadas(String reparacionesEfectuadas) {
        this.reparacionesEfectuadas = reparacionesEfectuadas;
    }

    public List<RepuestoUtilizado> getRepuestosUtilizados() {
        return repuestosUtilizados;
    }

    public void setRepuestosUtilizados(List<RepuestoUtilizado> repuestosUtilizados) {
        this.repuestosUtilizados = repuestosUtilizados;
    }

    public List<RegistroTiempo> getRegistrosTiempo() {
        return registrosTiempo;
    }

    public void setRegistrosTiempo(List<RegistroTiempo> registrosTiempo) {
        this.registrosTiempo = registrosTiempo;
    }

    public Double getCostoManoObra() {
        return costoManoObra;
    }

    public void setCostoManoObra(Double costoManoObra) {
        this.costoManoObra = costoManoObra;
    }

    public Double getCostoRepuestos() {
        return costoRepuestos;
    }

    public void setCostoRepuestos(Double costoRepuestos) {
        this.costoRepuestos = costoRepuestos;
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
}
