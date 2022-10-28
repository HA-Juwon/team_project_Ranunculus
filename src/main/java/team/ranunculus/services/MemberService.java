package team.ranunculus.services;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import team.ranunculus.components.SmsComponent;
import team.ranunculus.entities.member.ContactAuthEntity;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.enums.member.UserLoginResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.mappers.IMemberMapper;
import team.ranunculus.regex.MemberRegex;
import team.ranunculus.utils.CryptoUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service(value = "team.ranunculus.services.MemberService")
public class MemberService {
    private final IMemberMapper memberMapper;
    private final SmsComponent smsComponent;
    @Autowired
    public MemberService(IMemberMapper memberMapper, SmsComponent smsComponent) {
        this.memberMapper = memberMapper;
        this.smsComponent = smsComponent;
    }

    @Transactional
    public IResult checkUserEmail(UserEntity user) {
        if (user.getEmail() == null ||
                !user.getEmail().matches(MemberRegex.USER_EMAIL)) {
            return CommonResult.FAILURE;
        }
        user = this.memberMapper.selectUserByEmail(user);
        return user == null
                ? CommonResult.SUCCESS
                : CommonResult.DUPLICATE;
    }

    @Transactional
    public IResult checkContactAuth(ContactAuthEntity contactAuth) throws
            Exception {
        System.out.println(contactAuth);
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
        if (contactAuth == null) {
            return CommonResult.FAILURE;
        }
        if (contactAuth.isExpired() || new Date().compareTo(contactAuth.getExpiresAt()) > 0) {
            return CommonResult.EXPIRED;
        }
//        System.out.println("debug"+contactAuth.getIndex());
        contactAuth.setExpired(true);
        if (this.memberMapper.updateContactAuth(contactAuth) == 0) {
           return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult createUser(UserEntity user) {
        if (user.getEmail() == null ||
                user.getPassword() == null ||
                user.getName() == null ||
                user.getAddressPostal() == null ||
                user.getAddressPrimary() == null ||
                user.getAddressSecondary() == null ||
                user.getTelecomValue() == "-1" ||
                user.getContact() == null ||
                !user.getEmail().matches(MemberRegex.USER_EMAIL) ||
                !user.getPassword().matches(MemberRegex.USER_PASSWORD) ||
                !user.getName().matches(MemberRegex.USER_NAME) ||
                !user.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        if (this.memberMapper.insertUser(user) == 0) {
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult loginUser(UserEntity member) {
//        System.out.println(member.getEmail());
        if (member.getEmail() == null ||
                member.getPassword() == null ||
                !member.getEmail().matches(MemberRegex.USER_EMAIL) ||
                !member.getPassword().matches(MemberRegex.USER_PASSWORD)) {
            return CommonResult.FAILURE;
        }
        member.setPassword(CryptoUtils.hashSha512(member.getPassword()));
        UserEntity existingMember = this.memberMapper.selectUserByEmailPassword(member);
        if (existingMember == null) {
            return CommonResult.FAILURE;
        }
        member.setEmail(existingMember.getEmail())
                .setPassword(existingMember.getPassword())
                .setName(existingMember.getName())
                .setAddressPostal(existingMember.getAddressPostal())
                .setAddressPrimary(existingMember.getAddressPrimary())
                .setAddressSecondary(existingMember.getAddressSecondary())
                .setTelecomValue(existingMember.getTelecomValue())
                .setContact(existingMember.getContact())
                .setPolicyTermsAt(existingMember.getPolicyTermsAt())
                .setPolicyPrivacyAt(existingMember.getPolicyPrivacyAt())
                .setPolicyMarketingAt(existingMember.getPolicyMarketingAt())
                .setStatusValue(existingMember.getStatusValue())
                .setRegisteredAt(existingMember.getRegisteredAt())
                .setAdmin(existingMember.isAdmin());
        if (member.getStatusValue().equals("SUS")) {
            return UserLoginResult.SUSPENDED;
        }
        return CommonResult.SUCCESS;
    }
    @Transactional
    public IResult recoverUserEmailAuth(UserEntity user, ContactAuthEntity contactAuth) throws
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            UnexpectedRollbackException {

        if (user.getName() == null ||
                user.getContact() == null ||
                !user.getName().matches(MemberRegex.USER_NAME) ||
                !user.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        Date createdAt = new Date();
        Date expiresAt = DateUtils.addMinutes(createdAt, 5);
        String code = RandomStringUtils.randomNumeric(6);
        String salt = CryptoUtils.hashSha512(String.format("%s%s%d%f%f",
                contactAuth.getContact(),
                code,
                createdAt.getTime(),
                Math.random(),
                Math.random()));
        contactAuth.setCode(code)
                .setSalt(salt)
                .setCreatedAt(createdAt)
                .setExpiresAt(expiresAt)
                .setExpired(false);
        if (this.memberMapper.insertContactAuth(contactAuth) == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.FAILURE;
        }
        String smsContent = String.format("[라넌큘러스] 인증번호 [%s]를 입력해 주세요.", contactAuth.getCode());
        System.out.println(contactAuth.getContact()+ smsContent);
        System.out.println(this.smsComponent.sendMessage (contactAuth.getContact(), smsContent));
        if (this.smsComponent.sendMessage(contactAuth.getContact(), smsContent) != 202) {
            return CommonResult.FAILURE;
        }

        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult findUserEmail(ContactAuthEntity contactAuth, UserEntity user) {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
        if (contactAuth == null || !contactAuth.isExpired()) {
            return CommonResult.FAILURE;
        }
        UserEntity foundUser = this.memberMapper.selectUserByContact(user.setContact(contactAuth.getContact()));
        if (foundUser == null) {
            return CommonResult.FAILURE;
        }
        user.setEmail(foundUser.getEmail());
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult resetPassword(UserEntity user){
        user.setEmail(user.getEmail());
        System.out.println(user.getEmail());
        UserEntity existingUser = this.memberMapper.selectUserByEmail(user);
        if (existingUser == null) {
            return CommonResult.FAILURE;
        }
        existingUser.setPassword(CryptoUtils.hashSha512(user.getPassword()));

        if (this.memberMapper.updateUser(existingUser) == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.FAILURE;
        }

        return CommonResult.SUCCESS;
    }
}
