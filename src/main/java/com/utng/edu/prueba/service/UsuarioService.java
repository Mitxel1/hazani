package com.utng.edu.prueba.service;

import com.utng.edu.prueba.model.*;

public interface UsuarioService {
    UsuariosResponse crearUsuario(UsuariosRequest usuariosRequest);
    UserListResponse listarUsers(UserListRequest usuarioRequest);
    UsuariosResponse updateUsuario(UpdateUserRequest updateUserRequest);
    UsuariosResponse eliminarUsuario(DeleteUserRequest deleteUserRequest);

    // Métodos agregados
    AuthResponse login(LoginRequest request);
    OtpResponse verificarOtp(OtpVerifyRequest request);
}
