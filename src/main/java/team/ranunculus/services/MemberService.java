package team.ranunculus.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
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

@Service(value = "team.ranunculus.services.MemberService")
public class MemberService {

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
//        String salt = CryptoUtils .hashSha512(String.format("%s%s%d%f%f",
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
