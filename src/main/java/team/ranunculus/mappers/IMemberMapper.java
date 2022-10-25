package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.member.ContactAuthEntity;
import team.ranunculus.entities.member.UserEntity;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);

    int insertUser(UserEntity user);

    ContactAuthEntity selectContactAuthByContactCodeSalt(ContactAuthEntity contactAuth);

    UserEntity selectUserByContact(UserEntity user);

    UserEntity selectUserByEmail(UserEntity user);

    UserEntity selectUserByEmailPassword(UserEntity member);

    int updateContactAuth(ContactAuthEntity contactAuth);
}
