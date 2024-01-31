package com.firma.business.service.data.intf;

import com.firma.business.exception.ErrorDataServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUsuarioDataService {
    String uploadPhoto(MultipartFile file, Integer usuarioId) throws ErrorDataServiceException, IOException;
    byte[] downloadPhoto(Integer usuarioId) throws ErrorDataServiceException;
}
