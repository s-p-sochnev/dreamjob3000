package org.spsochnev.vacancies.statistics;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class VacancyErrorController implements ErrorController {

    @GetMapping("/error")
    public String error(HttpServletRequest request) {
        String redirect = "/statistics/error";
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status == null) return redirect;
        int statusCode = Integer.parseInt(status.toString());
        if (statusCode == 403) redirect = "/statistics/error403";
        if (statusCode == 404) redirect = "/statistics/error404";
        return redirect;
    }

}
