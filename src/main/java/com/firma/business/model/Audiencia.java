package com.firma.business.model;


import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Audiencia {
    private Integer id;
    private String enlace;
    private String nombre;
    private Proceso proceso;
}
