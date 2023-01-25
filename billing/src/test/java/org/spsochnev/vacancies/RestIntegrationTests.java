package org.spsochnev.vacancies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.spsochnev.vacancies.payment.PaymentDTO;
import org.spsochnev.vacancies.payment.PaymentRequestDTO;
import org.spsochnev.vacancies.payment.UserBalanceDTO;
import org.spsochnev.vacancies.statistics.StatisticsRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RestIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authManager;

    @BeforeEach
    public void setAuthentication() {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken("1", "1");
        Authentication auth = authManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("Get user balance")
    public void testGetBalance() throws Exception {
        MvcResult res = mockMvc.perform(get("/billing/balance")).
                andDo(print()).andExpect(status().isOk()).andReturn();
        String json = res.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        UserBalanceDTO balanceDTO = objectMapper.readValue(json, new TypeReference<UserBalanceDTO>() {});

        assertEquals("1", balanceDTO.username());
        assertEquals(0, BigDecimal.valueOf(30).compareTo(balanceDTO.balance()));
    }

    @Test
    @DisplayName("Get user payment history")
    public void testGetPaymentHistory() throws Exception {
        MvcResult res = mockMvc.perform(get("/billing/paymentHistory")).
                andDo(print()).andExpect(status().isOk()).andReturn();
        String json = res.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<PaymentDTO> paymentDTOs = objectMapper.readValue(json, new TypeReference<List<PaymentDTO>>() {});

        assertEquals(2, paymentDTOs.size());
        assertTrue(paymentDTOs.contains(new PaymentDTO("1",
                LocalDateTime.of(2022, 9, 3, 0, 0), BigDecimal.valueOf(10))));
        assertTrue(paymentDTOs.contains(new PaymentDTO("1",
                LocalDateTime.of(2022, 9, 3, 0, 0), BigDecimal.valueOf(20))));

    }

    @Test
    @DisplayName("Get user statistics request history")
    public void testGetStatisticsRequestHistory() throws Exception {
        MvcResult res = mockMvc.perform(get("/billing/requestHistory")).
                andDo(print()).andExpect(status().isOk()).andReturn();
        String json = res.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<StatisticsRequestDTO> requestDTOs = objectMapper.readValue(json, new TypeReference<List<StatisticsRequestDTO>>() {});

        assertEquals(2, requestDTOs.size());
        assertTrue(requestDTOs.contains(new StatisticsRequestDTO("1",
                LocalDateTime.of(2022, 9, 3, 0, 0), "/statistics/salary", BigDecimal.valueOf(10))));
        assertTrue(requestDTOs.contains(new StatisticsRequestDTO("1",
                LocalDateTime.of(2022, 9, 3, 0, 0), "/statistics/skills", BigDecimal.valueOf(20))));
    }

    @Test
    @DirtiesContext
    @DisplayName("Process user payment")
    public void testProcessPayment() throws Exception {
        MvcResult res = mockMvc.perform(post("/billing/payment").
                        contentType(MediaType.MULTIPART_FORM_DATA_VALUE).
                        flashAttr("payment", new PaymentRequestDTO(BigDecimal.valueOf(30)))).
                andDo(print()).andExpect(status().isOk()).andReturn();
        String json = res.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        UserBalanceDTO balanceDTO = objectMapper.readValue(json, new TypeReference<UserBalanceDTO>() {});

        assertEquals("1", balanceDTO.username());
        assertEquals(0, BigDecimal.valueOf(60).compareTo(balanceDTO.balance()));
    }

}
