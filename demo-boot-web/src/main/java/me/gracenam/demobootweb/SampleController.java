package me.gracenam.demobootweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  // preHandle
  // 요청 처리
  // postHandle
  // 뷰 렌더링
  // afterCompletion

  @GetMapping("/hello")
  public String hello(@RequestParam("id") Person person) {
    return "hello " + person.getName();
  }

}
