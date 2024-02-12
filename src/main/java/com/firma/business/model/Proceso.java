package com.firma.business.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Proceso {
    private Integer id;
    private String radicado;
    private BigInteger numeroproceso;
    private String sujetos;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecharadicado;
    private String ubicacionexpediente;
    private Character eliminado;
    private Despacho despacho;
    private TipoProceso tipoproceso;
    private EstadoProceso estadoproceso;
    private Empleado empleado;
    private Firma firma;
}
