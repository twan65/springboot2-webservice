package jojoidu.boot.springboot.web;

import jojoidu.boot.springboot.web.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    /**
     * TOP画面を表示（記事全部を取得）
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("posts", postsService.finAllDesc());

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }
}
