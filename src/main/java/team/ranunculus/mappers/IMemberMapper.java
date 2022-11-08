package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.member.ContactAuthEntity;
import team.ranunculus.entities.member.TelecomEntity;
import team.ranunculus.entities.member.UserEntity;

import java.util.Map;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);

    int insertUser(UserEntity user);

    UserEntity selectUserByEmail(UserEntity user);

    UserEntity selectUserBySessionId(UserEntity user);

    void updateAutoLogin(Map<String,Object>map);

    UserEntity selectUserByEmailPassword(UserEntity user);

    UserEntity selectUserByContact(UserEntity user);

    ContactAuthEntity selectContactAuthByContactCodeSalt(ContactAuthEntity contactAuth);

    TelecomEntity[] selectTelecoms ();

    int updateContactAuth(ContactAuthEntity contactAuth);

    int updateUser(UserEntity user);
}
