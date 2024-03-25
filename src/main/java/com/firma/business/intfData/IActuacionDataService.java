package com.firma.business.intfData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.EstadoActuacion;
import com.firma.business.model.Actuacion;
import com.firma.business.model.RegistroCorreo;
import com.firma.business.payload.response.PageableActuacionResponse;

import java.util.List;
import java.util.Set;

public interface IActuacionDataService {
    String updateActuacion(Actuacion actuacion) throws ErrorDataServiceException;
    String saveRegistroCorreo(RegistroCorreo estadoCorreo) throws ErrorDataServiceException;
    String saveActuaciones(List<Actuacion> actuaciones) throws ErrorDataServiceException;
    Set<Actuacion> findActuacionesNotSend() throws ErrorDataServiceException;
    String updateActuacionesSend(List<Integer> actuaciones) throws ErrorDataServiceException;
    Actuacion getActuacion(Integer id) throws ErrorDataServiceException;
    PageableActuacionResponse getActuacionesFilter(Integer procesoId, String fechaInicioStr, String fechaFinStr, String estadoActuacion, Integer page, Integer size) throws ErrorDataServiceException;
    PageableActuacionResponse getActuacionesByProcesoAbogado(Integer procesoId, String fechaInicioStr, String fechaFinStr, Boolean existeDoc, Integer page, Integer size) throws ErrorDataServiceException;
    EstadoActuacion findEstadoActuacionByName(String state) throws ErrorDataServiceException;
    Actuacion findLastActuacion(Integer processid) throws ErrorDataServiceException;
    List<Actuacion> findByNoVisto(Integer procesoId) throws ErrorDataServiceException;
}
