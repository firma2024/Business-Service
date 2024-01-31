package com.firma.business.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessRequest {
    private String numeroRadicado;
    private Integer idFirma;
    private Integer idAbogado;
}
