package com.acme.insurance.api;

import com.acme.insurance.application.usecase.CancelPolicyRequestUseCase;
import com.acme.insurance.application.usecase.FindPolicyRequestByIdUseCase;
import com.acme.insurance.application.usecase.FindPolicyRequestsByCustomerUseCase;
import com.acme.insurance.domain.model.Category;
import com.acme.insurance.domain.model.PolicyRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PolicyRequestQueryController.class)
public class PolicyRequestQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindPolicyRequestByIdUseCase findByIdUseCase;

    @MockBean
    private FindPolicyRequestsByCustomerUseCase findByCustomerUseCase;

    @MockBean
    private CancelPolicyRequestUseCase cancelUseCase;

    @Test
    void shouldReturnPolicyRequestById() throws Exception {
        UUID id = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setRequestId(id);
        request.setCustomerId(UUID.randomUUID());
        request.setCategory(Category.AUTO);
        request.setInsuredAmount(new BigDecimal("100000"));

        when(findByIdUseCase.execute(id)).thenReturn(Optional.of(request));

        mockMvc.perform(get("/policy-requests/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void shouldReturnNotFoundWhenPolicyRequestDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        when(findByIdUseCase.execute(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/policy-requests/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnListOfPolicyRequestsByCustomerId() throws Exception {
        UUID customerId = UUID.randomUUID();
        PolicyRequest req = new PolicyRequest();
        req.setRequestId(UUID.randomUUID());
        req.setCustomerId(customerId);

        when(findByCustomerUseCase.execute(customerId)).thenReturn(List.of(req));

        mockMvc.perform(get("/policy-requests").param("customerId", customerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer_id").value(customerId.toString()));
    }

    @Test
    void shouldCancelPolicyRequestSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/policy-requests/{id}", id))
                .andExpect(status().isNoContent());

        verify(cancelUseCase).execute(id);
    }

    @Test
    void shouldReturn404WhenCancelThrowsIllegalArgumentException() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalArgumentException("not found")).when(cancelUseCase).execute(id);

        mockMvc.perform(delete("/policy-requests/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn409WhenCancelThrowsIllegalStateException() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalStateException("conflict")).when(cancelUseCase).execute(id);

        mockMvc.perform(delete("/policy-requests/{id}", id))
                .andExpect(status().isConflict());
    }
}
