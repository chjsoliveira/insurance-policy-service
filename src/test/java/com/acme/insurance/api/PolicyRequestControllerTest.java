package com.acme.insurance.api;

import com.acme.insurance.application.usecase.CreatePolicyRequestDTO;
import com.acme.insurance.application.usecase.CreatePolicyRequestUseCase;
import com.acme.insurance.domain.model.Category;
import com.acme.insurance.domain.model.PolicyRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PolicyRequestController.class)
public class PolicyRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreatePolicyRequestUseCase createUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreatePolicyRequest() throws Exception {
        CreatePolicyRequestDTO dto = new CreatePolicyRequestDTO();
        dto.setCustomerId(UUID.randomUUID());
        dto.setProductId(UUID.randomUUID());
        dto.setCategory("AUTO");
        dto.setSalesChannel("MOBILE");
        dto.setPaymentMethod("CREDIT_CARD");
        dto.setTotalMonthlyPremiumAmount(new BigDecimal("75.25"));
        dto.setInsuredAmount(new BigDecimal("275000.50"));
        dto.setCoverages(Map.of("Roubo", new BigDecimal("100000.25")));
        dto.setAssistances(java.util.List.of("Guincho até 250km"));

        PolicyRequest created = new PolicyRequest();
        created.setRequestId(UUID.randomUUID());
        created.setCustomerId(dto.getCustomerId());
        created.setCategory(Category.AUTO);
        created.setInsuredAmount(dto.getInsuredAmount());

        when(createUseCase.execute(Mockito.any(CreatePolicyRequestDTO.class))).thenReturn(created);

        mockMvc.perform(post("/policy-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(created.getRequestId().toString()))
                .andExpect(jsonPath("$.category").value("AUTO"));
    }
}
