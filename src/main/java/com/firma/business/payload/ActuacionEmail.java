package com.firma.business.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActuacionEmail {
    private Integer id;
    private String demandante;
    private String demandado;
    private String actuacion;
    private String radicado;
    private String anotacion;
    private String fechaActuacion;
    private String emailAbogado;
    private String nameAbogado;
    private String link;
}
