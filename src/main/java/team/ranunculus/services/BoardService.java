package team.ranunculus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.ranunculus.entities.board.BoardEntity;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.mappers.IBoardMapper;

@Service
public class BoardService {
    private final IBoardMapper boardMapper;

    @Autowired
    public BoardService(IBoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public String userAdminCheck(UserEntity user) {
        UserEntity existingUser = this.boardMapper.selectUserAdminCheckByEmail(user);
        return null;
    }

    public IResult putArticle(BoardEntity board) {
        return this.boardMapper.insertArticle(board) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }
}
