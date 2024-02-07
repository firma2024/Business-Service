package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.payload.PageableResponse;
import com.firma.business.payload.UsuarioRequest;
import com.firma.business.payload.UsuarioResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUsuarioDataService {
    String uploadPhoto(MultipartFile file, Integer usuarioId) throws ErrorDataServiceException, IOException;
    byte[] downloadPhoto(Integer usuarioId) throws ErrorDataServiceException;
    PageableResponse<UsuarioResponse> getAbogadosByFirma(Integer firmaId, Integer page, Integer size) throws ErrorDataServiceException;
    PageableResponse<UsuarioResponse> getAbogadosByFilter(List<String> especialidades, Integer numProcesosInicial, Integer numProcesosFinal, Integer page, Integer size) throws ErrorDataServiceException;
    String saveAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException;
    UsuarioResponse getAbogado(Integer abogadoId) throws ErrorDataServiceException;
    List<UsuarioResponse> getAbogadosNames(Integer firmaId) throws ErrorDataServiceException;
    String deleteUser(Integer userId) throws ErrorDataServiceException;
    UsuarioResponse getInfoJefe(Integer id) throws ErrorDataServiceException;
    String updateInfoJefe(UsuarioRequest userRequest, Integer id) throws ErrorDataServiceException;
    UsuarioResponse getUserByUserName(String userName) throws ErrorDataServiceException;
    UsuarioResponse getInfoAbogado(Integer id) throws ErrorDataServiceException;

    String updateInfoAbogado(UsuarioRequest userRequest) throws ErrorDataServiceException;
}
