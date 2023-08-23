package com.test.retailstorediscounts.service;

import com.test.retailstorediscounts.dto.JwtResponse;
import com.test.retailstorediscounts.dto.UserLoginRequest;
import com.test.retailstorediscounts.dto.UserSignUpRequest;
import com.test.retailstorediscounts.dto.UserSignUpResponse;
import com.test.retailstorediscounts.entity.Role;
import com.test.retailstorediscounts.entity.User;
import com.test.retailstorediscounts.enums.UserRole;
import com.test.retailstorediscounts.exception.UserAlreadyExistException;
import com.test.retailstorediscounts.repository.RoleRepository;
import com.test.retailstorediscounts.repository.UserRepository;
import com.test.retailstorediscounts.service.security.JwtUtils;
import com.test.retailstorediscounts.service.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Override
    public UserSignUpResponse registerUser(UserSignUpRequest userSignUpRequest) throws UserAlreadyExistException {

        if (userRepository.existsByEmail(userSignUpRequest.getEmail()))
            throw new UserAlreadyExistException(String.format("User with email %s already exists", userSignUpRequest.getEmail()));

        Set<String> strRoles = userSignUpRequest.getRoles();

        Set<Role> roles = (strRoles == null || strRoles.isEmpty()) ?
                Set.of(getRoleByName(UserRole.ROLE_CUSTOMER)) : getRolesFromRoleNames(strRoles);

        User user = userRepository.save(User.builder().roles(roles)
                .email(userSignUpRequest.getEmail())
                .registrationDate(LocalDateTime.now())
                .password(encoder.encode(userSignUpRequest.getPassword()))
                .build());

        return UserSignUpResponse.builder()
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name()).collect(Collectors.toSet()))
                .success(true).build();
    }

    @Override
    public JwtResponse authenticate(UserLoginRequest userLoginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return JwtResponse.builder()
                .token(jwt).email(userDetails.getEmail())
                .roles(roles).build();
    }

    private Set<Role> getRolesFromRoleNames(Set<String> roleNames) {
        return roleNames.stream()
                .map(UserRole::valueOf)
                .map(this::getRoleByName)
                .collect(Collectors.toSet());
    }

    private Role getRoleByName(UserRole roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }
}
