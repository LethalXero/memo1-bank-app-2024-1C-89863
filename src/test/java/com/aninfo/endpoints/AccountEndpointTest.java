package com.aninfo.endpoints;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aninfo.Memo1BankApp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;

/*
 * Tests related to account endpoint.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Memo1BankApp.class})
@SpringBootTest(classes = Memo1BankApp.class)
public class AccountEndpointTest {

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
  public void when_send_a_get_request_to_accounts_expect_http_status_200() throws Exception {
    final ResultActions result =
        this.mockMvc.perform(get("/accounts").accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

    result.andExpect(status().isOk());
  }
}
