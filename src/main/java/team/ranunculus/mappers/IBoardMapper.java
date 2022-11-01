package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.board.BoardEntity;
import team.ranunculus.entities.member.UserEntity;

@Mapper
public interface IBoardMapper {
    int insertArticle(BoardEntity board);

    UserEntity selectUserAdminCheckByEmail(UserEntity user);
}
