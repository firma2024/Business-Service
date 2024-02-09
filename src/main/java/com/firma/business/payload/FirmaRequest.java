package com.firma.business.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FirmaRequest {
    private String nombre;
    private String direccion;
}
