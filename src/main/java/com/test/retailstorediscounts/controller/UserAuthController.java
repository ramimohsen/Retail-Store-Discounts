package com.test.retailstorediscounts.controller;


import com.test.retailstorediscounts.dto.request.UserLoginRequest;
import com.test.retailstorediscounts.dto.request.UserSignUpRequest;
import com.test.retailstorediscounts.dto.response.JwtResponse;
import com.test.retailstorediscounts.dto.response.UserSignUpResponse;
import com.test.retailstorediscounts.exception.ErrorDetails;
import com.test.retailstorediscounts.exception.UserAlreadyExistException;
import com.test.retailstorediscounts.service.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "User Auth Controller", description = "User Auth controller")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/user")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @Operation(summary = "Register new user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserSignUpResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")}),
    })
    @PostMapping("/auth/register")
    public UserSignUpResponse registerUser(@RequestBody @Valid UserSignUpRequest userSignUpRequest) throws UserAlreadyExistException {
        return this.userAuthService.registerUser(userSignUpRequest);
    }


    @Operation(summary = "Login and generate JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = JwtResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")}),
    })
    @PostMapping("/auth/login")
    public JwtResponse authenticate(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        return this.userAuthService.authenticate(userLoginRequest);
    }
}
