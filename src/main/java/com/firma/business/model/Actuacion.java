package com.firma.business.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Actuacion {
    private Integer id;
    private String actuacion;
    private String anotacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaactuacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecharegistro;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechainicia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechafinaliza;
    private Boolean existedoc;
    private byte[] documento;
    private Character enviado;
    private Proceso proceso;
    private EstadoActuacion estadoactuacion;
}
