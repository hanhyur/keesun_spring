package me.gracenam.demobootweb;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  PersonRepository personRepository;

  @Test
  public void hello() throws Exception {
    Person person = new Person();
    person.setName("spring");
    Person savedPerson = personRepository.save(person);

    this.mockMvc.perform(get("/hello")
            .param("id", savedPerson.getId().toString()))
        .andDo(print())
        .andExpect(content().string("hello spring"));
  }

  @Test
  public void helloStatic() throws Exception {
    this.mockMvc.perform(get("/index.html"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(Matchers.containsString("hello static")));
  }

  @Test
  public void helloMobile() throws Exception {
    this.mockMvc.perform(get("/mobile/index.html"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(Matchers.containsString("hello mobile")))
        .andExpect(header().exists(HttpHeaders.CACHE_CONTROL));
  }
}