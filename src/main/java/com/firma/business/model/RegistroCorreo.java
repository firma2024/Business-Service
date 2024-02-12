package com.firma.business.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistroCorreo {
    private Integer id;
    private String correo;
    private LocalDateTime fecha;

}
