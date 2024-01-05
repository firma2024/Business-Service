package com.firma.business.payload;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FindProcess {
    private BigInteger number_process;
    private String date;
    private String file_number;
}
