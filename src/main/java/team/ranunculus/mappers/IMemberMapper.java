package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.member.UserEntity;

@Mapper
public interface IMemberMapper {

    UserEntity selectUserByEmailPassword(UserEntity member);
}
