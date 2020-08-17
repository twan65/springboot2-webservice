package jojoidu.boot.springboot.web;

import jojoidu.boot.springboot.config.auth.dto.SessionUser;
import jojoidu.boot.springboot.web.dto.PostsResponseDto;
import jojoidu.boot.springboot.web.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    /**
     * TOP画面を表示（記事全部を取得）
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("posts", postsService.finAllDesc());

        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }

    /**
     * 記事登録画面を表示
     *
     * @return 記事登録画面のパス
     */
    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    /**
     * 記事更新画面を表示
     *
     * @param id 記事ID
     * @param model モデル
     * @return 記事更新画面のパス
     */
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable long id, Model model) {

        // 記事取得
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

}
