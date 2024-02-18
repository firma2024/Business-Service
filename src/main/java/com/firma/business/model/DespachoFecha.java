package com.firma.business.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DespachoFecha {
    private String nombre;
    private Integer despachoId;
    private Integer year;

    public DespachoFecha(Integer year, String nombre, Integer despachoId) {
        this.year = year;
        this.nombre = nombre;
        this.despachoId = despachoId;
    }
}