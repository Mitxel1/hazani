package utng.edu.mx.prueba.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import utng.edu.mx.prueba.entity.empresa.Usuarios;
import utng.edu.mx.prueba.model.*;
import utng.edu.mx.prueba.repositories.empresa.UsuariosRepositories;
import utng.edu.mx.prueba.service.EncryptService;
import utng.edu.mx.prueba.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Service
public class UsuarioServiceimpl implements UsuarioService {

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private UsuariosRepositories usuariosRepositories;

    @Override
    public UsuariosResponse usuariosResponse(UsuariosRequest usuarioRequest) {
        Usuarios user = new Usuarios();
        UsuariosResponse userResponse = new UsuariosResponse();
        user.setUsername(usuarioRequest.getUsername());
        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest.setEncrypt(usuarioRequest.getPassword());
        EncryptResponse encryptResponse=encryptService.encryptCadena(encryptRequest);

        if (encryptResponse != null && encryptResponse.getEncrypt() != null) {
            user.setPassword(encryptResponse.getEncrypt());
        }
        user.setEstatus(true);
        usuariosRepositories.save(user);

        userResponse.setCodigo(0);
        userResponse.setMensaje("Exito");
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public UserListResponse listUsers(UserListResquest usuariosListRequest) {
            UserListResponse userResponse = new UserListResponse();
        String username = usuariosListRequest.getUsername() == null ? "" : usuariosListRequest.getUsername();
        try {
            Pageable pageable = PageRequest.of(Math.toIntExact(usuariosListRequest.getPagina()),
                    Math.toIntExact(usuariosListRequest.getTamañoPagina()),Sort.by("username"));
            Page<Usuarios> userList = usuariosRepositories.findByUsernameContainingIgnoreCase(username,  pageable);
            if(userList.isEmpty()&&userList!=null) {
                userResponse.setUsuarios(userList.getContent());
                userResponse.setTotalElementos(userList.getTotalElements());
                userResponse.setTotalPaginas(userList.getTotalPages());
                return userResponse;
            } else {
                userResponse.setMensaje("no existen registros");
                userResponse.setCodigo(1);
                userResponse.setUsuarios(userList.getContent());
                userResponse.setTotalElementos(userList.getTotalElements());
                userResponse.setTotalPaginas(userList.getTotalPages());
                return userResponse;
            }
        } catch (Exception e) {
            userResponse.setMensaje("Error al listar el usuario");
            userResponse.setCodigo(1);
            log.error("Ha ocurrido un error al consultar la informacion: " + e.getMessage());
            return userResponse;
        }
    }

    @Override
    public UsuariosResponse updateUser(UpdateUserRequest updateUserRequest) {
        UsuariosResponse userResponse = new UsuariosResponse();
        try {
            Optional<Usuarios> usuarioExistente = usuariosRepositories.findByUsername(updateUserRequest.getUsername());

            if (usuarioExistente.isPresent()) {
                Usuarios user = usuarioExistente.get();

                // Verificar si el nuevo username ya está en uso
                Optional<Usuarios> usuarioConNuevoUsername = usuariosRepositories.findByUsername(updateUserRequest.getNewUsername());
                if (usuarioConNuevoUsername.isPresent()) {
                    userResponse.setCodigo(1);
                    userResponse.setMensaje("El nuevo username ya está en uso");
                    return userResponse;
                }

                // Actualizar los datos y guardar
                user.setUsername(updateUserRequest.getNewUsername());
                BeanUtils.copyProperties(updateUserRequest, user); // Evitar sobreescribir
                usuariosRepositories.save(user);

                userResponse.setCodigo(0);
                userResponse.setMensaje("Datos actualizados correctamente");
                BeanUtils.copyProperties( user, userResponse);
                return userResponse;
            } else {
                userResponse.setCodigo(1);
                userResponse.setMensaje("El username no existe: " + updateUserRequest.getUsername());
                return userResponse;
            }
        } catch (Exception e) {
            userResponse.setCodigo(1);
            userResponse.setMensaje("Error al actualizar el usuario");
            log.error("Error al actualizar la información: {}", e.getMessage());
            return userResponse;
        }
    }

}

