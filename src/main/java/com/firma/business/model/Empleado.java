package com.firma.business.model;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Empleado {
    private Integer id;
    private Usuario usuario;
    private Firma firma;
}
