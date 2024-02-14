package com.firma.business.payload.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessAbogadoResponse {
    private Integer id;
    private String numeroRadicado;
    private String despacho;
    private String sujetos;
    private String tipoProceso;
    private String fechaRadicacion;
    private String estado;
    private List<AudienciaResponse> audiencias;
}