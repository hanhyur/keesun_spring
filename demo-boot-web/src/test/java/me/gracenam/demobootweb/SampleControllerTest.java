package me.gracenam.demobootweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  public void hello() throws Exception {
    this.mockMvc.perform(get("/hello")
            .param("name", "spring"))
        .andDo(print())
        .andExpect(content().string("hello spring"));
  }

}