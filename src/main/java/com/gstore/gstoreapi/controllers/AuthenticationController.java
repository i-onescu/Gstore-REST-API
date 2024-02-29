package com.gstore.gstoreapi.controllers;

import com.gstore.gstoreapi.exceptions.ExistingActiveSessionException;
import com.gstore.gstoreapi.exceptions.InvalidSessionIdException;
import com.gstore.gstoreapi.models.dtos.LoginDTO;
import com.gstore.gstoreapi.services.BuyerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final BuyerService buyerService;

    @PutMapping("/login")
    public ResponseEntity<String> buyerLogin(@RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok()
                    .header("Session-Id", buyerService.login(loginDTO).toString())
                    .body("Successfully logged in!");
        } catch (ExistingActiveSessionException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> buyerLogout(HttpServletRequest request) {

        if (request.getHeader("Session-id").isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Please provide a session id in order to log out.\n" +
                            "Forgot session id? " +
                            "Go to login and if there is an existing session, then its respective id will be returned");
        }

        try {
            return ResponseEntity.ok()
                    .body(buyerService.logout(Long.valueOf(request.getHeader("Session-Id"))));
        } catch (InvalidSessionIdException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }



}
