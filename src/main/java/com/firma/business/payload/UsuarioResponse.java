package com.firma.business.payload;


import lombok.*;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioResponse {

    private Integer id;
    private String nombres;
    private String correo;
    private BigInteger telefono;
    private BigInteger identificacion;
    private List<String> especialidades;
    private Integer numeroProcesosAsignados;
}