package team.ranunculus.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import team.ranunculus.entities.member.ContactAuthEntity;
import team.ranunculus.entities.member.MemberEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.regex.MemberRegex;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.enums.member.UserLoginResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.mappers.IMemberMapper;
import team.ranunculus.regex.MemberRegex;
import team.ranunculus.utils.CryptoUtils;

import java.security.NoSuchAlgorithmException;

@Service(value = "team.ranunculus.services.MemberService")
public class MemberService {
    private final IMemberMapper memberMapper;

    @Autowired
    public MemberService(IMemberMapper memberMapper) {
        this.memberMapper = memberMapper;
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
        if (member.getEmail() == null ||
                member.getPassword() == null ||
                !member.getEmail().matches(MemberRegex.USER_EMAIL) ||
                !member.getPassword().matches(MemberRegex.USER_PASSWORD)) {
            return CommonResult.FAILURE;
        }
        member.setPassword(CryptoUtils.hashSha512(member.getPassword()));
        MemberEntity existingMember = this.memberMapper.selectUserByEmailPassword(member);
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

    //Mapper 만들어서 의존성 추가하고...전화번호 인증이랑 연동하기
    @Transactional
    public IResult recoverUserEmailAuth(MemberEntity user, ContactAuthEntity contactAuth) throws
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
//        String salt = CryptoUtils.hashSha512(String.format("%s%s%d%f%f",
//                contactAuth.getContact(),
//                code,
//                createdAt.getTime(),
//                Math.random(),
//                Math.random()));
//        contactAuth.setCode(code)
//                .setSalt(salt)
//                .setCreatedAt(createdAt)
//                .setExpiresAt(expiresAt)
//                .setExpired(false);

        return CommonResult.SUCCESS;
    }
}
