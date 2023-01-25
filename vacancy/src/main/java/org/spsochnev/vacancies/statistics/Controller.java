package org.spsochnev.vacancies.statistics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class Controller {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/error")
    public String error() { return "Something went wrong, please try again later"; }

    @GetMapping("/error403")
    public String error403() { return "Please login to use the service"; }

    @GetMapping("/error404")
    public String error404() {
        return "The page you requested cannot be found";
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/salary")
    public List<ExperienceStatDTO> averageSalaryByExp() {
        return statisticsService.getAverageSalaryByExp();
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/skills")
    public List<String> skillsByExp(@RequestParam(name="exp") String exp) {
        return statisticsService.getTop10SkillsByExp(exp);
    }

}
