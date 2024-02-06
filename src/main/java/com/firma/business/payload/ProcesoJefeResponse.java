package com.firma.business.payload;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcesoJefeResponse {
    private Integer id;
    private String numeroRadicado;
    private String despacho;
    private String demandante;
    private String demandado;
    private String tipoProceso;
    private String abogado;
    private String fechaRadicacion;
    private Boolean estadoVisto;
    private String estado;
}