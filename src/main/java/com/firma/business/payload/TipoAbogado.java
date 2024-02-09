package com.firma.business.payload;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TipoAbogado {
    private Integer id;
    private String nombre;
}