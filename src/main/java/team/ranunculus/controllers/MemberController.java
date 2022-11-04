package team.ranunculus.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import team.ranunculus.entities.member.ContactAuthEntity;
import team.ranunculus.entities.member.TelecomEntity;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.services.MemberService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Optional;

@Controller(value = "team.ranunculus.controllers.MemberContainer")
@RequestMapping(value = "/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(value = "userEmailCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getUserEmailCheck(UserEntity user) {
        IResult result = this.memberService.checkUserEmail(user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.GET)
    public ModelAndView getUserLogin(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity member, ModelAndView modelAndView) {
        if (member != null) {
            modelAndView.setViewName("redirect:/");
        }
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.POST)
    @ResponseBody
    public String postUserLogin(@RequestParam(value = "autosign", required = false)
                                    Optional<Boolean> autosignOptional,
                                HttpSession session,
                                UserEntity member) {
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
                session.setAttribute("id",session.getId());
                System.out.println(session.getId());

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
    public ModelAndView getUserRegister(ModelAndView modelAndView) {
        TelecomEntity[] telecoms = this.memberService.getTelecoms();
        modelAndView.addObject(TelecomEntity.ATTRIBUTE_NAME_PLURAL, telecoms);
        modelAndView.setViewName("member/userRegister");
        return modelAndView;
    }

    @RequestMapping(value = "userRegister", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postUserRegister(@RequestParam(value = "policyMarketing") boolean policyMarketing,
                                   UserEntity user) {
        user.setPolicyTermsAt(new Date())
                .setPolicyPrivacyAt(new Date())
                .setPolicyMarketingAt(policyMarketing ? new Date() : null)
                .setStatusValue("OKY")
                .setRegisteredAt(new Date())
                .setAdmin(false);
        IResult result = this.memberService.createUser(user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserRegisterAuth(ContactAuthEntity contactAuth) {
        IResult result;
        try {
            result = this.memberService.registerUserEmailAuth(contactAuth);
        } catch (Exception ex) {
            result = CommonResult.FAILURE;
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("salt", contactAuth.getSalt());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRegisterAuth(ContactAuthEntity contactAuth) throws Exception {
        return this.postUserRecoverAuth(contactAuth);
    }

    @RequestMapping(value = "userRecoverEmail", method = RequestMethod.GET)
    public ModelAndView getUserRecoverEmail(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user, ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.setViewName("member/userRecoverEmail");
        return modelAndView;
    }

    @RequestMapping(value = "userRecoverEmail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRecoverEmail(ContactAuthEntity contactAuth) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        UserEntity user = UserEntity.build();
        IResult result = this.memberService.findUserEmail(contactAuth, user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("email", user.getEmail());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userRecoverPassword", method = RequestMethod.GET)
    public ModelAndView getUserRecoverPassword(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user, ModelAndView modelAndView) {

        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.setViewName("member/userRecoverPassword");
        return modelAndView;
    }

    @RequestMapping(value = "userRecoverPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRecoverPassword(ContactAuthEntity contactAuth ,UserEntity user) {
        user.setPolicyTermsAt(null)
                .setPolicyPrivacyAt(null)
                .setPolicyMarketingAt(null)
                .setStatusValue(null)
                .setRegisteredAt(null)
                .setAdmin(false);

        IResult result = this.memberService.findUserEmail(contactAuth, user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("email", user.getEmail());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userResetPassword", method = RequestMethod.GET)
    public ModelAndView getUserResetPassword(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false)                                                                               UserEntity user,
                                             ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.setViewName("member/userResetPassword");
        return modelAndView;
    }


    @RequestMapping(value = "userResetPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserResetPassword(UserEntity user) {
        user.setContact(null)
                .setPolicyTermsAt(null)
                .setPolicyPrivacyAt(null)
                .setPolicyMarketingAt(null)
                .setStatusValue(null)
                .setRegisteredAt(null)
                .setAdmin(false);
        IResult result;

        result = this.memberService.resetPassword(user);

        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userRecoverAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserRecoverAuth(UserEntity user, ContactAuthEntity contactAuth) {
        user.setEmail(null)
                .setPassword(null)
                .setPolicyTermsAt(null)
                .setPolicyPrivacyAt(null)
                .setPolicyMarketingAt(null)
                .setStatusValue(null)
                .setRegisteredAt(null)
                .setAdmin(false);
        IResult result;
        try {
            result = this.memberService.recoverUserEmailAuth(user, contactAuth);
        } catch (Exception ex) {
            result = CommonResult.FAILURE;
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("salt", contactAuth.getSalt());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userRecoverAuth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRecoverAuth(ContactAuthEntity contactAuth) throws Exception {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        IResult result;
        result = this.memberService.checkContactAuth(contactAuth);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userEdit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getUserEdit(@SessionAttribute(UserEntity.ATTRIBUTE_NAME) UserEntity user,
                                    @RequestParam(value = "tab", required = false, defaultValue = "info") String tab,
                                    ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
        }
        if (tab == null || tab.equals("info") || (!tab.equals("qna")) && (!tab.equals("review")) && (!tab.equals("shipping")) && (!tab.equals("truncate"))) {
            TelecomEntity[] telecoms = this.memberService.getTelecoms();
            modelAndView.addObject(TelecomEntity.ATTRIBUTE_NAME_PLURAL, telecoms);
        }
        modelAndView.setViewName("member/userEdit");
        return modelAndView;
    }

}
