package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.member.MemberEntity;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);


    int insertUser(UserEntity user);

    UserEntity selectUserByEmail(UserEntity user);

    UserEntity selectUserByEmailPassword(UserEntity member);
}
