package com.example.login_api.security;


import org.springframework.security.authentication.AbstractAuthenticationToken;


/*
* clase  del token de autenticación del usuario
* */
public class UserPrincipalAuthenticationToken extends AbstractAuthenticationToken {
    //se incluye el USerPrincipal que es el usuario autenticado
    private final UserPrincipal principal;

    //este constructor se usa al crear el token de autenticacion utilizando el UserPrincipal
    public UserPrincipalAuthenticationToken(UserPrincipal principal) {
        //se llama al constructor de la clase padre con las autoridades del usuario
        super(principal.getAuthorities());
        //se asigna el usuario principal al token
        this.principal = principal;
        //se decide que si hay token estas autenticado
        setAuthenticated(true);
    }
    // este metodo devuelve el usuario autenticado
    @Override
    public Object getCredentials() {
        return null;
    }
    // este método devuelve el usuario principal
    @Override
    public UserPrincipal getPrincipal() {
        return principal;
    }
}
