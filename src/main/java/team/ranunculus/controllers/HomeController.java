package team.ranunculus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.services.MemberService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller(value = "team.ranunculus.controllers.HomeController")
@RequestMapping(value = "/")
public class HomeController {
    private final MemberService memberService;
    public HomeController(MemberService memberService) {
        this.memberService = memberService;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView,
                                 @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity member,
                                 UserEntity user,
                                 HttpServletRequest request,
                                 HttpSession session) {
        //멤버는 세션에서 받아온 유저엔타타
        //유저는 로그인에 사용할 유저엔티티임
        if(member==null) {
            //로그인되어있지 않은 상태면
            user.setEmail(null)
                    .setPassword(null)
                    .setName(null)
                    .setAddressPostal(null)
                    .setAddressPrimary(null)
                    .setAddressSecondary(null)
                    .setTelecomValue(null)
                    .setContact(null)
                    .setPolicyTermsAt(null)
                    .setPolicyPrivacyAt(null)
                    .setPolicyMarketingAt(null)
                    .setStatusValue(null)
                    .setRegisteredAt(null);
            //자동로그인 부분
            Cookie cookie = WebUtils.getCookie(request, "loginCookie");
            if (cookie != null) { //로그인 쿠키라는 쿠키값이 있으면
                user.setSessionId(cookie.getValue());
                session.setAttribute(UserEntity.ATTRIBUTE_NAME, user);
                this.memberService.autoLogin(user);
            }
        }
        modelAndView.setViewName("home/index");
        return modelAndView;
    }
}