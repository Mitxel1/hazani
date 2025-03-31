package utng.edu.mx.prueba.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import utng.edu.mx.prueba.model.*;
import utng.edu.mx.prueba.service.UsuarioService;


@RestController
@RequestMapping("API/v1/ENCRYPT/userServicio") // Define el prefijo para todas las rutas
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping(value = "/crearUsuarios", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuariosResponse> crearUser(@Valid @RequestBody UsuariosRequest request) {
        UsuariosResponse user = usuarioService.usuariosResponse(request);
        if(user != null && user.getCodigo()==0) {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/listarUsuarios", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<UserListResponse> listarUsuarios(
                @RequestParam(required = false) String username,
                @RequestParam(defaultValue = "0") Integer pagina,
                @RequestParam(defaultValue = "10") Integer tamañoPagina) {

            UserListResquest request = new UserListResquest();
            request.setUsername(username);
            request.setPagina(Long.valueOf(pagina));
            request.setTamañoPagina(Integer.valueOf(tamañoPagina));

            UserListResponse response = usuarioService.listUsers(request);
            if(response != null && response.getCodigo()==0) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    // Actualizar usuario username, password , estatus

    @PostMapping(value = "updateUsuarios",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody
    ResponseEntity<UsuariosResponse> updateUser(@Valid @RequestBody UpdateUserRequest request){
        UsuariosResponse user = new UsuariosResponse();
        user = usuarioService.updateUser(request);
        if (user == null && user.getCodigo()==0){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }

}
