package com.firma.business.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActuacionJefeResponse {
    private String nombreActuacion;
    private String anotacion;
    private String fechaActuacion;
    private String fechaRegistro;
    private boolean existDocument;
    private String estado;
}