package com.firma.business.payload.request;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FindProcessRequest {
    private BigInteger number_process;
    private String date;
    private String file_number;
}
