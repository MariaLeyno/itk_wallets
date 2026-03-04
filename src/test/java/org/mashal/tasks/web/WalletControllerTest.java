package org.mashal.tasks.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mashal.tasks.errors.WalletInsufficientFundsException;
import org.mashal.tasks.errors.WalletNotFoundException;
import org.mashal.tasks.errors.WalletServiceException;
import org.mashal.tasks.model.OperationType;
import org.mashal.tasks.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    private final UUID walletId = UUID.randomUUID();
    private final UUID notWalletId = UUID.randomUUID();

    @BeforeEach
    public void resetMocks() throws WalletServiceException {
        reset(walletService);

        when(walletService.getAmount(eq(walletId))).thenReturn(100.00);
        when(walletService.getAmount(eq(notWalletId))).thenThrow(new WalletNotFoundException(notWalletId));

        doNothing()
                .when(walletService).addWalletOperation(eq(walletId), eq(OperationType.WITHDRAW), anyDouble());
        doThrow(new WalletNotFoundException(notWalletId))
                .when(walletService).addWalletOperation(eq(notWalletId), any(OperationType.class), anyDouble());
        doThrow(new WalletInsufficientFundsException(walletId))
                .when(walletService).addWalletOperation(eq(walletId), eq(OperationType.WITHDRAW), eq(13.00));
    }

    @DisplayName("01. Get amount for an existing wallet.")
    @Test
    public void testGetAmount_success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v1/wallets/" + walletId)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"))
                .andReturn();
        assertNotNull(mvcResult);
    }

    @DisplayName("02. Getting amount fails for a not existing wallet.")
    @Test
    public void testGetAmount_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v1/wallets/" + notWalletId)
                )
                .andExpect(status().isNotFound())
                .andReturn();
        assertNotNull(mvcResult);
        assertInstanceOf(ResponseStatusException.class, mvcResult.getResolvedException());
        assertThat(mvcResult.getResolvedException().getMessage()).contains("does not exist");
    }

    @DisplayName("03. Add wallet operation for an existing wallet with enough money.")
    @Test
    public void testAddWalletOperation_success() throws Exception {
        String body = "{\"walletId\": \"" + walletId + "\", \"operationType\": \"" + OperationType.DEPOSIT + "\", \"amount\": 12}";
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isAccepted())
                .andReturn();
        assertNotNull(mvcResult);
    }

    @DisplayName("04. Adding wallet operation fails for a not existing wallet")
    @Test
    public void testAddWalletOperation_notFound() throws Exception {
        String body = "{\"walletId\": \"" + notWalletId + "\", \"operationType\": \"" + OperationType.DEPOSIT + "\", \"amount\": 12}";
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isNotFound())
                .andReturn();
        assertNotNull(mvcResult);
        assertInstanceOf(ResponseStatusException.class, mvcResult.getResolvedException());
        assertThat(mvcResult.getResolvedException().getMessage()).contains("does not exist");
    }

    @DisplayName("04. Adding wallet operation fails for a not existing wallet")
    @Test
    public void testAddWalletOperation_notMoney() throws Exception {
        String body = "{\"walletId\": \"" + walletId + "\", \"operationType\": \"" + OperationType.WITHDRAW + "\", \"amount\": 13}";
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        assertNotNull(mvcResult);
        assertInstanceOf(ResponseStatusException.class, mvcResult.getResolvedException());
        assertThat(mvcResult.getResolvedException().getMessage()).contains("does not have enough money");
    }
}
