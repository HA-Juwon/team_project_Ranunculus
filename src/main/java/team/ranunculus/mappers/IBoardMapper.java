package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.board.BoardEntity;
import team.ranunculus.entities.member.UserEntity;

import java.util.List;

@Mapper
public interface IBoardMapper {
    int insertArticle(BoardEntity board);

    UserEntity selectUserAdminCheckByEmail(UserEntity user);

    List<BoardEntity> getList();

    List<BoardEntity> getIndex();

    List<BoardEntity> searchTitle(String keyword);
    List<BoardEntity> searchContent(String keyword);
    List<BoardEntity> searchName(String keyword);
}
