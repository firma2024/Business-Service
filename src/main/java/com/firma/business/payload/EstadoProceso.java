package com.firma.business.payload;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EstadoProceso {
    private Integer id;
    private String nombre;
}