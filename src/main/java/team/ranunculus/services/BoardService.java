package team.ranunculus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.ranunculus.entities.board.BoardEntity;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.mappers.IBoardMapper;

import java.sql.SQLException;
import java.util.List;

@Service
public class BoardService {
    private final IBoardMapper boardMapper;

    @Autowired
    public BoardService(IBoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    //TODO : 댓글구현 할 때 참고
    public IResult userAdminCheck(UserEntity user) {
        UserEntity existingUser = this.boardMapper.selectUserAdminCheckByEmail(user);
        if (existingUser.getEmail() == null ||
                existingUser.isAdmin() == false) {
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }


    public IResult putArticle(BoardEntity board) {
        return this.boardMapper.insertArticle(board) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public List<BoardEntity> getList() {
        return this.boardMapper.getList();
    }

    public List<BoardEntity> getIndex() {
        return this.boardMapper.getIndex();
    }

}
