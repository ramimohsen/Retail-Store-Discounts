package com.test.retailstorediscounts.service.user;

import com.test.retailstorediscounts.dto.request.UserLoginRequest;
import com.test.retailstorediscounts.dto.request.UserSignUpRequest;
import com.test.retailstorediscounts.dto.response.JwtResponse;
import com.test.retailstorediscounts.dto.response.UserSignUpResponse;
import com.test.retailstorediscounts.exception.custom.UserAlreadyExistException;

public interface UserAuthService {

    /**
     * @param userSignUpRequest user signup request dto
     * @return @{@link UserSignUpResponse}
     * @throws UserAlreadyExistException if user email already exists
     */
    UserSignUpResponse registerUser(UserSignUpRequest userSignUpRequest) throws UserAlreadyExistException;

    /**
     * @param userLoginRequest user login request dto
     * @return @{@link JwtResponse} JWT if successful authentication occurred
     */
    JwtResponse authenticate(UserLoginRequest userLoginRequest);
}
