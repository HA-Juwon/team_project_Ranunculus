package team.ranunculus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "team.ranunculus.controllers.Controller")
@RequestMapping(value = "/")
public class HomeController {
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("home/index");
        return modelAndView;
    }
}