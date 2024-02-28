package com.nocturnals.budget;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocturnals.budget.controller.BankController;
import com.nocturnals.budget.db.dao.*;
import com.nocturnals.budget.db.entity.Bank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void BankController_save_returnSaved() throws Exception {
        Bank bank = new Bank();
        bank.setName("test");
        Mockito.when(bankService.save(Mockito.any(Bank.class))).thenReturn(bank);
        String body = objectMapper.writeValueAsString(bank);
        mockMvc.perform(post("/api/v1/bank")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));

        Mockito.verify(bankService, Mockito.times(1)).save(Mockito.any(Bank.class));

        Mockito.reset(bankService);
    }

    // When finding a bank by name, the BankController should return the correct bank with a status code of 200
    @Test
    public void BankController_findBankByName_returnBank() throws Exception {
        Bank bank = new Bank();
        bank.setId(1L);
        bank.setName("test");
        Mockito.when(bankService.findByName(Mockito.anyString())).thenReturn(bank);
        mockMvc.perform(get("/api/v1/bank")
                .param("bank-name", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"));

        Mockito.verify(bankService, Mockito.times(1)).findByName(Mockito.anyString());

        Mockito.reset(bankService);
    }

    // When finding a bank by ID, the BankController should return the correct bank with a status code of 200
    @Test
    public void BankController_findBankById_returnBank() throws Exception {
        Bank bank = new Bank();
        bank.setId(1L);
        bank.setName("test");
        Mockito.when(bankService.findById(Mockito.anyLong())).thenReturn(bank);
        mockMvc.perform(get("/api/v1/bank")
                .param("bank-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"));

        Mockito.verify(bankService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(bankService);
    }

    @Test
    public void BankController_findBankById_returnNull() throws Exception {
        Mockito.when(bankService.findById(Mockito.anyLong())).thenReturn(null);
        mockMvc.perform(get("/api/v1/bank")
                        .param("bank-id", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        Mockito.verify(bankService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(bankService);
    }

    @Test
    public void BankController_findBankByName_returnNotFound() throws Exception {
        Mockito.when(bankService.findByName(Mockito.anyString())).thenReturn(null);
        mockMvc.perform(get("/api/v1/bank")
                        .param("bank-name", "nonexistent"))
                .andExpect(status().isNotFound());

        Mockito.verify(bankService, Mockito.times(1)).findByName(Mockito.anyString());

        Mockito.reset(bankService);
    }

    @Test
    public void BankController_deleteBankById_returnSuccess() throws Exception {
        Long bankId = 1L;
        mockMvc.perform(delete("/api/v1/bank/{id}", bankId))
                .andExpect(status().isOk());

        Mockito.verify(bankService, Mockito.times(1)).deleteById(Mockito.eq(bankId));

        Mockito.reset(bankService);
    }

    @Test
    public void BankController_findAll_returnList() throws Exception {
        Bank bank = new Bank();
        bank.setId(1L);
        bank.setName("test");
        Mockito.when(bankService.findAll()).thenReturn(List.of(bank));
        mockMvc.perform(get("/api/v1/bank/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test"));

        Mockito.verify(bankService, Mockito.times(1)).findAll();

        Mockito.reset(bankService);
    }
}
