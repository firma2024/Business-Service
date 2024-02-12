package com.firma.business.model;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Firma {
    private Integer id;
    private String nombre;
    private String direccion;
}
