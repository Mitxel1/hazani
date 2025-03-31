package utng.edu.mx.prueba.service;

import utng.edu.mx.prueba.model.*;

public interface UsuarioService {
    UsuariosResponse usuariosResponse(UsuariosRequest request);
    UserListResponse listUsers(UserListResquest request);
    UsuariosResponse updateUser(UpdateUserRequest request);
}
