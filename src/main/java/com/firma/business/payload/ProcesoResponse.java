package com.firma.business.payload;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcesoResponse {
    private Integer id;
    private String numeroRadicado;
    private BigInteger numeroProceso;
    private String tipoProceso;
    private String fechaRadicacion;
    private String fechaUltimaActuacion;
}