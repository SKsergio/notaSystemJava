package com.sistema.notas.service.security;

import com.sistema.notas.dto.security.LoginRequestDTO;
import com.sistema.notas.dto.security.LoginResponseDTO;
import com.sistema.notas.dto.security.RegisterRequestDTO;
import com.sistema.notas.dto.security.UserResponseDTO;
import com.sistema.notas.entity.security.User;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.UnauthorizedException;
import com.sistema.notas.respository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sistema.notas.dto.security.ChangePasswordRequestDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
            );
        } catch (BadCredentialsException ex) {
            // Mensaje generico: no revelamos si el email existe o no
            throw new UnauthorizedException("Credenciales invalidas");
        }

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));

        String token = jwtService.generateToken(user);
        return new LoginResponseDTO(
                token,
                "Bearer",
                jwtService.getExpirationMs(),
                toResponse(user),
                user.isFirstLogin()
        );
    }

    @Transactional
    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Ya existe un usuario con el correo: " + dto.email());
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role());
        user.setTeacherId(dto.teacherId());
        user.setStudentId(dto.studentId());

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Sesion invalida"));
        return toResponse(user);
    }

    //creo que esto lo voy a pasar a un mapper
    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getTeacherId(),
                user.getStudentId()
        );
    }
    @Transactional
    public void changePassword(ChangePasswordRequestDTO request) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UnauthorizedException("Usuario no encontrado"));

        user.setPasswordHash(
                passwordEncoder.encode(request.newPassword())
        );

        user.setFirstLogin(false);

        userRepository.save(user);
    }
}
