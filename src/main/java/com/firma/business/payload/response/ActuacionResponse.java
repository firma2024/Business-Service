package com.firma.business.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActuacionResponse {
    private Integer id;
    private String anotacion;
    private String actuacion;
    private String radicado;
    private String sujetos;
    private String emailAbogado;
    private String fechaActuacion;
    private String fechaRegistro;
    private String fechaInicia;
    private String fechaFinaliza;
    private boolean existeDocumento;
    private String tipoProceso;
    private String despacho;
    private String nameAbogado;
    private String link;
    private String estado;
}