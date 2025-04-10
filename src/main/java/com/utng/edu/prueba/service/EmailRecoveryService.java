package com.utng.edu.prueba.service;

import com.utng.edu.prueba.model.*;

public interface EmailRecoveryService {
    RecoveryResponse handleRecoveryRequest(RecoveryRequest request);
    RecoveryResponse resetPassword(ResetPasswordRequest request);
}