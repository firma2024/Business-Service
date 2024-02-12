package com.firma.business.payload.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageableResponse <T>{
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}