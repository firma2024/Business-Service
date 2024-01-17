package com.firma.business.payload;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Despacho {
    private Integer id;
    private String nombre;
}