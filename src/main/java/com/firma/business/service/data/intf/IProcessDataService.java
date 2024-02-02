package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.PageableResponse;
import com.firma.business.payload.Proceso;
import com.firma.business.payload.ProcesoResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProcessDataService {
    List<ProcesoResponse> getProcess() throws ErrorDataServiceException;

    String saveProcess(Proceso process) throws ErrorDataServiceException;

    PageableResponse<Proceso> getProcessByFilter(String fechaInicioStr, Integer firmaId, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException;

    PageableResponse<Proceso> getProcessByAbogado(Integer abogadoId, String fechaInicioStr, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException;
}
