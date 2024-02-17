package com.firma.business.payload.request;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessRequest {
    private Integer id;
    private BigInteger idProceso;
    private String numeroRadicado;
    private String despacho;
    private String sujetos;
    private String fechaRadicacion;
    private List<ActuacionRequest> actuaciones;
    private String tipoProceso;
    private String ubicacionExpediente;
    private Integer idAbogado;
    private String estadoProceso;
}
