package com.firma.business.payload.request;

import com.firma.business.model.Rol;
import com.firma.business.model.TipoAbogado;
import com.firma.business.model.TipoDocumento;
import lombok.*;

import java.math.BigInteger;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {
    private Integer id;
    private String nombres;
    private String correo;
    private BigInteger telefono;
    private BigInteger identificacion;
    private String username;
    private TipoDocumento tipoDocumento;
    private Set<TipoAbogado> especialidades;
    private Integer firmaId;
}