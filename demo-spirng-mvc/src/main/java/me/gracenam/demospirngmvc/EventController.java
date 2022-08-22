package me.gracenam.demospirngmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventController {

  @Autowired
  EventService eventService;

//  @RequestMapping(value = "/events", method = RequestMethod.GET)
  @GetMapping("/events")  // spring 4.3 이상
  public String events(Model model) {
    model.addAttribute("events", eventService.getEvents());

    return "events";
  }
}
