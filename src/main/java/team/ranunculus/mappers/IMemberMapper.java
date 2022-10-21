package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.member.MemberEntity;

@Mapper
public interface IMemberMapper {

    MemberEntity selectUserByEmailPassword(MemberEntity member);
}
