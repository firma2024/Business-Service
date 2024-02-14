package com.firma.business.payload.request;

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
