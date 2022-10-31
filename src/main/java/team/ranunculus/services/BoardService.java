package team.ranunculus.services;

import org.springframework.stereotype.Service;
import team.ranunculus.entities.board.BoardEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.mappers.IBoardMapper;

@Service
public class BoardService {
    private final IBoardMapper boardMapper;

    public BoardService(IBoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public IResult putArticle(BoardEntity board) {
        return this.boardMapper.insertArticle(board) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }
}
