package com.firma.business.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessJefeResponse {
    private Integer id;
    private String numeroRadicado;
    private String despacho;
    private String sujetos;
    private String tipoProceso;
    private String abogado;
    private String fechaRadicacion;
    private Boolean estadoVisto;
    private String estado;
}