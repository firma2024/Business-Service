package com.firma.business.service;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.*;
import com.firma.business.payload.request.UserAbogadoUpdateRequest;
import com.firma.business.payload.request.UserJefeUpdateRequest;
import com.firma.business.payload.request.UserRequest;
import com.firma.business.payload.response.MessageResponse;
import com.firma.business.payload.response.PageableResponse;
import com.firma.business.payload.response.PageableUserResponse;
import com.firma.business.payload.response.UserResponse;
import com.firma.business.intfData.IUserDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private IUserDataService userDataService;
    @Mock
    private FirmaService firmaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveAbogadoShouldReturnExpectedResult() throws ErrorDataServiceException {
        UserRequest userRequest = new UserRequest();
        userRequest.setTipoDocumento(new TipoDocumento(1, "CC"));
        userRequest.setNombres("John Doe");
        userRequest.setCorreo("john.doe@example.com");
        userRequest.setUsername("johndoe");
        userRequest.setTelefono(new BigInteger("1234567890"));
        userRequest.setIdentificacion(new BigInteger("123456789"));
        userRequest.setFirmaId(1);
        userRequest.setEspecialidades(Set.of(new TipoAbogado(1, "Familia")));

        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setNombre("CC");

        Rol rol = new Rol();
        rol.setNombre("ABOGADO");
        TipoAbogado tp = new TipoAbogado();
        tp.setNombre("Familia");

        Firma firma = new Firma(1, "test", "casa");
        Integer idReturn = 1;

        when(userDataService.findRolByName("ABOGADO")).thenReturn(rol);
        when(firmaService.findFirmaById(1)).thenReturn(firma);
        when(userDataService.saveUser(any())).thenReturn(idReturn);

        MessageResponse result = userService.saveAbogado(userRequest);

        assertEquals(1, result.getValue());
    }

    @Test
    void saveJefeShouldReturnExpectedResult() throws ErrorDataServiceException {
        UserRequest userRequest = new UserRequest();
        userRequest.setTipoDocumento(new TipoDocumento(1, "CC"));
        userRequest.setNombres("John Doe");
        userRequest.setCorreo("john.doe@example.com");
        userRequest.setUsername("johndoe");
        userRequest.setTelefono(new BigInteger("1234567890"));
        userRequest.setIdentificacion(new BigInteger("123456789"));
        userRequest.setFirmaId(1);
        Integer idReturn = 1;

        Rol rol = new Rol();
        rol.setNombre("JEFE");
        Firma firma = new Firma(1, "test", "casa");
        when(userDataService.findRolByName("JEFE")).thenReturn(rol);
        when(firmaService.findFirmaById(1)).thenReturn(firma);
        when(userDataService.saveUser(any())).thenReturn(idReturn);

        MessageResponse result = userService.saveJefe(userRequest);

        assertEquals(1, result.getValue());
    }

    @Test
    void saveAdminShouldReturnExpectectResult() throws ErrorDataServiceException {
        UserRequest userRequest = new UserRequest();
        userRequest.setTipoDocumento(new TipoDocumento(1, "CC"));
        userRequest.setNombres("John Doe");
        userRequest.setCorreo("john.doe@example.com");
        userRequest.setUsername("johndoe");
        userRequest.setTelefono(new BigInteger("1234567890"));
        userRequest.setIdentificacion(new BigInteger("123456789"));
        Integer idReturn = 1;

        Rol rol = new Rol(1, "ADMIN");
        when(userDataService.findRolByName("ADMIN")).thenReturn(rol);
        when(userDataService.saveUser(any())).thenReturn(idReturn);
        MessageResponse result = userService.saveAdmin(userRequest);

        assertEquals(1, result.getValue());

    }

    @Test
    void shouldUpdateInfoAbogadoSuccessfully() throws ErrorDataServiceException {
        UserAbogadoUpdateRequest userRequest = new UserAbogadoUpdateRequest();
        userRequest.setId(1);
        userRequest.setNombres("John Doe");
        userRequest.setCorreo("john.doe@example.com");
        userRequest.setTelefono(new BigInteger("1234567890"));
        userRequest.setIdentificacion(new BigInteger("123456789"));
        userRequest.setEspecialidades(Set.of(new TipoAbogado(1, "Familia")));

        TipoAbogado tipoAbogado = new TipoAbogado(1, "Familia");
        Usuario user = new Usuario();
        when(userDataService.findUserById(userRequest.getId())).thenReturn(user);
        when(userDataService.findTipoAbogadoByName("Familia")).thenReturn(tipoAbogado);
        when(userDataService.updateUser(user)).thenReturn("User updated successfully");

        MessageResponse response = userService.updateInfoAbogado(userRequest);

        assertEquals("User updated successfully", response.getMessage());

    }

    @Test
    void shouldUpdateInfoJefeSuccessfully() throws ErrorDataServiceException {
        UserJefeUpdateRequest userRequest = new UserJefeUpdateRequest();
        userRequest.setId(1);
        userRequest.setNombres("John Doe");
        userRequest.setCorreo("john.doe@example.com");
        userRequest.setTelefono(new BigInteger("1234567890"));
        userRequest.setIdentificacion(new BigInteger("123456789"));

        Usuario user = new Usuario();
        when(userDataService.findUserById(userRequest.getId())).thenReturn(user);
        when(userDataService.updateUser(user)).thenReturn("User updated successfully");

        MessageResponse response = userService.updateInfoJefe(userRequest);

        assertEquals("User updated successfully", response.getMessage());
    }

    @Test
    void shouldGetInfoJefeSuccessfully() throws ErrorDataServiceException {
        String username = "test";
        Usuario user = new Usuario();
        user.setId(1);
        when(userDataService.findUserByUserName(username)).thenReturn(user);
        UserResponse userResponse = userService.getInfoJefe(username);
        assertEquals(userResponse.getId(), user.getId());
    }

    @Test
    void shouldGetInfoAbogadoSuccessfully() throws ErrorDataServiceException {
        String username = "test";
        Usuario user = new Usuario();
        user.setId(1);
        user.setEspecialidadesAbogado(Set.of(new TipoAbogado(1, "Familia")));
        when(userDataService.findUserByUserName(username)).thenReturn(user);

        UserResponse userResponse = userService.getInfoAbogado(username);

        assertEquals(user.getEspecialidadesAbogado().size(), userResponse.getEspecialidades().size());

    }

    @Test
    void shouldDeleteUserJefeSuccessfully() throws ErrorDataServiceException {
        Integer id = 1;

        Usuario user = new Usuario();
        user.setRol(new Rol(1, "JEFE"));

        when(userDataService.findUserById(id)).thenReturn(user);
        when(userDataService.deleteUser(id)).thenReturn("User deleted");

        MessageResponse response = userService.deleteUser(id);

        assertEquals("User deleted", response.getMessage());
    }

    @Test
    void shouldDeleteUserAdminSuccessfully() throws ErrorDataServiceException {
        Integer id = 1;

        Usuario user = new Usuario();
        user.setRol(new Rol(1, "ADMIN"));

        when(userDataService.findUserById(id)).thenReturn(user);
        when(userDataService.deleteUser(id)).thenReturn("User deleted");

        MessageResponse response = userService.deleteUser(id);

        assertEquals("User deleted", response.getMessage());
    }

    @Test
    void shouldDeleteUserAbogadoSuccessfully() throws ErrorDataServiceException {
        Integer id = 1;

        Usuario user = new Usuario();
        user.setRol(new Rol(1, "ABOGADO"));

        when(userDataService.findUserById(id)).thenReturn(user);
        when(userDataService.getNumberAssignedProcesses(id)).thenReturn(null);
        when(userDataService.deleteUser(id)).thenReturn("User deleted");

        MessageResponse response = userService.deleteUser(id);

        assertEquals("User deleted", response.getMessage());
    }

    @Test
    void shouldDeleteUserAbogadoNoSuccessfully() {
        Integer id = 1;
        Integer numberProcesses = 3;

        Usuario user = new Usuario();
        user.setId(1);
        user.setRol(new Rol(1, "ABOGADO"));

        try {
            when(userDataService.findUserById(id)).thenReturn(user);
            when(userDataService.getNumberAssignedProcesses(id)).thenReturn(numberProcesses);
            when(userDataService.deleteUser(id)).thenReturn("User deleted");
            userService.deleteUser(id);

        } catch (ErrorDataServiceException e) {
            assertEquals("El abogado tiene 3 procesos asignados", e.getMessage());
        }
    }

    @Test
    void shouldGetUserNameSuccessfully() throws ErrorDataServiceException {
        String username = "test";
        Usuario user = Usuario.builder()
                .id(1)
                .nombres("Daniel Barreto")
                .build();

        when(userDataService.findUserByUserName("test")).thenReturn(user);
        UserResponse userResponse = userService.getUserName(username);
        assertEquals(userResponse.getNombres(), user.getNombres());
    }

    @Test
    void shouldGetAllAbogadosNamesSuccesfully() throws ErrorDataServiceException {
        Usuario user1 = Usuario.builder()
                .id(1)
                .nombres("Daniel Barreto")
                .build();
        Usuario user2 = Usuario.builder()
                .id(2)
                .nombres("Juan Paez")
                .build();
        List<Usuario> users = List.of(user2, user1);
        Integer firmaId = 1;

        when(userDataService.findAllAbogadosNames(firmaId)).thenReturn(users);

        List<UserResponse> userResponseList = userService.getAllAbogadosNames(firmaId);

        assertEquals(userResponseList.size(), users.size());
    }

    @Test
    void shouldgetAllTipoDocumentoSuccessfully() throws ErrorDataServiceException {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setNombre("CC");
        when(userDataService.getAllTipoDocumento()).thenReturn(List.of(tipoDocumento));
        List<TipoDocumento> tipoDocumentos = userService.getAllTipoDocumento();
        assertEquals(tipoDocumentos.size(), 1);
    }

    @Test
    void shouldGetRolByUserSuccessfully() throws ErrorDataServiceException {
        String username = "test";
        Rol role = new Rol(1, "ABOGADO");

        when(userDataService.getRoleByUser(username)).thenReturn(role);
        Rol roleReponse = userService.getRoleByUser(username);

        assertEquals(role.getNombre(), roleReponse.getNombre());
    }

    @Test
    void shouldgetAllTipoAbpgadoSuccessfully() throws ErrorDataServiceException {
        TipoAbogado tipoAbogado = new TipoAbogado();
        tipoAbogado.setNombre("Familia");
        when(userDataService.findAllTipoAbogado()).thenReturn(List.of(tipoAbogado));
        List<TipoAbogado> tipoAbogados = userService.findAllTipoAbogado();
        assertEquals(tipoAbogados.size(), 1);
    }

}