package org.propertybiddingsystem.usermanagementsystem.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.propertybiddingsystem.usermanagementsystem.Enums.Role;

public record RegisterRequest(@NotBlank  String username, @NotBlank @Email String email , @NotBlank String password , @NotBlank String confirmPassword, @NotBlank Role role ) {
}
