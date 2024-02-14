package com.firma.business.model;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TipoDocumento {
    private Integer id;
    private String nombre;
}
