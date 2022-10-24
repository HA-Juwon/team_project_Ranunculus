package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.member.UserEntity;

@Mapper
public interface IMemberMapper {

    int insertUser(UserEntity user);

    UserEntity selectUserByEmail(UserEntity user);

    UserEntity selectUserByEmailPassword(UserEntity member);
}
