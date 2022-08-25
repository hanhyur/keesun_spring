package me.gracenam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

  public String getName() {
    return "gracename";
  }

}
