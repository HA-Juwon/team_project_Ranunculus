package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.board.BoardEntity;

@Mapper
public interface IBoardMapper {
    int insertArticle(BoardEntity board);
}
