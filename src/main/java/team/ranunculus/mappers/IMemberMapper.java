package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.member.MemberEntity;
import team.ranunculus.entities.member.ContactAuthEntity;
import team.ranunculus.entities.member.UserEntity;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);


    UserEntity selectUserByEmailPassword(UserEntity member);
}
