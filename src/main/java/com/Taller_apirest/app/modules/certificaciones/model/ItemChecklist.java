package com.Taller_apirest.app.modules.certificaciones.model;

public class ItemChecklist {

    private String norma;
    private String descripcion;
    private boolean cumplido = true;
    private String evidencia;

    public ItemChecklist() {
    }

    public ItemChecklist(String norma, String descripcion, boolean cumplido, String evidencia) {
        this.norma = norma;
        this.descripcion = descripcion;
        this.cumplido = cumplido;
        this.evidencia = evidencia;
    }

    public String getNorma() {
        return norma;
    }

    public void setNorma(String norma) {
        this.norma = norma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCumplido() {
        return cumplido;
    }

    public void setCumplido(boolean cumplido) {
        this.cumplido = cumplido;
    }

    public String getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;
    }
}
