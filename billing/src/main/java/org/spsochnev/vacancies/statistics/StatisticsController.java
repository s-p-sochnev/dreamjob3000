package org.spsochnev.vacancies.statistics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/billing")
public class StatisticsController {

    @Autowired
    private StatisticsRequestService statisticsRequestService;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/requestHistory")
    public List<StatisticsRequestDTO> getRequestHistory() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return statisticsRequestService.getStatisticsRequestHistoryByUsername(username);
    }

}
