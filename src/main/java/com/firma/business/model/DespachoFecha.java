package com.firma.business.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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