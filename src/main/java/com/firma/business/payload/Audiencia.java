package com.firma.business.payload;

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
}