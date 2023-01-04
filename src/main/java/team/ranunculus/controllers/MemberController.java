package team.ranunculus.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
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
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    public ModelAndView getUserLogin(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity member,
                                     ModelAndView modelAndView) {
        if (member != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.POST)
    @ResponseBody
    public String postUserLogin(@RequestParam(value = "autosign", required = false)
                                Optional<Boolean> autosignOptional,
                                HttpSession session,
                                UserEntity member,
                                HttpServletResponse response) throws NoSuchAlgorithmException {
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
            if (autosign) { //오토로그인이 체크된 채로 로그인하면
                long second = 60 * 60 * 24 * 90; //오토로그인 작동 시간을 3달로 설정할 예정
                Cookie cookie = new Cookie("loginCookie", session.getId());
                cookie.setPath("/");
                cookie.setMaxAge((int) second);
                response.addCookie(cookie);
                //3개월뒤의 밀리초를 날짜로 변환
                long millis = System.currentTimeMillis() + (second * 1000);
                Date limitDate = new Date(millis);
//                System.out.println(limitDate);
                //DB에 세션아이디,쿠키만료날짜,회원 아이디 전달
                memberService.pushAutoLogin(session.getId(), limitDate, member.getEmail());
            }
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userLogout", method = RequestMethod.GET)
    public ModelAndView getUserLogout(ModelAndView modelAndView,
                                      HttpSession session,
                                      UserEntity member,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        member.setEmail(null)
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
        Cookie cookie = WebUtils.getCookie(request, "loginCookie");
        if (cookie != null) {//만약에 쿠키가 남아있다면(오토로그인을 켠 사용자가 로그아웃을 누르면) 그 세션의 유효기간을 현재까지로 바꾸고 세션아이디를 none로 바꾼다.
            cookie.setPath("/");
            cookie.setMaxAge(0); //쿠키의 유효기간을 지금까지로
            response.addCookie(cookie);
//            System.out.println("[getUserLogout]쿠키의 세션 키값"+cookie.getValue());
            member.setSessionId(cookie.getValue());
            this.memberService.autoLoginLogout(member);
        }

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

    @RequestMapping(value = "userRegisterDone", method = RequestMethod.GET)
    public ModelAndView getUserRegisterDone(ModelAndView modelAndView) {
        modelAndView.setViewName("member/userRegisterDone");
        return modelAndView;
    }

    @RequestMapping(value = "userContactAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserContactAuth(ContactAuthEntity contactAuth) {
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

    @RequestMapping(value = "userContactAuth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserContactAuth(ContactAuthEntity contactAuth) throws Exception {
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
    public String postUserRecoverPassword(ContactAuthEntity contactAuth, UserEntity user) {
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
    public ModelAndView getUserResetPassword(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
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
    public ModelAndView getUserEdit(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                    @RequestParam(value = "tab", required = false, defaultValue = "info") String tab,
                                    ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        if (tab == null || tab.equals("info") || (!tab.equals("qna") && !tab.equals("review") && !tab.equals("shipping") && !tab.equals("truncate"))) {
            TelecomEntity[] telecoms = this.memberService.getTelecoms();
            modelAndView.addObject(TelecomEntity.ATTRIBUTE_NAME_PLURAL, telecoms);
        }
        modelAndView.setViewName("member/userEdit");
        return modelAndView;
    }

    @RequestMapping(value = "userEditInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postUserEditInfo(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity currentUser,
                               @RequestParam(value = "oldPassword") String oldPassword,
                               @RequestParam(value = "newPassword", required = false) String newPassword,
                               @RequestParam(value = "newAddressPostal", required = false) String newAddressPostal,
                               @RequestParam(value = "newAddressPrimary", required = false) String newAddressPrimary,
                               @RequestParam(value = "newAddressSecondary", required = false) String newAddressSecondary,
                               @RequestParam(value = "newTelecomValue", required = false) String newTelecomValue,
                               @RequestParam(value = "newContact", required = false) String newContact,
                               @RequestParam(value = "newContactAuthCode",required = false) String newContactAuthCode,
                               @RequestParam(value = "newContactAuthSalt",required = false) String newContactAuthSalt )
            throws Exception {
        UserEntity newUser = UserEntity.build();
        if (newPassword != null) {
            newUser.setPassword(newPassword);
        }
        if (newAddressPostal != null &&
                newAddressPrimary != null &&
                newAddressSecondary != null) {
            newUser.setAddressPostal(newAddressPostal);
            newUser.setAddressPrimary(newAddressPrimary);
            newUser.setAddressSecondary(newAddressSecondary);
        }
        if (newTelecomValue != null &&
                newContact != null) {
            newUser.setTelecomValue(newTelecomValue);
            newUser.setContact(newContact);
        }
        ContactAuthEntity contactAuth = ContactAuthEntity.build();
        contactAuth.setContact(newContact)
                .setCode(newContactAuthCode)
                .setSalt(newContactAuthSalt);
        IResult result = this.memberService.editUser(currentUser, newUser, oldPassword, contactAuth);
        JSONObject responseJson = new JSONObject();
//        System.out.println(result);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }
}
