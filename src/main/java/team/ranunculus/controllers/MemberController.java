package team.ranunculus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "team.ranunculus.controllers.MemberContainer")
@RequestMapping(value = "/member")
public class MemberController {

    @RequestMapping(value = "userLogin", method = RequestMethod.GET)
    public ModelAndView getUserLogin (ModelAndView modelAndView) {
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }

    @RequestMapping(value = "userRegister", method = RequestMethod.GET)
    public ModelAndView getUserRegister (ModelAndView modelAndView) {
        modelAndView.setViewName("member/userRegister");
        return modelAndView;
    }

    @RequestMapping(value = "userRecoverEmail", method = RequestMethod.GET)
    public ModelAndView getUserRecoverEmail (ModelAndView modelAndView) {
        modelAndView.setViewName("member/userRecoverEmail");
        return modelAndView;
    }
}
