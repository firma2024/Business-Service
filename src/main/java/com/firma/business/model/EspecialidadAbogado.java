package com.firma.business.model;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EspecialidadAbogado {
    private Integer id;
    private Usuario usuario;
    private TipoAbogado tipoAbogado;
}
