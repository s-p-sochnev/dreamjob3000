package org.spsochnev.vacancies.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing")
public class Controller {

    @GetMapping("/error")
    public String error() { return "Something went wrong, please try again later"; }

    @GetMapping("/error403")
    public String error403() { return "Please login to use the service"; }

    @GetMapping("/error404")
    public String error404() {
        return "The page you requested cannot be found";
    }

}
