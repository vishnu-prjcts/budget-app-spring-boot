package com.nocturnals.budget;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocturnals.budget.controller.LedgerController;
import com.nocturnals.budget.db.dao.LedgerService;
import com.nocturnals.budget.db.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LedgerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class LedgerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LedgerService ledgerService;

    @Test
    public void LedgerController_save_returnSaved() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setDescription("test");
        transaction.setAmount(BigDecimal.valueOf(100));
        Mockito.when(ledgerService.save(Mockito.any(Transaction.class))).thenReturn(transaction);
        String body = objectMapper.writeValueAsString(transaction);
        mockMvc.perform(post("/api/v1/transaction")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.amount").value(100));

        Mockito.verify(ledgerService, Mockito.times(1)).save(Mockito.any(Transaction.class));

        Mockito.reset(ledgerService);
    }

    // Filtering transactions by start and end dates
    @Test
    public void testFilterTransactionsByDates() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setDescription("Transaction 1");
        transaction1.setAmount(BigDecimal.valueOf(100));
        transaction1.setTransactionDate(Date.valueOf("2022-01-05"));
        transactions.add(transaction1);
        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setDescription("Transaction 2");
        transaction2.setAmount(BigDecimal.valueOf(200));
        transaction2.setTransactionDate(Date.valueOf("2022-01-15"));
        transactions.add(transaction2);
        Mockito.when(ledgerService.findByDateRange(Mockito.any(), Mockito.any())).thenReturn(transactions);
        mockMvc.perform(get("/api/v1/transaction")
                        .param("start-date", "2022-01-01")
                        .param("end-date", "2022-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Transaction 1"))
                .andExpect(jsonPath("$[0].amount").value(100))
                .andExpect(jsonPath("$[0].transactionDate").value("2022-01-05"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Transaction 2"))
                .andExpect(jsonPath("$[1].amount").value(200))
                .andExpect(jsonPath("$[1].transactionDate").value("2022-01-15"));

        Mockito.verify(ledgerService, Mockito.times(1)).findByDateRange(Mockito.any(), Mockito.any());

        Mockito.reset(ledgerService);
    }

    // Deleting a transaction with invalid ID
    @Test
    public void LedgerController_deleteTransactionWithInvalidId_returnNotFound() throws Exception {
        Long invalidId = 9999L;
        Mockito.doThrow(EmptyResultDataAccessException.class).when(ledgerService).deleteById(invalidId);
        mockMvc.perform(delete("/api/v1/transaction/{id}", invalidId))
                .andExpect(status().isNotFound());

        Mockito.verify(ledgerService, Mockito.times(1)).deleteById(invalidId);

        Mockito.reset(ledgerService);
    }

    // Retrieving a transaction with invalid ID
    @Test
    public void LedgerController_getTransactionByInvalidId_returnNotFound() throws Exception {
        Long invalidId = 9999L;
        Mockito.when(ledgerService.findById(Mockito.eq(invalidId))).thenReturn(null);
        mockMvc.perform(get("/api/v1/transaction")
                        .param("transaction-id", String.valueOf(invalidId)))
                .andExpect(status().isNotFound());

        Mockito.verify(ledgerService, Mockito.times(1)).findById(Mockito.eq(invalidId));

        Mockito.reset(ledgerService);
    }

    // Deleting a transaction with valid ID
    @Test
    public void testDeleteTransactionValidId() throws Exception {
        Long transactionId = 1L;
        Mockito.doNothing().when(ledgerService).deleteById(transactionId);
        mockMvc.perform(delete("/api/v1/transaction/{id}", transactionId))
                .andExpect(status().isOk());

        Mockito.verify(ledgerService, Mockito.times(1)).deleteById(transactionId);

        Mockito.reset(ledgerService);
    }

    // Retrieving a transaction by ID
    @Test
    public void testGetTransactionById() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("test");
        transaction.setAmount(BigDecimal.valueOf(100));
        Mockito.when(ledgerService.findById(Mockito.anyLong())).thenReturn(transaction);
        mockMvc.perform(get("/api/v1/transaction")
                        .param("transaction-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("test"))
                .andExpect(jsonPath("$[0].amount").value(100));

        Mockito.verify(ledgerService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(ledgerService);
    }

    // Saving a transaction with unique description
    @Test
    public void testSaveTransactionUniqueDescription() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setDescription("test");
        transaction.setAmount(BigDecimal.valueOf(100));
        Mockito.when(ledgerService.save(Mockito.any(Transaction.class))).thenReturn(transaction);
        String body = objectMapper.writeValueAsString(transaction);
        mockMvc.perform(post("/api/v1/transaction")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.amount").value(100));

        Mockito.verify(ledgerService, Mockito.times(1)).save(Mockito.any(Transaction.class));

        Mockito.reset(ledgerService);
    }

    // When deleting a transaction by ID, the LedgerController should return a successful response.
    @Test
    public void LedgerController_deleteTransaction_returnSuccess() throws Exception {
        mockMvc.perform(delete("/api/v1/transaction/1"))
                .andExpect(status().isOk());

        Mockito.verify(ledgerService, Mockito.times(1)).deleteById(Mockito.anyLong());

        Mockito.reset(ledgerService);
    }

    // When retrieving a transaction by ID, the LedgerController should return the transaction with the specified ID.
    @Test
    public void LedgerController_getTransactionById_returnTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("test");
        transaction.setAmount(BigDecimal.valueOf(100));
        Mockito.when(ledgerService.findById(Mockito.anyLong())).thenReturn(transaction);
        mockMvc.perform(get("/api/v1/transaction")
                        .param("transaction-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("test"))
                .andExpect(jsonPath("$[0].amount").value(100));

        Mockito.verify(ledgerService, Mockito.times(1)).findById(Mockito.anyLong());

        Mockito.reset(ledgerService);
    }
}
