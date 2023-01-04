package team.ranunculus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.ranunculus.entities.board.QnaEntity;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.mappers.IBoardMapper;

import java.util.List;

@Service
public class BoardService {
    private final IBoardMapper boardMapper;

    @Autowired
    public BoardService(IBoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public IResult userAdminCheck(UserEntity user) {
        UserEntity existingUser = this.boardMapper.selectUserAdminCheckByEmail(user);
        if (existingUser.getEmail() == null ||
                existingUser.isAdmin() == false) {
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }


    public IResult putArticle(QnaEntity board) {
        return this.boardMapper.insertArticle(board) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public List<QnaEntity> getList() {
        return this.boardMapper.getList();
    }

    public QnaEntity selectBoardByIndex(int id) {
        return this.boardMapper.selectBoardByIndex(id);
    }

    public List<QnaEntity> getIndex() {
        return this.boardMapper.getIndex();
    }

    public QnaEntity getIndex2(int boardIndex) {
        return this.boardMapper.selectBoardByIndex(boardIndex);
    }

    public List<QnaEntity> search(String search, String keyword) {
        if (search.equals("name")) {
            return this.boardMapper.searchName(keyword);
        } else if (search.equals("title")) {
            return this.boardMapper.searchTitle(keyword);
        } else if (search.equals("content")) {
            return this.boardMapper.searchContent(keyword);
        } else {
            return null;
        }
    }

    public IResult deleteArticle(int index) {
        return this.boardMapper.deleteArticle(index) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult modifyArticle(QnaEntity board) {
        return this.boardMapper.updateArticle(board) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }


}