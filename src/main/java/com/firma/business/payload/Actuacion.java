package com.firma.business.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Actuacion {
    private Integer id;
    private String anotacion;
    private String actuacion;
    private String radicado;
    private String demandante;
    private String demandado;
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
}
