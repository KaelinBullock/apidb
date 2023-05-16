package com.example.apidb.authentication;

import com.example.apidb.config.JwtUtils;
import com.example.apidb.config.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtUtils jwtUtils;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate (
            @RequestBody AuthenticationRequest request
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        final UserDetails user = userDao.findUsersByEmail(request.getEmail());
        if (user != null) {
            return ResponseEntity.ok(jwtUtils.generateToken(user));
        }
        return ResponseEntity.status(400).body("Some error has occured");
    }

    @PostMapping( "/authenticateTokenEncoded" )
    public ResponseEntity<Map<String, String>> authenticateTokenEncoded( @RequestParam Map<String, String> request ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password"))
        );
        final UserDetails user = userDao.findUsersByEmail(request.get("username"));
        Map<String, String> accessToken = new HashMap<>();
        if (user != null) {
            accessToken.put("access_token", jwtUtils.generateToken(user));
            return ResponseEntity.ok(accessToken);
        }
        accessToken.put("error", "Some error has occured");
        return ResponseEntity.status(400).body(accessToken);
    }
}
