package team.ranunculus.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping(value = "qna", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("board/index");
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.GET)
    public ModelAndView getWrite(ModelAndView modelAndView,
                                 Model model) {
        model.addAttribute("list",boardService.getList((BoardEntity) model));
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
                            BoardEntity board) {
        String hashPassword = CryptoUtils.hashSha512(board.getPassword());
        board.setIndex(-1)
                .setWriter(writer)
                .setPassword(CryptoUtils.hashSha512(password))
                .setTitle(title)
                .setContent(content) // TODO: 값이 안넘어옴 체크 필요.
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

//    @RequestMapping(value = "image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String postImage(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
//                            @RequestParam(value = "upload") MultipartFile upload) throws
//            IOException {
//        ImageEntity image = ImageEntity.build()
//                .setUserEmail(user.getEmail())
//                .setCreatedAt(new Date())
//                .setName(upload.getOriginalFilename())
//                .setMime(upload.getContentType())
//                .setData(upload.getBytes());
//        IResult result = this.accompanyService.uploadImage(image);
//        JSONObject responseJson = new JSONObject();
//        if (result == CommonResult.SUCCESS) {
//            responseJson.put("url", String.format("http://localhost:8080/accompany/image/%d", image.getIndex()));
//        } else {
//            JSONObject errorJson = new JSONObject();
//            errorJson.put("message", "이미지 업로드에 실패하였습니다. 잠시 후 다시 시도해 주세요.");
//            responseJson.put("error", errorJson);
//        }
//        return responseJson.toString();
//    }
}