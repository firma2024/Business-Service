package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.*;

import java.util.List;

public interface IUserDataService {
    String saveAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException;
    String saveJefe(UsuarioRequest userRequest) throws ErrorDataServiceException;
    String saveAdmin(UsuarioRequest userRequest) throws ErrorDataServiceException;
    UsuarioResponse gerInfoJefe(Integer id) throws ErrorDataServiceException;
    String updateInfoJefe(UsuarioRequest userRequest) throws ErrorDataServiceException;
    String updateInfoAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException;
    String deleteUser(Integer id) throws ErrorDataServiceException;
    UsuarioResponse getUserName(String userName) throws ErrorDataServiceException;
    List<UsuarioResponse> getAllAbogadosNames(Integer firmaId) throws ErrorDataServiceException;
    PageableResponse<UsuarioResponse> getAbogadosFilter(Integer numProcesosInicial, Integer numProcesosFinal, List<String> especialidades, Integer firmaId, Integer page, Integer size) throws ErrorDataServiceException;
    UsuarioResponse getAbogado(Integer usuarioId) throws ErrorDataServiceException;
    List<TipoDocumento> getAllTipoDocumento() throws ErrorDataServiceException;
    Rol getRoleByUser(String userName) throws ErrorDataServiceException;
    TipoDocumento getTipoDocumento(String name) throws ErrorDataServiceException;
    List<TipoAbogado> findAllTipzoAbogado() throws ErrorDataServiceException;
}
