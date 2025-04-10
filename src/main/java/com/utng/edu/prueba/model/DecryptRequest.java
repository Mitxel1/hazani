package com.utng.edu.prueba.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DecryptRequest {
        @NonNull
        @NotBlank
        private String decrypt;

}

