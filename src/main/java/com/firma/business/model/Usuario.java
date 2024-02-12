package com.firma.business.model;

import lombok.*;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    private Integer id;
    private String username;
    private String nombres;
    private BigInteger telefono;
    private BigInteger identificacion;
    private String correo;
    private Character eliminado;
    private byte[] img;
    private Rol rol;
    private TipoDocumento tipodocumento;
    private Set<TipoAbogado> especialidadesAbogado = new HashSet<>();
}
