package com.nocturnals.budget;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocturnals.budget.controller.AccountTypeController;
import com.nocturnals.budget.db.dao.AccountTypeService;
import com.nocturnals.budget.db.entity.AccountType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AccountTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountTypeService accountTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void AccountTypeController_save_returnSaved() throws Exception {
        AccountType accountType = new AccountType();
        accountType.setType("test");
        Mockito.when(accountTypeService.save(Mockito.any(AccountType.class))).thenReturn(accountType);
        String body = objectMapper.writeValueAsString(accountType);
        mockMvc.perform(post("/api/v1/account-type")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("test"));

        Mockito.verify(accountTypeService, Mockito.times(1)).save(Mockito.any(AccountType.class));

        Mockito.reset(accountTypeService);
    }

    // Trying to find an account type with an invalid id returns null
    @Test
    public void AccountTypeController_findAccountTypeByInvalidId_returnNull() throws Exception {
        Long invalidId = 999L;
        Mockito.when(accountTypeService.findById(invalidId)).thenReturn(null);
        mockMvc.perform(get("/api/v1/account-type")
                        .param("account-type-id", invalidId.toString()))
                .andExpect(status().isNotFound());

        Mockito.verify(accountTypeService, Mockito.times(1)).findById(invalidId);

        Mockito.reset(accountTypeService);
    }

    // Trying to find an account type with a valid id and matching type returns the account type
    @Test
    public void AccountTypeController_findAccountTypeByIdAndType_returnAccountType() throws Exception {
        AccountType accountType = new AccountType();
        accountType.setId(1L);
        accountType.setType("test");
        Mockito.when(accountTypeService.findById(Mockito.anyLong())).thenReturn(accountType);
        Mockito.when(accountTypeService.findByType(Mockito.anyString())).thenReturn(accountType);
        mockMvc.perform(get("/api/v1/account-type")
                        .param("account-type-id", "1")
                        .param("account-type", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("test"));

        Mockito.verify(accountTypeService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(accountTypeService);
    }

    // Trying to find an account type with a valid id and mismatching type returns an error message
    @Test
    public void AccountTypeController_findAccountTypeByIdAndMismatchingType_returnErrorMessage() throws Exception {
        Long id = 1L;
        String type = "test";
        AccountType accountType = new AccountType();
        accountType.setId(id);
        accountType.setType("other");
        Mockito.when(accountTypeService.findById(id)).thenReturn(accountType);
        mockMvc.perform(get("/api/v1/account-type")
                        .param("account-type-id", String.valueOf(id))
                        .param("account-type", type))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account type and id does not match"));

        Mockito.verify(accountTypeService, Mockito.times(1)).findById(id);

        Mockito.reset(accountTypeService);
    }

    // Deleting an account type by id successfully deletes the account type
    @Test
    public void AccountTypeController_deleteById_successfullyDeletesAccountType() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/v1/account-type/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(accountTypeService, Mockito.times(1)).deleteById(id);

        Mockito.reset(accountTypeService);
    }

    // Trying to save an account type with a duplicate type throws a DataIntegrityViolationException
    @Test
    public void testSaveAccountTypeWithDuplicateType() throws Exception {
        AccountType accountType = new AccountType();
        accountType.setType("duplicate");
        Mockito.when(accountTypeService.save(Mockito.any(AccountType.class))).thenThrow(DataIntegrityViolationException.class);
        String body = objectMapper.writeValueAsString(accountType);
        mockMvc.perform(post("/api/v1/account-type")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account type must be unique"));

        Mockito.verify(accountTypeService, Mockito.times(1)).save(Mockito.any(AccountType.class));

        Mockito.reset(accountTypeService);
    }

    // Finding all account types returns a list of all account types
    @Test
    public void testFindAllAccountTypes() throws Exception {
        List<AccountType> accountTypes = new ArrayList<>();
        AccountType accountType1 = new AccountType();
        accountType1.setId(1L);
        accountType1.setType("type1");
        AccountType accountType2 = new AccountType();
        accountType2.setId(2L);
        accountType2.setType("type2");
        accountTypes.add(accountType1);
        accountTypes.add(accountType2);
        Mockito.when(accountTypeService.findAll()).thenReturn(accountTypes);
        mockMvc.perform(get("/api/v1/account-type/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("type1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].type").value("type2"));

        Mockito.verify(accountTypeService, Mockito.times(1)).findAll();

        Mockito.reset(accountTypeService);
    }

    // When finding an AccountType by type, the AccountTypeController should return the corresponding AccountType with a status code of 200.
    @Test
    public void AccountTypeController_findAccountTypeByType_returnAccountType() throws Exception {
        AccountType accountType = new AccountType();
        accountType.setId(1L);
        accountType.setType("test");
        Mockito.when(accountTypeService.findByType(Mockito.anyString())).thenReturn(accountType);
        mockMvc.perform(get("/api/v1/account-type")
                        .param("account-type", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("test"));

        Mockito.verify(accountTypeService, Mockito.times(1)).findByType(Mockito.anyString());

        Mockito.reset(accountTypeService);
    }

    // When finding an AccountType by id, the AccountTypeController should return the corresponding AccountType with a status code of 200.
    @Test
    public void AccountTypeController_findAccountTypeById_returnAccountType() throws Exception {
        AccountType accountType = new AccountType();
        accountType.setId(1L);
        accountType.setType("test");
        Mockito.when(accountTypeService.findById(Mockito.anyLong())).thenReturn(accountType);
        mockMvc.perform(get("/api/v1/account-type")
                        .param("account-type-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("test"));

        Mockito.verify(accountTypeService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(accountTypeService);
    }
}
