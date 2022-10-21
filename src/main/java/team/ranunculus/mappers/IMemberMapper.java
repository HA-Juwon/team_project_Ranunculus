package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.member.MemberEntity;
import team.ranunculus.entities.member.ContactAuthEntity;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);


    MemberEntity selectUserByEmailPassword(MemberEntity member);
}
