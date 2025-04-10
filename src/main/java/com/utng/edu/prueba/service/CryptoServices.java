package com.utng.edu.prueba.service;

import com.utng.edu.prueba.model.*;

public interface CryptoServices {
    Boolean validiacionCadenaOriginalAltaUser(UsuariosRequest usuariosRequest);
    Boolean validacionCadenaOriginalActualizarUser(UpdateUserRequest updateUserRequest);
    Boolean validacionCadenaOriginalBajaUser(DeleteUserRequest deleteUserRequest);
    Boolean validacionCadenaOriginalListUser(UserListRequest userListRequest);
    Boolean validacionCadenaOriginalLogin(LoginRequest loginRequest);
    Boolean validacionCadenaOriginalOtp(OtpVerifyRequest otpVerifyRequest);
}