package com.firma.business.payload.request;

import com.firma.business.model.Proceso;
import com.firma.business.model.Actuacion;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessDataRequest {
    private Proceso process;
    List<Actuacion> actions;
}
