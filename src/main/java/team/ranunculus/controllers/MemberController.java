package team.ranunculus.controllers;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.services.MemberService;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Controller(value = "team.ranunculus.controllers.MemberContainer")
@RequestMapping(value = "/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.GET)
    public ModelAndView getUserLogin (@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity member, ModelAndView modelAndView) {
        if (member != null) {
            modelAndView.setViewName("redirect:/");
        }
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }
    @RequestMapping(value = "userLogin", method = RequestMethod.POST)
    @ResponseBody
    public String postUserLogin (@RequestParam(value = "autosign", required = false) Optional<Boolean> autosignOptional,
                                       HttpSession session,
                                       UserEntity member) throws NoSuchAlgorithmException {
        boolean autosign = autosignOptional.orElse(false);
        member.setName(null)
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
        IResult result = this.memberService.loginUser(member);
        if (result == CommonResult.SUCCESS) {
            session.setAttribute(UserEntity.ATTRIBUTE_NAME, member);
            if (autosign) {
                //구현하기
            }
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userLogout", method = RequestMethod.GET)
    public ModelAndView getUserLogout(ModelAndView modelAndView,
                                      HttpSession session) {
        session.removeAttribute(UserEntity.ATTRIBUTE_NAME);
        modelAndView.setViewName("redirect:/");
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
