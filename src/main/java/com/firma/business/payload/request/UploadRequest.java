package com.firma.business.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UploadRequest {
    private byte[] file;
    private Integer id;
}