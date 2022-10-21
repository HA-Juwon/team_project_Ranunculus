package team.ranunculus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public IResult loginUser(UserEntity member) throws NoSuchAlgorithmException {
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
}
