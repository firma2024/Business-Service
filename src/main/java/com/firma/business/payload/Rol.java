package com.firma.business.payload;


import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rol {
    private Integer id;
    private String nombre;
}
