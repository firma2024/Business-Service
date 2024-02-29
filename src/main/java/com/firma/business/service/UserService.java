package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.*;
import com.firma.business.payload.request.UserAbogadoUpdateRequest;
import com.firma.business.payload.request.UserJefeUpdateRequest;
import com.firma.business.payload.response.MessageResponse;
import com.firma.business.payload.response.PageableResponse;
import com.firma.business.payload.request.UserRequest;
import com.firma.business.payload.response.PageableUserResponse;
import com.firma.business.payload.response.UserResponse;
import com.firma.business.payload.request.UserDataRequest;
import com.firma.business.intfData.IUserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private IUserDataService userDataService;
    @Autowired
    private FirmaService firmaService;
    @Value("${api.rol.abogado}")
    private String rolAbogado;
    @Value("${api.rol.jefe}")
    private String rolJefe;
    @Value("${api.rol.admin}")
    private String rolAdmin;


    public MessageResponse saveAbogado(UserRequest userRequest) throws ErrorDataServiceException {
        Set<TipoAbogado> specialties = new HashSet<>();
        Rol rol = userDataService.findRolByName(rolAbogado);
        Usuario newUser = Usuario.builder()
                .nombres(userRequest.getNombres())
                .correo(userRequest.getCorreo())
                .username(userRequest.getUsername())
                .telefono(userRequest.getTelefono())
                .identificacion(userRequest.getIdentificacion())
                .rol(rol)
                .tipodocumento(userRequest.getTipoDocumento())
                .especialidadesAbogado(userRequest.getEspecialidades())
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

        return new MessageResponse(userDataService.saveUser(userDataRequest));
    }

    public MessageResponse saveJefe(UserRequest userRequest) throws ErrorDataServiceException {
        Rol rol = userDataService.findRolByName(rolAbogado);
        Usuario newUser = Usuario.builder()
                .nombres(userRequest.getNombres())
                .correo(userRequest.getCorreo())
                .username(userRequest.getUsername())
                .telefono(userRequest.getTelefono())
                .identificacion(userRequest.getIdentificacion())
                .rol(rol)
                .tipodocumento(userRequest.getTipoDocumento())
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

        return new MessageResponse( userDataService.saveUser(userDataRequest));
    }

    public MessageResponse saveAdmin(UserRequest userRequest) throws ErrorDataServiceException {
        Rol rol = userDataService.findRolByName(rolAdmin);
        Usuario newUser = Usuario.builder()
                .nombres(userRequest.getNombres())
                .correo(userRequest.getCorreo())
                .username(userRequest.getUsername())
                .telefono(userRequest.getTelefono())
                .identificacion(userRequest.getIdentificacion())
                .rol(rol)
                .tipodocumento(userRequest.getTipoDocumento())
                .eliminado('N')
                .build();

        UserDataRequest userDataRequest = UserDataRequest.builder()
                .user(newUser)
                .build();

        return new MessageResponse(userDataService.saveUser(userDataRequest));
    }

    public MessageResponse updateInfoAbogado(UserAbogadoUpdateRequest userRequest) throws ErrorDataServiceException {
        Usuario user = userDataService.findUserById(userRequest.getId());
        user.setNombres(userRequest.getNombres());
        user.setCorreo(userRequest.getCorreo());
        user.setTelefono(userRequest.getTelefono());
        user.setIdentificacion(userRequest.getIdentificacion());
        user.setEspecialidadesAbogado(userRequest.getEspecialidades());
        return new MessageResponse(userDataService.updateUser(user));
    }

    public MessageResponse updateInfoJefe(UserJefeUpdateRequest userRequest) throws ErrorDataServiceException {
        Usuario user = userDataService.findUserById(userRequest.getId());

        user.setNombres(userRequest.getNombres());
        user.setCorreo(userRequest.getCorreo());
        user.setTelefono(userRequest.getTelefono());
        user.setIdentificacion(userRequest.getIdentificacion());
        return new MessageResponse(userDataService.updateUser(user));
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

        return UserResponse.builder()
                .id(user.getId())
                .nombres(user.getNombres())
                .correo(user.getCorreo())
                .telefono(user.getTelefono())
                .identificacion(user.getIdentificacion())
                .especialidades(user.getEspecialidadesAbogado())
                .build();
    }

    public MessageResponse deleteUser(Integer id) throws ErrorDataServiceException {
        Usuario user = userDataService.findUserById(id);
        if (user.getRol().getNombre().equals(rolAbogado)){
            Integer number = userDataService.getNumberAssignedProcesses(user.getId());
            if (number == null) {
                number = 0;
            }
            if (number != 0) {
                throw new ErrorDataServiceException(String.format("El abogado tiene %d procesos asignados", number), 400);
            }
        }

        return new MessageResponse(userDataService.deleteUser(id));
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
        Rol rol = userDataService.findRolByName(rolAbogado);
        PageableUserResponse pageableResponse = userDataService.getAbogadosByFirmaFilter(especialidades, firmaId, rol.getId(), page, size);
        List<UserResponse> userResponse = new ArrayList<>();
        for (Usuario user : pageableResponse.getData()) {
            Integer number = userDataService.getNumberAssignedProcesses(user.getId());
            if (number == null) {
                number = 0;
            }

            if (number >= numProcesosInicial && number <= numProcesosFinal) {
                userResponse.add(UserResponse.builder()
                        .id(user.getId())
                        .nombres(user.getNombres())
                        .correo(user.getCorreo())
                        .telefono(user.getTelefono())
                        .especialidades(user.getEspecialidadesAbogado())
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

    public Map<String, Long> getActiveAbogados(Integer firmaId) throws ErrorDataServiceException {
        Rol rol = userDataService.findRolByName(rolAbogado);
        PageableUserResponse pageableResponse = userDataService.getAbogadosByFirmaFilter( null, firmaId, rol.getId(), null, null);
        return Map.of("value", pageableResponse.getTotalItems());
    }

    public MessageResponse checkInsertUser(UserRequest userRequest) throws ErrorDataServiceException {
        return new MessageResponse(userDataService.checkInsertUser(userRequest));
    }
}
