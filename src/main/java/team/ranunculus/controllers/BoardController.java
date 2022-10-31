package team.ranunculus.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import team.ranunculus.entities.board.BoardEntity;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.services.BoardService;
import team.ranunculus.services.MemberService;
import team.ranunculus.utils.CryptoUtils;

import java.util.Date;

@Controller(value = "team.ranunculus.controllers.BoardController")
@RequestMapping(value = "/board")
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;

    @Autowired
    public BoardController(BoardService boardService, MemberService memberService) {
        this.boardService = boardService;
        this.memberService = memberService;
    }

    @RequestMapping(value = "qna", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("board/index");
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.GET)
    public ModelAndView getWrite(ModelAndView modelAndView) {
        modelAndView.setViewName("board/write");
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.POST)
    @ResponseBody
    public String postWrite(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                            @RequestParam(value = "write") String write,
                            @RequestParam(value = "password") String password,
                            @RequestParam(value = "title") String title,
                            @RequestParam(value = "content") String content,
                            BoardEntity board) {
        board.setIndex(-1)
                .setWriter(write)
                .setPassword(CryptoUtils.hashSha512(password))
                .setTitle(title)
                .setContent(content) // TODO: 값이 안넘어옴 체크 필요.
                .setCreatedAt(new Date())
                .setEmailAdminFlag(null);
        IResult result = this.boardService.putArticle(board);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("id", board.getIndex());
        }
        return responseJson.toString();
    }
}