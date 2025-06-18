package hexlet.code.controller.api;

import hexlet.code.model.AuthRequest;
import hexlet.code.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public String create(@RequestBody AuthRequest authRequest) {
        return authenticationService.createAuthentication(authRequest);
    }
}
