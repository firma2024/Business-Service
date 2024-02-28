package com.firma.business.payload.response;


import com.firma.business.model.TipoAbogado;
import lombok.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {

    private Integer id;
    private String nombres;
    private String correo;
    private BigInteger telefono;
    private BigInteger identificacion;
    private Set<TipoAbogado> especialidades;
    private Integer numeroProcesosAsignados;
}