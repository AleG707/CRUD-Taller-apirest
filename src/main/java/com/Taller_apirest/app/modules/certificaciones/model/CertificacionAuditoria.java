package com.Taller_apirest.app.modules.certificaciones.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "certificaciones_auditoria")
public class CertificacionAuditoria {

    @Id
    private String id;
    private String codigoCertificado;
    private String ordenTrabajoId;
    private String numeroOrden;
    private String vehiculoPlaca;
    private String mecanicoNombre;
    private String auditorId;
    private String auditorNombre;

    private LocalDateTime fechaCertificacion = LocalDateTime.now();
    private ResultadoAuditoria resultado = ResultadoAuditoria.APROBADO;
    private Integer puntajeCalidad = 100;

    private List<ItemChecklist> checklist = new ArrayList<>();
    private String observacionesAuditor;
    private String hashTrazabilidad;

    public CertificacionAuditoria() {
    }

    public CertificacionAuditoria(String id, String codigoCertificado, String ordenTrabajoId, String numeroOrden,
                                  String vehiculoPlaca, String mecanicoNombre, String auditorId, String auditorNombre,
                                  ResultadoAuditoria resultado, Integer puntajeCalidad, String observacionesAuditor, String hashTrazabilidad) {
        this.id = id;
        this.codigoCertificado = codigoCertificado;
        this.ordenTrabajoId = ordenTrabajoId;
        this.numeroOrden = numeroOrden;
        this.vehiculoPlaca = vehiculoPlaca;
        this.mecanicoNombre = mecanicoNombre;
        this.auditorId = auditorId;
        this.auditorNombre = auditorNombre;
        this.resultado = resultado;
        this.puntajeCalidad = puntajeCalidad;
        this.observacionesAuditor = observacionesAuditor;
        this.hashTrazabilidad = hashTrazabilidad;
        this.fechaCertificacion = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoCertificado() {
        return codigoCertificado;
    }

    public void setCodigoCertificado(String codigoCertificado) {
        this.codigoCertificado = codigoCertificado;
    }

    public String getOrdenTrabajoId() {
        return ordenTrabajoId;
    }

    public void setOrdenTrabajoId(String ordenTrabajoId) {
        this.ordenTrabajoId = ordenTrabajoId;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public String getMecanicoNombre() {
        return mecanicoNombre;
    }

    public void setMecanicoNombre(String mecanicoNombre) {
        this.mecanicoNombre = mecanicoNombre;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorNombre() {
        return auditorNombre;
    }

    public void setAuditorNombre(String auditorNombre) {
        this.auditorNombre = auditorNombre;
    }

    public LocalDateTime getFechaCertificacion() {
        return fechaCertificacion;
    }

    public void setFechaCertificacion(LocalDateTime fechaCertificacion) {
        this.fechaCertificacion = fechaCertificacion;
    }

    public ResultadoAuditoria getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoAuditoria resultado) {
        this.resultado = resultado;
    }

    public Integer getPuntajeCalidad() {
        return puntajeCalidad;
    }

    public void setPuntajeCalidad(Integer puntajeCalidad) {
        this.puntajeCalidad = puntajeCalidad;
    }

    public List<ItemChecklist> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<ItemChecklist> checklist) {
        this.checklist = checklist;
    }

    public String getObservacionesAuditor() {
        return observacionesAuditor;
    }

    public void setObservacionesAuditor(String observacionesAuditor) {
        this.observacionesAuditor = observacionesAuditor;
    }

    public String getHashTrazabilidad() {
        return hashTrazabilidad;
    }

    public void setHashTrazabilidad(String hashTrazabilidad) {
        this.hashTrazabilidad = hashTrazabilidad;
    }
}
