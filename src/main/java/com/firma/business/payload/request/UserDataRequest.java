package com.firma.business.payload.request;

import com.firma.business.model.Empleado;
import com.firma.business.model.Usuario;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDataRequest {
    private Usuario user;
    private Empleado employee;
}
