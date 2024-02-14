package com.firma.business.payload.response;

import com.firma.business.model.Proceso;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageableProcessResponse {
    private List<Proceso> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
