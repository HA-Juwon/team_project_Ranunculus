package team.ranunculus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/shop")
public class ShopController {

    private ModelAndView modelAndView;

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ModelAndView getCategory(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/category");
        return modelAndView;
    }

    @RequestMapping(value = "/basket", method = RequestMethod.GET)
    public ModelAndView getBasket(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/basket");
        return modelAndView;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ModelAndView getDetail(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/detailpage");
        return modelAndView;
    }
}
