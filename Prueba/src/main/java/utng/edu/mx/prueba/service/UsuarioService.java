package utng.edu.mx.prueba.service;

import utng.edu.mx.prueba.model.*;

public interface UsuarioService {
    UsuariosResponse usuariosResponse(UsuariosRequest usuarioRequest);
    UserListResponse listUsers(UserListResquest usuarioRequest);
    UsuariosResponse updateUser(UpdateUserRequest updateUserRequest);
}
