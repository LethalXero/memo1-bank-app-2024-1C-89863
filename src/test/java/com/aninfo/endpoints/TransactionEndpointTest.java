package com.aninfo.endpoints;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aninfo.Memo1BankApp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/*
 * Tests related to transaction endpoint.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Memo1BankApp.class})
public class TransactionEndpointTest {

  @Autowired private Memo1BankApp memo1BankApp;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(memo1BankApp).build();
  }

  @After
  public void tearDown() {
    this.mockMvc = null;
  }

  @Test
  public void
      when_send_a_get_request_to_access_to_accounts_cbu_transactions_expect_http_status_200()
          throws Exception {
    final Long cbu = 124L;

    mockMvc
        .perform(get("/accounts/{cbu}/transactions", cbu).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void when_send_a_get_request_to_accounts_cbu_transactions_number_expect_http_status_404()
      throws Exception {
    final Long cbu = 124L;
    final Long number = 123L;

    mockMvc
        .perform(
            get("/accounts/{cbu}/transactions/{number}", cbu, number)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }
}
