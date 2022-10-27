package team.ranunculus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/shop")
public class ShopController {

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ModelAndView getCategory(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/category");
        return modelAndView;
    }

    @RequestMapping(value = "/basket", method = RequestMethod.GET)
    public ModelAndView getbasket(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/basket");
        return modelAndView;
    }

}
