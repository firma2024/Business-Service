package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.*;
import com.firma.business.payload.request.UserAbogadoUpdateRequest;
import com.firma.business.payload.request.UserJefeUpdateRequest;
import com.firma.business.payload.response.PageableResponse;
import com.firma.business.payload.request.UserRequest;
import com.firma.business.payload.response.PageableUserResponse;
import com.firma.business.payload.response.UserResponse;
import com.firma.business.payload.request.UserDataRequest;
import com.firma.business.service.data.intf.IUserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private IUserDataService userDataService;
    @Autowired
    private FirmaService firmaService;

    public String saveAbogado(UserRequest userRequest) throws ErrorDataServiceException {
        TipoDocumento typeDocument = userDataService.findTipoDocumendoByName(userRequest.getTipoDocumento());
        Rol role = userDataService.findRolByName("ABOGADO");
        Set<TipoAbogado> specialties = new HashSet<>();

        for (String specialty : userRequest.getEspecialidades()) {
            TipoAbogado typeLawyer = userDataService.findTipoAbogadoByName(specialty);
            specialties.add(typeLawyer);
        }

        Usuario newUser = Usuario.builder()
                .nombres(userRequest.getNombres())
                .correo(userRequest.getCorreo())
                .username(userRequest.getUsername())
                .telefono(userRequest.getTelefono())
                .identificacion(userRequest.getIdentificacion())
                .rol(role)
                .tipodocumento(typeDocument)
                .especialidadesAbogado(specialties)
                .eliminado('N')
                .build();

        Firma firma = firmaService.findFirmaById(userRequest.getFirmaId());
        Empleado newEmployee = Empleado.builder()
                .usuario(newUser)
                .firma(firma)
                .build();

        UserDataRequest userDataRequest = UserDataRequest.builder()
                .user(newUser)
                .employee(newEmployee)
                .build();

        return userDataService.saveUser(userDataRequest);
    }

    public String saveJefe(UserRequest userRequest) throws ErrorDataServiceException {
        TipoDocumento typeDocument = userDataService.findTipoDocumendoByName(userRequest.getTipoDocumento());
        Rol role = userDataService.findRolByName("JEFE");

        Usuario newUser = Usuario.builder()
                .nombres(userRequest.getNombres())
                .correo(userRequest.getCorreo())
                .username(userRequest.getUsername())
                .telefono(userRequest.getTelefono())
                .identificacion(userRequest.getIdentificacion())
                .rol(role)
                .tipodocumento(typeDocument)
                .eliminado('N')
                .build();

        Firma firma = firmaService.findFirmaById(userRequest.getFirmaId());
        Empleado newEmployee = Empleado.builder()
                .usuario(newUser)
                .firma(firma)
                .build();

        UserDataRequest userDataRequest = UserDataRequest.builder()
                .user(newUser)
                .employee(newEmployee)
                .build();

        return userDataService.saveUser(userDataRequest);
    }

    public String saveAdmin(UserRequest userRequest) throws ErrorDataServiceException {
        TipoDocumento typeDocument = userDataService.findTipoDocumendoByName(userRequest.getTipoDocumento());
        Rol role = userDataService.findRolByName("ADMIN");

        Usuario newUser = Usuario.builder()
                .nombres(userRequest.getNombres())
                .correo(userRequest.getCorreo())
                .username(userRequest.getUsername())
                .telefono(userRequest.getTelefono())
                .identificacion(userRequest.getIdentificacion())
                .rol(role)
                .tipodocumento(typeDocument)
                .eliminado('N')
                .build();

        UserDataRequest userDataRequest = UserDataRequest.builder()
                .user(newUser)
                .build();

        return userDataService.saveUser(userDataRequest);
    }

    public String updateInfoAbogado(UserAbogadoUpdateRequest userRequest) throws ErrorDataServiceException {
        Usuario user = userDataService.findUserById(userRequest.getId());
        Set<TipoAbogado> specialties = new HashSet<>();

        for (String specialty : userRequest.getEspecialidades()) {
            TipoAbogado typeLawyer = userDataService.findTipoAbogadoByName(specialty);
            specialties.add(typeLawyer);
        }

        user.setNombres(userRequest.getNombres());
        user.setCorreo(userRequest.getCorreo());
        user.setTelefono(userRequest.getTelefono());
        user.setIdentificacion(userRequest.getIdentificacion());
        user.setEspecialidadesAbogado(specialties);
        return userDataService.updateUser(user);
    }

    public String updateInfoJefe(UserJefeUpdateRequest userRequest) throws ErrorDataServiceException {
        Usuario user = userDataService.findUserById(userRequest.getId());

        user.setNombres(userRequest.getNombres());
        user.setCorreo(userRequest.getCorreo());
        user.setTelefono(userRequest.getTelefono());
        user.setIdentificacion(userRequest.getIdentificacion());
        return userDataService.updateUser(user);
    }

    public UserResponse getInfoJefe(String userName) throws ErrorDataServiceException {
        Usuario user = userDataService.findUserByUserName(userName);

        return UserResponse.builder()
                .id(user.getId())
                .nombres(user.getNombres())
                .correo(user.getCorreo())
                .telefono(user.getTelefono())
                .identificacion(user.getIdentificacion())
                .build();
    }

    public UserResponse getInfoAbogado(String userName) throws ErrorDataServiceException {
        Usuario user = userDataService.findUserByUserName(userName);
        List<String> especialidades = new ArrayList<>();

        for(TipoAbogado tipoAbogado : user.getEspecialidadesAbogado()){
            especialidades.add(tipoAbogado.getNombre());
        }

        return UserResponse.builder()
                .id(user.getId())
                .nombres(user.getNombres())
                .correo(user.getCorreo())
                .telefono(user.getTelefono())
                .identificacion(user.getIdentificacion())
                .especialidades(especialidades)
                .build();
    }

    public String deleteUser(Integer id) throws ErrorDataServiceException {
        Usuario user = userDataService.findUserById(id);
        if (user.getRol().getNombre().equals("ABOGADO")){
            Integer number = userDataService.getNumberAssignedProcesses(user.getId());
            if (number == null) {
                number = 0;
            }
            if (number != 0) {
                throw new ErrorDataServiceException(String.format("El abogado tiene %d procesos asignados", number));
            }
        }

        return userDataService.deleteUser(id);
    }

    public UserResponse getUserName(String userName) throws ErrorDataServiceException {
        Usuario user = userDataService.findUserByUserName(userName);
        return UserResponse.builder()
                .id(user.getId())
                .nombres(user.getNombres())
                .build();
    }

    public List<UserResponse> getAllAbogadosNames(Integer firmaId) throws ErrorDataServiceException {
        List<Usuario> users = userDataService.findAllAbogadosNames(firmaId);
        List<UserResponse> userResponse = new ArrayList<>();

        for (Usuario user : users) {
            userResponse.add(UserResponse.builder()
                    .id(user.getId())
                    .nombres(user.getNombres())
                    .build());
        }

        return userResponse;
    }

    public PageableResponse<UserResponse> getAbogadosByFirmaFilter(Integer numProcesosInicial, Integer numProcesosFinal, List<String> especialidades, Integer firmaId, Integer page, Integer size) throws ErrorDataServiceException {
        Rol rol = userDataService.findRolByName("ABOGADO");
        PageableUserResponse pageableResponse = userDataService.getAbogadosByFirmaFilter(numProcesosInicial, numProcesosFinal, especialidades, firmaId, rol.getId(), page, size);
        List<UserResponse> userResponse = new ArrayList<>();
        for (Usuario user : pageableResponse.getData()) {
            Integer number = userDataService.getNumberAssignedProcesses(user.getId());
            if (number == null) {
                number = 0;
            }
            List<String> especialidadesAbogado = new ArrayList<>();

            for(TipoAbogado tipoAbogado : user.getEspecialidadesAbogado()){
                especialidadesAbogado.add(tipoAbogado.getNombre());
            }
            if (number >= numProcesosInicial && number <= numProcesosFinal) {
                userResponse.add(UserResponse.builder()
                        .id(user.getId())
                        .nombres(user.getNombres())
                        .correo(user.getCorreo())
                        .telefono(user.getTelefono())
                        .especialidades(especialidadesAbogado)
                        .numeroProcesosAsignados(number)
                        .build());
            }
        }
        return PageableResponse.<UserResponse>builder()
                .data(userResponse)
                .currentPage(pageableResponse.getCurrentPage())
                .totalItems(pageableResponse.getTotalItems())
                .totalPages(pageableResponse.getTotalPages())
                .build();
    }

    public List<TipoDocumento> getAllTipoDocumento() throws ErrorDataServiceException {
        return userDataService.getAllTipoDocumento();
    }

    public Rol getRoleByUser(String username) throws ErrorDataServiceException {
        return userDataService.getRoleByUser(username);
    }

    public List<TipoAbogado> findAllTipoAbogado() throws ErrorDataServiceException {
        return userDataService.findAllTipoAbogado();
    }

    public Map<String, Object> getActiveAbogados(Integer firmaId) throws ErrorDataServiceException {
        Rol rol = userDataService.findRolByName("ABOGADO");
        PageableUserResponse pageableResponse = userDataService.getAbogadosByFirmaFilter(null, null, null, firmaId, rol.getId(), null, null);
        return Map.of("value", pageableResponse.getTotalItems());
    }
}
