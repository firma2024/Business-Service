package com.firma.business.payload;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcesoAbogadoResponse {
    private Integer id;
    private String numeroRadicado;
    private String despacho;
    private String demandante;
    private String demandado;
    private String tipoProceso;
    private String fechaRadicacion;
    private String estado;
    private Set<Audiencia> audiencias;
}