package com.Taller_apirest.app.modules.ordenes.model;

import java.time.LocalDateTime;

public class RegistroTiempo {

    private String mecanicoNombre;
    private Integer horas = 0;
    private Integer minutos = 0;
    private String descripcionTarea;
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    public RegistroTiempo() {
    }

    public RegistroTiempo(String mecanicoNombre, Integer horas, Integer minutos, String descripcionTarea) {
        this.mecanicoNombre = mecanicoNombre;
        this.horas = horas;
        this.minutos = minutos;
        this.descripcionTarea = descripcionTarea;
        this.fechaRegistro = LocalDateTime.now();
    }

    public String getMecanicoNombre() {
        return mecanicoNombre;
    }

    public void setMecanicoNombre(String mecanicoNombre) {
        this.mecanicoNombre = mecanicoNombre;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    public String getDescripcionTarea() {
        return descripcionTarea;
    }

    public void setDescripcionTarea(String descripcionTarea) {
        this.descripcionTarea = descripcionTarea;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public double getTotalHorasDecimales() {
        double h = (horas != null) ? horas : 0;
        double m = (minutos != null) ? minutos / 60.0 : 0;
        return h + m;
    }
}
