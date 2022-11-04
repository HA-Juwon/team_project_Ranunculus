package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.board.QnaEntity;
import team.ranunculus.entities.member.UserEntity;

import java.util.List;

@Mapper
public interface IBoardMapper {
    int insertArticle(QnaEntity board);

    UserEntity selectUserAdminCheckByEmail(UserEntity user);

    List<QnaEntity> getList();

    List<QnaEntity> getIndex();

    List<QnaEntity> searchTitle(String keyword);
    List<QnaEntity> searchContent(String keyword);
    List<QnaEntity> searchName(String keyword);
}
