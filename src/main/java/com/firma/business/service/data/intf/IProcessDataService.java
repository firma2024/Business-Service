package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.exception.ErrorIntegrationServiceException;
import com.firma.business.payload.*;

import java.util.List;
import java.util.Set;

public interface IProcessDataService {
    List<ProcesoResponse> getProcess() throws ErrorDataServiceException;
    String saveProcess(Proceso process) throws ErrorDataServiceException;
    PageableResponse<Proceso> getProcessByFilter(String fechaInicioStr, Integer firmaId, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException;
    PageableResponse<Proceso> getProcessByAbogado(Integer abogadoId, String fechaInicioStr, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException;
    List<ProcesoResponse> getStateProcessesJefe(String state, Integer firmaId) throws ErrorDataServiceException;
    ProcesoJefeResponse getProcessById(Integer processId) throws ErrorDataServiceException;
    String deleteProcess(Integer processId) throws ErrorDataServiceException;
    String updateProcess(Proceso process) throws ErrorDataServiceException;
    List<ProcesoResponse> getStateProcessesAbogado(String name, String userName) throws ErrorDataServiceException;
    ProcesoAbogadoResponse getProcessAbogado(Integer processId) throws ErrorDataServiceException;
    List<EstadoProceso> getEstadoProcesos() throws ErrorDataServiceException;
    String updateAudiencia(Integer id, String enlace) throws ErrorDataServiceException;
    String addAudiencia(AudienciaRequest audiencia) throws ErrorDataServiceException;
    Set<Despacho> findAllDespachosWithOutLink(Integer year) throws ErrorDataServiceException;
    List<TipoProceso> getTipoProcesos() throws ErrorDataServiceException;
    String saveEnlace(EnlaceRequest enlaceRequest) throws ErrorDataServiceException;

}
