package org.spsochnev.vacancies.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/payment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserBalanceDTO payment(@ModelAttribute(name = "payment") PaymentRequestDTO paymentDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return paymentService.processUserPayment(username, paymentDTO.amount());
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/balance")
    public UserBalanceDTO getBalance() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserBalanceByUsername(username);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/paymentHistory")
    public List<PaymentDTO> getPaymentHistory() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return paymentService.getPaymentHistoryByUsername(username);
    }

}
