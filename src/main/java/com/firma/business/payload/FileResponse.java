package com.firma.business.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileResponse {
    private byte[] file;
    private String fileName;
}
