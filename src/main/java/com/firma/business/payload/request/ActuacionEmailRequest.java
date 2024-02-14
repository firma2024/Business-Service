package com.firma.business.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActuacionEmailRequest {
    private Integer id;
    private String actuacion;
    private String radicado;
    private String anotacion;
    private String fechaActuacion;
    private String emailAbogado;
    private String nameAbogado;
    private String link;
}
