package com.nocturnals.budget;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocturnals.budget.controller.AccountController;
import com.nocturnals.budget.db.dao.AccountService;
import com.nocturnals.budget.db.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void AccountController_save_returnSaved() throws Exception {
        Account account = new Account();
        account.setName("test");
        account.setBalance(BigDecimal.valueOf(100));
        Mockito.when(accountService.save(Mockito.any(Account.class))).thenReturn(account);
        String body = objectMapper.writeValueAsString(account);
        mockMvc.perform(post("/api/v1/account")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.balance").value(100));

        Mockito.verify(accountService, Mockito.times(1)).save(Mockito.any(Account.class));

        Mockito.reset(accountService);
    }

    // Saving account with non-unique name
    @Test
    public void testSaveAccountNonUniqueName() throws Exception {
        Account account = new Account();
        account.setName("test");
        account.setBalance(BigDecimal.valueOf(100));
        Mockito.when(accountService.save(Mockito.any(Account.class))).thenThrow(DataIntegrityViolationException.class);
        String body = objectMapper.writeValueAsString(account);
        mockMvc.perform(post("/api/v1/account")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(accountService, Mockito.times(1)).save(Mockito.any(Account.class));

        Mockito.reset(accountService);
    }

    // When finding an account by name, if the account exists, it should be returned with the correct values
    @Test
    public void AccountController_findByName_returnAccount() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setName("test");
        account.setBalance(BigDecimal.valueOf(100));
        Mockito.when(accountService.findByName(Mockito.anyString())).thenReturn(account);
        mockMvc.perform(get("/api/v1/account?account-name=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.balance").value(100));

        Mockito.verify(accountService, Mockito.times(1)).findByName(Mockito.anyString());

        Mockito.reset(accountService);
    }

    // When finding an account by ID, if the account exists, it should be returned with the correct values
    @Test
    public void AccountController_findById_returnAccount() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setName("test");
        account.setBalance(BigDecimal.valueOf(100));
        Mockito.when(accountService.findById(Mockito.anyLong())).thenReturn(account);
        mockMvc.perform(get("/api/v1/account?account-id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.balance").value(100));

        Mockito.verify(accountService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(accountService);
    }

    @Test
    public void testSaveAccountWithoutBalance() throws Exception {
        Account account = new Account();
        account.setName("test");
        Mockito.when(accountService.save(Mockito.any(Account.class))).thenReturn(account);
        String body = objectMapper.writeValueAsString(account);
        mockMvc.perform(post("/api/v1/account")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.balance").value(0));

        Mockito.verify(accountService, Mockito.times(1)).save(Mockito.any(Account.class));

        Mockito.reset(accountService);
    }

    @Test
    public void AccountController_findById_returnNotFound() throws Exception {
        Mockito.when(accountService.findById(Mockito.anyLong())).thenReturn(null);
        mockMvc.perform(get("/api/v1/account?account-id=999"))
                .andExpect(status().isNotFound());

        Mockito.verify(accountService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(accountService);
    }

    @Test
    public void AccountController_findByName_returnNull() throws Exception {
        Mockito.when(accountService.findByName(Mockito.anyString())).thenReturn(null);
        mockMvc.perform(get("/api/v1/account?account-name=TEST"))
                .andExpect(status().isNotFound());

        Mockito.verify(accountService, Mockito.times(1)).findByName(Mockito.anyString());

        Mockito.reset(accountService);
    }

    @Test
    public void AccountController_deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/v1/account/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(accountService, Mockito.times(1)).deleteById(id);
        Mockito.reset(accountService);
    }

    @Test
    public void testDeleteAccountNonExistingId() throws Exception {
        Long id = 1L;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(accountService).deleteById(id);
        mockMvc.perform(delete("/api/v1/account/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(accountService, Mockito.times(1)).deleteById(id);

        Mockito.reset(accountService);
    }

    @Test
    public void AccountController_findByName_returnAccountDifferentCase() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setName("test");
        account.setBalance(BigDecimal.valueOf(100));
        Mockito.when(accountService.findByName(Mockito.anyString())).thenReturn(account);
        mockMvc.perform(get("/api/v1/account?account-name=TEST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.balance").value(100));

        Mockito.verify(accountService, Mockito.times(1)).findByName(Mockito.anyString());

        Mockito.reset(accountService);
    }
}
