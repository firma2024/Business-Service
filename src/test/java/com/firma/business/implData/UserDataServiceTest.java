package com.firma.business.implData;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.implData.UserDataService;
import com.firma.business.model.Usuario;
import com.firma.business.payload.request.UserDataRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UserDataServiceTest {

    @InjectMocks
    UserDataService userDataService;

    @Mock
    RestTemplate restTemplate;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserSuccessfully() throws ErrorDataServiceException {
        UserDataRequest userRequest = new UserDataRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDataRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<Integer> responseEntity = ResponseEntity.ok(1);

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), eq(requestEntity), eq(Integer.class)))
                .thenReturn(responseEntity);

        Integer result = userDataService.saveUser(userRequest);

        assertEquals(1, result);
    }


    @Test
    void updateUserSuccessfully() throws ErrorDataServiceException {
        Usuario userRequest = new Usuario();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> responseEntity = ResponseEntity.ok("User updated successfully");

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PUT), eq(requestEntity), eq(String.class)))
                .thenReturn(responseEntity);

        String result = userDataService.updateUser(userRequest);

        assertEquals("User updated successfully", result);
    }


    @Test
    void getNumberAssignedProcessesReturnsExpectedValue() throws ErrorDataServiceException {
        Integer id = 1;
        Integer expectedProcesses = 5;

        when(restTemplate.getForEntity(any(String.class), eq(Integer.class)))
                .thenReturn(new ResponseEntity<>(expectedProcesses, HttpStatus.OK));

        Integer result = userDataService.getNumberAssignedProcesses(id);

        assertEquals(expectedProcesses, result);
    }


    @Test
    void deleteUserReturnsExpectedValue() throws ErrorDataServiceException {
        Integer id = 1;
        String expectedMessage = "User deleted successfully";

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.DELETE), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(expectedMessage, HttpStatus.OK));

        String result = userDataService.deleteUser(id);

        assertEquals(expectedMessage, result);
    }


    @Test
    void findUserByUserNameReturnsExpectedValue() throws ErrorDataServiceException {
        String userName = "testUser";
        Usuario expectedUser = new Usuario();
        expectedUser.setUsername(userName);

        when(restTemplate.getForEntity(any(String.class), eq(Usuario.class)))
                .thenReturn(new ResponseEntity<>(expectedUser, HttpStatus.OK));

        Usuario result = userDataService.findUserByUserName(userName);

        assertEquals(expectedUser, result);
    }

}