package com.firma.business.payload.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AudienciaResponse {
    private Integer id;
    private String enlace;
    private String nombre;
}
