package me.gracenam.springbootsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @WithMockUser
  public void hello() throws Exception {
    mockMvc.perform(get("/hello"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("hello"));
  }

  @Test
  @WithMockUser
  public void my() throws Exception {
    mockMvc.perform(get("/my"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("my"));
  }

}