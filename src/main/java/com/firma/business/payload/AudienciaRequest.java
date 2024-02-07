package com.firma.business.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AudienciaRequest {
    private String enlace;
    private Integer procesoid;
    private String nombre;
}