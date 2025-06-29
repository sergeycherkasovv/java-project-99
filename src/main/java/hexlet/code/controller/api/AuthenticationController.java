package hexlet.code.controller.api;

import hexlet.code.model.AuthRequest;
import hexlet.code.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/login")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public String create(@RequestBody AuthRequest authRequest) {
        return authenticationService.createAuthentication(authRequest);
    }
}
