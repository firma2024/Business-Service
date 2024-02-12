package com.firma.business.payload.response;

import com.firma.business.model.Usuario;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageableUserResponse {
    private List<Usuario> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
