package org.spsochnev.vacancies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.spsochnev.vacancies.statistics.ExperienceStatDTO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RestIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authManager;

    @Test
    @DisplayName("Average salary integration test")
    public void testGetAverageSalary() throws Exception {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken("1", "1");
        Authentication auth = authManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(auth);

        MvcResult res = mockMvc.perform(get("/statistics/salary")).
                andDo(print()).andExpect(status().isOk()).andReturn();
        String json = res.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ExperienceStatDTO> statDTOs = objectMapper.readValue(json, new TypeReference<ArrayList<ExperienceStatDTO>>() {});

        assertFalse(statDTOs.isEmpty());
        Arrays.stream(new ExperienceStatDTO[]{
                new ExperienceStatDTO("noExperience", BigDecimal.valueOf(135000), BigInteger.valueOf(3)),
                new ExperienceStatDTO("between1And3", BigDecimal.valueOf(198333), BigInteger.valueOf(3)),
                new ExperienceStatDTO("between3And6", BigDecimal.valueOf(280000), BigInteger.valueOf(4)),
                new ExperienceStatDTO("moreThan6", BigDecimal.valueOf(410600), BigInteger.valueOf(5))
        }).forEach(dto -> assertTrue(statDTOs.contains(dto)));
    }

    @Test
    @DisplayName("Top skills integration test")
    public void testTop10Skills() throws Exception {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken("1", "1");
        Authentication auth = authManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(auth);

        HashMap<String, List<String>> expected = new HashMap<>();
        expected.put("noExperience", List.of("Java", "Spring", "OOP", "SQL", "Git"));
        expected.put("between1And3", List.of("Java", "OOP", "Maven", "REST"));
        expected.put("between3And6", List.of("SQL", "Git", "Spring Boot", "Agile", "Hibernate", "JUnit"));
        expected.put("moreThan6", List.of("BitBucket", "AWS", "SCALA", "Cassandra", "Docker", "Jenkins", "SQL", "Git", "Hibernate", "JUnit"));

        MvcResult res;
        String json;
        ObjectMapper objectMapper = new ObjectMapper();

        for (Map.Entry<String, List<String>> e: expected.entrySet()) {
            res = mockMvc.perform(get("/statistics/skills").param("exp", e.getKey())).
                    andDo(print()).andExpect(status().isOk()).andReturn();
            json = res.getResponse().getContentAsString();
            List<String> skills = objectMapper.readValue(json, new TypeReference<ArrayList<String>>() {});

            assertEquals(e.getValue().size(), skills.size());
            e.getValue().forEach(skill -> assertTrue(skills.contains(skill)));
        }
    }

}
