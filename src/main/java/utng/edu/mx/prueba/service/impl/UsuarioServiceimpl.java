package utng.edu.mx.prueba.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import utng.edu.mx.prueba.entity.empresa.Usuarios;
import utng.edu.mx.prueba.model.*;
import utng.edu.mx.prueba.repositories.empresa.UsuariosRepositories;
import utng.edu.mx.prueba.service.EncryptService;
import utng.edu.mx.prueba.service.KeystoreService;
import utng.edu.mx.prueba.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import utng.edu.mx.prueba.service.cryptoService;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuarioServiceimpl implements UsuarioService {

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private UsuariosRepositories usuariosRepositories;

    @Autowired
    private KeystoreService keystoreService;

    @Autowired
    private cryptoService crytoService;

    @Override
    public UsuariosResponse usuariosResponse(UsuariosRequest request) {
        UsuariosResponse response = new UsuariosResponse();
        try {
            // Validar el rol proporcionado
            if (!isValidRole(request.getRole())) {
                response.setCodigo("1");
                response.setMensaje("Error: Rol no válido");
                return response;
            }

            // Crear y guardar usuario
            Usuarios usuario = new Usuarios();
            usuario.setUsername(request.getUsername());
            usuario.setPassword(encodeBase64(request.getPassword()));
            usuario.setEstatus(Boolean.valueOf(request.getEstatus()));
            usuario.setRole(request.getRole().toUpperCase()); // Normalizar a mayúsculas

            usuario = usuariosRepositories.save(usuario);

            // Preparar respuesta
            response.setId(usuario.getId());
            response.setUsername(usuario.getUsername());
            response.setEstatus(usuario.getEstatus());
            response.setRole(usuario.getRole());
            response.setCodigo("0");
            response.setMensaje("Usuario creado exitosamente");

        } catch (Exception e) {
            log.error("Error creando usuario: ", e);
            response.setCodigo("1");
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }

    private boolean isValidRole(String role) {
        // Lista de roles permitidos (puedes modificarla según tus necesidades)
        Set<String> validRoles = Set.of("ADMIN", "USER");
        return role != null && validRoles.contains(role.toUpperCase());
    }


    private String encodeBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public UserListResponse listUsers(UserListResquest request) {
        UserListResponse response = new UserListResponse();
        try {
            int pagina = (int) request.getPagina();
            Pageable pageable = PageRequest.of(pagina, request.getTamañoPagina());

            Page<Usuarios> usuarios;
            if (request.getUsername() != null && !request.getUsername().isEmpty()) {
                usuarios = usuariosRepositories.findByUsernameContainingIgnoreCase(
                        request.getUsername(), pageable);
            } else {
                usuarios = usuariosRepositories.findAll(pageable);
            }

            List<UsuariosResponse> usuariosResponseList = usuarios.getContent().stream()
                    .map(user -> {
                        UsuariosResponse userResponse = new UsuariosResponse();
                        BeanUtils.copyProperties(user, userResponse);
                        return userResponse;
                    })
                    .collect(Collectors.toList());

            response.setUsuarios(usuariosResponseList);
            response.setTotalElementos(usuarios.getTotalElements());
            response.setTotalPaginas(usuarios.getTotalPages());
            response.setPaginaActual(usuarios.getNumber());
            response.setCodigo("0");
            response.setMensaje("Éxito");

        } catch (Exception e) {
            log.error("Error listando usuarios: ", e);
            response.setCodigo("1");
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public UsuariosResponse updateUser(UpdateUserRequest request) {
        UsuariosResponse response = new UsuariosResponse();
        try {
            Optional<Usuarios> usuarioOpt = usuariosRepositories.findById(request.getId());

            if (usuarioOpt.isPresent()) {
                Usuarios usuario = usuarioOpt.get();

                if (request.getUsername() != null) {
                    usuario.setUsername(request.getUsername());

                    DecryptRequest decryptRequest = new DecryptRequest();
                    DecryptResponse decryptResponse = encryptService.decryptCadena(decryptRequest);

                }

                if (request.getPassword() != null) {
                    EncryptRequest encryptRequest = new EncryptRequest();
                    encryptRequest.setEncrypt(request.getPassword());
                    EncryptResponse encryptResponse = encryptService.encryptCadena(encryptRequest);
                    usuario.setPassword(encryptResponse.getEncrypt());
                }

                if (request.getEstatus() != null) {
                    usuario.setEstatus(request.getEstatus());
                }

                usuariosRepositories.save(usuario);
                BeanUtils.copyProperties(usuario, response);
                response.setCodigo("0");
                response.setMensaje("Usuario actualizado exitosamente");
            } else {
                response.setCodigo("1");
                response.setMensaje("Usuario no encontrado");
            }
        } catch (Exception e) {
            log.error("Error actualizando usuario: ", e);
            response.setCodigo("1");
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

}

