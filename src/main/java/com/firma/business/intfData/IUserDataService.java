package com.firma.business.intfData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.Rol;
import com.firma.business.model.TipoAbogado;
import com.firma.business.model.TipoDocumento;
import com.firma.business.model.Usuario;
import com.firma.business.payload.request.UserDataRequest;
import com.firma.business.payload.response.PageableUserResponse;


import java.util.List;

public interface IUserDataService {
    PageableUserResponse getAbogadosByFirmaFilter(List<String> especialidades, Integer firmaId, Integer rolId, Integer page, Integer size) throws ErrorDataServiceException;
    List<TipoDocumento> getAllTipoDocumento() throws ErrorDataServiceException;
    Rol getRoleByUser(String userName) throws ErrorDataServiceException;
    List<TipoAbogado> findAllTipoAbogado() throws ErrorDataServiceException;
    TipoDocumento findTipoDocumendoByName(String tipoDocumento)throws ErrorDataServiceException;
    Rol findRolByName(String name) throws ErrorDataServiceException;
    TipoAbogado findTipoAbogadoByName(String specialty)throws ErrorDataServiceException;
    String saveUser(UserDataRequest userDataRequest)throws ErrorDataServiceException;
    Usuario findUserById(Integer id)throws ErrorDataServiceException;
    String updateUser(Usuario user) throws ErrorDataServiceException;
    Integer getNumberAssignedProcesses(Integer id)throws ErrorDataServiceException;
    String deleteUser(Integer id) throws ErrorDataServiceException;
    Usuario findUserByUserName(String userName) throws ErrorDataServiceException;
    List<Usuario> findAllAbogadosNames(Integer firmaId) throws ErrorDataServiceException;
}
