package team.ranunculus.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import team.ranunculus.entities.board.QnaEntity;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.services.BoardService;
import team.ranunculus.utils.CryptoUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller(value = "team.ranunculus.controllers.BoardController")
@RequestMapping(value = "/board")
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping(value = "qna", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getIndex(ModelAndView modelAndView) {
        List<QnaEntity> list = this.boardService.getList();
        modelAndView.addObject("list", list);
        modelAndView.setViewName("board/index");
        return modelAndView;
    }

    @RequestMapping(value = "qna", method = RequestMethod.POST)
    public String postIndex(ModelAndView modelAndView,
                            @RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(name = "page", required = false) Optional<Integer> optionalPage) {
        List<QnaEntity> list = this.boardService.search(search, keyword);
        modelAndView.clear();
        modelAndView.addObject("list", list);
        modelAndView.setViewName("board/index");
        JSONObject responseJson = new JSONObject();
        responseJson.put("list", modelAndView);
        return responseJson.toString();
    }

    @RequestMapping(value = "write", method = RequestMethod.GET)
    public ModelAndView getWrite(ModelAndView modelAndView,
                                 @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user) {

        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        } else {
            String name = user.getName();
            modelAndView.addObject("name", name);
        }
        modelAndView.setViewName("board/write");
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.POST)
    @ResponseBody
    public String postWrite(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                            @RequestParam(value = "writer") String writer,
                            @RequestParam(value = "password") String password,
                            @RequestParam(value = "title") String title,
                            @RequestParam(value = "content") String content,
                            QnaEntity board) {
        String hashPassword = CryptoUtils.hashSha512(board.getPassword());
        board.setIndex(-1)
                .setWriter(writer)
                .setEmail(user.getEmail())
                .setPassword(CryptoUtils.hashSha512(password))
                .setTitle(title)
                .setContent(content)
                .setPassword(hashPassword)
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

    @RequestMapping(value = "read/{id}", method = RequestMethod.GET)
    public ModelAndView getRead(@PathVariable(value = "id") int id,
                                ModelAndView modelAndView) {
        QnaEntity board = this.boardService.selectBoardByIndex(id);
        modelAndView.addObject("list", board);
        modelAndView.setViewName("board/read");
        return modelAndView;
    }


    @RequestMapping(value = "read/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteRead(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                             @PathVariable(value = "id") int id) {
        JSONObject responseJson = new JSONObject();
        QnaEntity qna = this.boardService.selectBoardByIndex(id);
        if (qna == null) {
            responseJson.put(IResult.ATTRIBUTE_NAME, CommonResult.FAILURE);
            return responseJson.toString();
        }
        if (user == null || !user.isAdmin() && !qna.getEmail().equals(user.getEmail())) {
            responseJson.put(IResult.ATTRIBUTE_NAME, "k");
            return responseJson.toString();
        }
        IResult result = this.boardService.deleteArticle(id);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.GET)
    public ModelAndView getModify(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                  @PathVariable(value = "id") int id,
                                  HttpServletResponse response,
                                  ModelAndView modelAndView) {
        QnaEntity qna = this.boardService.selectBoardByIndex(id);
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        if (user == null || !user.isAdmin() && !qna.getEmail().equals(user.getEmail())) {
            response.setStatus(403);
            return null;
        }
        modelAndView.addObject("qna", qna);
        modelAndView.setViewName("board/modify");
        return modelAndView;
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postModify(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
                             @PathVariable(value = "id") int id,
                             @RequestParam(value = "writer") String writer,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "content") String content,
                             QnaEntity qna) {
        JSONObject responseJson = new JSONObject();
        if (password.equals(qna.getPassword())) {
            qna.setIndex(id)
                    .setEmail(user.getEmail())
                    .setWriter(writer)
                    .setPassword(CryptoUtils.hashSha512(password))
                    .setTitle(title)
                    .setContent(content)
                    .setCreatedAt(new Date());
            IResult result = this.boardService.modifyArticle(qna);
            responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
            return responseJson.toString();
        } else {
            responseJson.put(IResult.ATTRIBUTE_NAME, "k");
            return responseJson.toString();
        }

    }
}