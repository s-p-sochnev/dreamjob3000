package org.spsochnev.vacancies.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class BillingErrorController implements ErrorController {

    @GetMapping("/error")
    public String error(HttpServletRequest request) {
        String redirect = "/billing/error";
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status == null) return redirect;
        int statusCode = Integer.parseInt(status.toString());
        if (statusCode == 403) redirect = "/billing/error403";
        if (statusCode == 404) redirect = "/billing/error404";
        return redirect;
    }

}
