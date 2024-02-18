package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.*;
import com.firma.business.model.Audiencia;
import com.firma.business.payload.request.ProcessDataRequest;
import com.firma.business.payload.response.PageableProcessResponse;
import com.firma.business.payload.response.PageableResponse;
import com.firma.business.payload.response.ProcessAbogadoResponse;

import java.util.List;
import java.util.Set;

public interface IProcessDataService {
    Set<Proceso> getProcess() throws ErrorDataServiceException;
    String saveProcess(ProcessDataRequest process) throws ErrorDataServiceException;
    PageableProcessResponse getProcessByFilter(String fechaInicioStr, Integer firmaId, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException;
    PageableProcessResponse getProcessByAbogado(Integer abogadoId, String fechaInicioStr, String fechaFinStr, List<String> estadosProceso, String tipoProceso, Integer page, Integer size) throws ErrorDataServiceException;
    List<Proceso> getStateProcessesJefe(String state, Integer firmaId) throws ErrorDataServiceException;
    Proceso getProcessById(Integer processId) throws ErrorDataServiceException;
    String deleteProcess(Integer processId) throws ErrorDataServiceException;
    String updateProcess(Proceso process) throws ErrorDataServiceException;
    List<Proceso> getStateProcessesAbogado(String name, String userName) throws ErrorDataServiceException;
    ProcessAbogadoResponse getProcessAbogado(Integer processId) throws ErrorDataServiceException;
    List<EstadoProceso> getEstadoProcesos() throws ErrorDataServiceException;
    String updateAudiencia(Integer id, String enlace) throws ErrorDataServiceException;
    String addAudiencia(Audiencia audiencia) throws ErrorDataServiceException;
    Set<Despacho> findAllDespachosWithOutLink(Integer year) throws ErrorDataServiceException;
    List<TipoProceso> getTipoProcesos() throws ErrorDataServiceException;
    String saveEnlace(Enlace enlaceRequest) throws ErrorDataServiceException;
    TipoProceso findTipoProcesoByNombre(String tipoProceso) throws ErrorDataServiceException;
    Despacho findDespachoByNombre(String despacho) throws ErrorDataServiceException;
    EstadoProceso findEstadoProcesoByNombre(String name) throws ErrorDataServiceException;
    Set<Audiencia> findAllAudienciasByProceso(Integer processId) throws ErrorDataServiceException;
    Despacho findDespachoById(Integer despachoid) throws ErrorDataServiceException;
    Proceso findByRadicado(String radicado) throws ErrorDataServiceException;
    Enlace findByDespachoAndYear(Integer id, String year) throws ErrorDataServiceException;
    Set<DespachoFecha> findAllDespachosWithDateActuacion() throws ErrorDataServiceException;
}
