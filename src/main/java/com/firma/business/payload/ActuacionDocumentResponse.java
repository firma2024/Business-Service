package com.firma.business.payload;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActuacionDocumentResponse {
    private byte [] document;
    private String fechaActuacion;
    private String radicado;
}