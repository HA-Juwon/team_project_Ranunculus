package team.ranunculus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "team.ranunculus.controllers.BoardController")
@RequestMapping(value = "/board")
public class BoardController {
    @RequestMapping(value = "qna", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("board/index");
        return modelAndView;
    }
    @RequestMapping(value = "write", method = RequestMethod.GET)
    public ModelAndView getWrite(ModelAndView modelAndView) {
        modelAndView.setViewName("board/write");
        return modelAndView;
    }
}