package com.sistema.notas.config.security;

import com.sistema.notas.entity.enums.Role;
import com.sistema.notas.entity.security.User;
import com.sistema.notas.respository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea un usuario ADMIN inicial UNICAMENTE si la tabla users esta vacia.
 * Esto permite hacer el primer login sin necesidad de scripts SQL manuales.
 *
 * Las credenciales se toman de application.properties (con defaults para dev):
 *   app.security.bootstrap.admin-email
 *   app.security.bootstrap.admin-password
 *
 * En produccion DEBE configurar estas variables (ADMIN_EMAIL / ADMIN_PASSWORD)
 * o cambiar la contrasena tras el primer arranque.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.bootstrap.admin-email}")
    private String adminEmail;

    @Value("${app.security.bootstrap.admin-password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        log.info("====================================================");
        log.info("Usuario administrador inicial creado: {}", adminEmail);
        log.info("Cambie la contrasena tras el primer login.");
        log.info("====================================================");
    }
}
