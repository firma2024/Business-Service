package com.firma.business.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActuacionRequest {
    private String nombreActuacion;
    private String anotacion;
    private String fechaActuacion;
    private String fechaRegistro;
    private String proceso;
    private boolean existDocument;
}