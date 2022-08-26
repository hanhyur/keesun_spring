package me.gracenam.demobootweb;

import org.aspectj.weaver.patterns.PerObject;
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

}