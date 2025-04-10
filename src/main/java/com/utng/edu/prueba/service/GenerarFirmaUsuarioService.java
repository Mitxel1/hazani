package com.utng.edu.prueba.service;
import com.utng.edu.prueba.model.*;

public interface GenerarFirmaUsuarioService {
    String generarFirmaAltaUsuario(UsuariosRequest usuariosRequest);

    String generarFirmaActualizarUsuario(UpdateUserRequest updateUserRequest);

    String generarFirmaBajaUsuario(DeleteUserRequest deleteUserRequest);

    String generarFirmaListarUsuario(UserListRequest userListRequest);
}
