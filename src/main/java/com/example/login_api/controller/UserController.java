package com.example.login_api.controller;


import com.example.login_api.dto.ProfileResponse;
import com.example.login_api.dto.UpdateProfileRequest;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.security.UserPrincipal;
import com.example.login_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;



/*
* Permite a los usuarios gestionar el propio perfil.
		Endpoints:
		GET /user/profile → Ver su perfil
		PUT /user/profile → Actualizar su  perfil
		DELETE /user/profile → Eliminar  su cuenta
* */


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Bienvenido al Dashboard de Usuario");
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    // Ver perfil del usuario
     @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
            // Obtiene los datos del usuario autenticado
            String fn = principal.getFirstName();
            String ln = principal.getLastName();
            String email = principal.getEmail();
            String phone = principal.getPhone();
            if (fn == null || fn.isEmpty()) fn ="Sin nombre";
            if (ln == null || ln.isEmpty()) ln = "Sin apellido";
            if(email == null || email.isEmpty()) email = "Sin correo";
            if(phone == null || phone.isEmpty()) phone = "Sin teléfono";

            // Crea la respuesta con los datos del usuario

          ProfileResponse response = new ProfileResponse(
                  fn.trim(),
                  ln.trim(),
                  email.trim(),
                  phone.trim()
            );
          return ResponseEntity.ok(response);
    }

    //actualizar perfil del usuario
    @PutMapping("/profile")
    public ResponseEntity<UserEntity> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                    @RequestBody UpdateProfileRequest req) {
        UserEntity updated = userService.updateProfile(principal.getEmail(), req);
        return ResponseEntity.ok(updated);
    }

    /*Eliminar uenta propia*/
    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteProfile(Principal principal) {
        String email = principal.getName();
        return userService.deleteProfile(email);
    }
}
