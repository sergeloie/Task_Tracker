package hexlet.code.app.controller;

import hexlet.code.app.dto.AuthRequest;
import hexlet.code.app.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class AuthenticationController {

    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    @PostMapping(path = "/login")
    public String create(@RequestBody AuthRequest authRequest) {
        var authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword());
        authenticationManager.authenticate(authentication);
        var token = jwtUtils.generateToken(authRequest.getUsername());
        return token;
    }

}
