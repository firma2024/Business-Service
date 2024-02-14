package com.firma.business.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessBusinessRequest {
    private String numeroRadicado;
    private Integer idAbogado;
}
