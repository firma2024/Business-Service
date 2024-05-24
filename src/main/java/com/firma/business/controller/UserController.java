package com.firma.business.controller;

import com.firma.business.exception.ErrorDataServiceException;
import com.firma.business.model.TipoAbogado;
import com.firma.business.model.TipoDocumento;
import com.firma.business.payload.request.UserAbogadoUpdateRequest;
import com.firma.business.payload.request.UserJefeUpdateRequest;
import com.firma.business.payload.request.UserRequest;
import com.firma.business.payload.response.MessageResponse;
import com.firma.business.payload.response.PageableResponse;
import com.firma.business.payload.response.UserResponse;
import com.firma.business.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/business/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Verificar insercion de usuario", description = "Verifica que el usuario no tenga campos ya existentes")
    @ApiResponse(responseCode = "200", description = "No hay un usuario con esos campos")
    @ApiResponse(responseCode = "409", description = "Conficto con alguno de los campos")
    @PostMapping("/check/insert")
    public ResponseEntity<?> checkInsertUser(@RequestBody UserRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.checkInsertUser(userRequest), HttpStatus.OK);
        } catch (ErrorDataServiceException e) {
            if (e.getStatusCode() == 409){
                return new ResponseEntity<>(new MessageResponse(e.getMessage(),  null), HttpStatus.CONFLICT);
            }
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(),  null));
        }
    }

    @Operation(summary = "Agregar Abogado", description = "Guarda la información del abogado.")
    @ApiResponse(responseCode = "201", description = "Abogado guardado correctamente")
    @ApiResponse(responseCode = "400", description = "Error al guardar el abogado")
    @PostMapping("/add/abogado")
    public ResponseEntity<?> addAbogado(@RequestBody UserRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.saveAbogado(userRequest), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Agregar Jefe", description = "Guarda la información del jefe.")
    @ApiResponse(responseCode = "201", description = "Jefe guardado correctamente")
    @ApiResponse(responseCode = "400", description = "Error al guardar el jefe")
    @PostMapping("/add/jefe")
    public ResponseEntity<?> addJefe(@RequestBody UserRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.saveJefe(userRequest), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Agregar Admin", description = "Guarda la información del administrador.")
    @ApiResponse(responseCode = "201", description = "Admin guardado correctamente")
    @ApiResponse(responseCode = "400", description = "Error al guardar el admin")
    @PostMapping("/add/admin")
    public ResponseEntity<?> addAdmin(@RequestBody UserRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.saveAdmin(userRequest), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Obtener información del jefe", description = "Obtiene la información del jefe dado el nombre de usuario los campos numeroProcesosAsignados y especialidades [] no son tomado en cuenta")
    @Parameter(name = "userName", description = "Nombre de usuario", required = true)
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener la información del jefe")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JEFE')")
    @GetMapping("/get/info/jefe")
    public ResponseEntity<?> getPersonalInfo(@RequestParam String userName) {
        try {
            return new ResponseEntity<>(userService.getInfoJefe(userName), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Actualizar información del abogado", description = "Actualiza la información del abogado dado el nombre de usuario")
    @ApiResponse(responseCode = "200", description = "Abogado actualizado")
    @ApiResponse(responseCode = "400", description = "Error al actualizar el abogado")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ABOGADO')")
    @PutMapping("/update/info/abogado")
    public ResponseEntity<?> updatePersonalInfoAbogado(@RequestBody UserAbogadoUpdateRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.updateInfoAbogado(userRequest), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Actualizar información del jefe", description = "Actualiza la información del jefe dado el nombre de usuario")
    @ApiResponse(responseCode = "200", description = "Jefe actualizado")
    @ApiResponse(responseCode = "400", description = "Error al actualizar el jefe")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JEFE')")
    @PutMapping("/update/info/jefe")
    public ResponseEntity<?> updatePersonalInfoJefe(@RequestBody UserJefeUpdateRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.updateInfoJefe(userRequest), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario dado su id")
    @Parameter(name = "id", description = "Id del usuario", required = true)
    @ApiResponse(responseCode = "200", description = "Usuario eliminado")
    @ApiResponse(responseCode = "400", description = "Error al eliminar el usuario")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JEFE')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Integer id) {
        try {
            return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Obtener nombres de usuarios", description = "Obtiene los nombres de un usuario dado su username")
    @Parameter(name = "name", description = "Nombre de usuario", required = true)
    @ApiResponse(responseCode = "200", description = "solo se retornan los atributos id y nombres" ,content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener el nombre de usuario")
    @GetMapping("/get/name")
    public ResponseEntity<?> getUserName(@RequestParam String name) {
        try {
            return new ResponseEntity<>(userService.getUserName(name), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Obtener nombres de todos los abogados", description = "Obtiene los nombres de todos los abogados dado el id de una firma")
    @Parameter(name = "firmaId", description = "Id de la firma", required = true)
    @ApiResponse(responseCode = "200",description = "Se retorna una Lista de la entidad con los atributos id y nombres", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener los nombres de los abogados")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JEFE')")
    @GetMapping("/get/all/names/abogados")
    public ResponseEntity<?> getAllAbogadosNames(@RequestParam Integer firmaId) {
        try {
            return new ResponseEntity<>(userService.getAllAbogadosNames(firmaId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Filtrar abogados", description = "Lista los abogados dado el id de la firma teniendo en cuenta los filtros dados")
    @Parameter(name = "especialidades", description = "Lista de especialidades", required = false)
    @Parameter(name = "firmaId", description = "Id de la firma", required = true)
    @Parameter(name = "numProcesosInicial", description = "Rango inical de numero de procesos valor default 0", required = false)
    @Parameter(name = "numProcesosFinal", description = "Rango final de numero de procesos valor default 5", required = false)
    @Parameter(name = "page", description = "Numero de pagina valor default 0", required = false)
    @Parameter(name = "size", description = "Tamaño de la pagina valor default 10", required = false)
    @ApiResponse(responseCode = "200", description = "Retorna paginados los datos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageableResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener los abogados")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JEFE')")
    @GetMapping("/jefe/abogados/filter")
    public ResponseEntity<?> getAbogadosFilter(@RequestParam(required = false) List<String> especialidades,
                                               @RequestParam Integer firmaId,
                                               @RequestParam(defaultValue = "0") Integer numProcesosInicial,
                                               @RequestParam(defaultValue = "1000") Integer numProcesosFinal,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size) {
        try {
            return new ResponseEntity<>(userService.getAbogadosByFirmaFilter(numProcesosInicial, numProcesosFinal, especialidades, firmaId, page, size), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Obtener abogado", description = "Obtiene la información del abogado dado el nombre de usuario")
    @Parameter(name = "userName", description = "Nombre de usuario", required = true)
    @ApiResponse(responseCode = "200", description = "Retorna valores de usuario", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener la información del abogado")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ABOGADO')")
    @GetMapping("/get/info/abogado")
    public ResponseEntity<?> getInfoAbogado(@RequestParam String userName) {
        try {
            return new ResponseEntity<>(userService.getInfoAbogado(userName), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }
    @Operation(summary = "Obtener abogado por id", description = "Obtiene la información del abogado dado el id")
    @Parameter(name = "id", description = "id del usuario", required = true)
    @ApiResponse(responseCode = "200", description = "Retorna valores de usuario", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener la información del abogado")
    @GetMapping("/get/abogado")
    public ResponseEntity<?> getAbogado(@RequestParam Integer id) {
        try {
            return new ResponseEntity<>(userService.getAbogado(id), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }



    @Operation(summary = "Obtener abogados activos", description = "Obtiene los abogados activos dado el id de la firma")
    @Parameter(name = "firmaId", description = "Id de la firma", required = true)
    @ApiResponse(responseCode = "200", description = "Retorna el numero de abogados activos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener los abogados activos")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JEFE')")
    @GetMapping("/get/active/abogados")
    public ResponseEntity<?> getActiveAbogados(@RequestParam Integer firmaId) {
        try {
            return new ResponseEntity<>(userService.getActiveAbogados(firmaId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Obtener todos los tipos de documento", description = "Obtiene todos los tipos de documentos")
    @ApiResponse(responseCode = "200", description = "Retorna una lista con todos los tipos de documentos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TipoDocumento.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener los tipos de documentos")
    @GetMapping("/tipoDocumento/get/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JEFE')")
    public ResponseEntity<?> getAllTipoDocumento() {
        try {
            return new ResponseEntity<>(userService.getAllTipoDocumento(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @Operation(summary = "Obtener todos los tipos de abogado", description = "Obtiene todos los tipos de abogados")
    @ApiResponse(responseCode = "200", description = "Retorna una lista con todos los tipos de abogados", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TipoAbogado.class))})
    @ApiResponse(responseCode = "400", description = "Error al obtener los tipos de abogados")
    @GetMapping("/tipoAbogado/get/all")
    public ResponseEntity<?> getAllTipoAbogado() {
        try {
            return new ResponseEntity<>(userService.findAllTipoAbogado(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage() , null));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        System.out.println("Test :)");
        return new ResponseEntity<>("Test", HttpStatus.OK);
    }
}
