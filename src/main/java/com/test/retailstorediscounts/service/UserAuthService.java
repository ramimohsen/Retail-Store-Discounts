package com.test.retailstorediscounts.service;

import com.test.retailstorediscounts.dto.JwtResponse;
import com.test.retailstorediscounts.dto.UserLoginRequest;
import com.test.retailstorediscounts.dto.UserSignUpRequest;
import com.test.retailstorediscounts.dto.UserSignUpResponse;
import com.test.retailstorediscounts.exception.UserAlreadyExistException;

public interface UserAuthService {

    /**
     * @param userSignUpRequest user signup request dto
     * @return @{@link UserSignUpResponse}
     * @throws UserAlreadyExistException if user email already exists
     */
    UserSignUpResponse registerUser(UserSignUpRequest userSignUpRequest) throws UserAlreadyExistException;

    /**
     * @param userLoginRequest user login request dto
     * @return @{@link JwtResponse} JWT if successful authentication occurred and @{@link javax.security.sasl.AuthenticationException} if incorrect email or password provided
     */
    JwtResponse authenticate(UserLoginRequest userLoginRequest);
}
