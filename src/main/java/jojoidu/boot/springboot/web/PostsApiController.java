package jojoidu.boot.springboot.web;

import jojoidu.boot.springboot.web.dto.PostsResponseDto;
import jojoidu.boot.springboot.web.dto.PostsSaveRequestDto;
import jojoidu.boot.springboot.web.dto.PostsUpdateRequestDto;
import jojoidu.boot.springboot.web.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    /**
     * 記事登録を行う。
     *
     * @param requestDto 記事詳細
     * @return 記事ID
     */
    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    /**
     * 記事を更新する。
     *
     * @param id 記事ID
     * @param requestDto 更新記事詳細
     * @return 記事ID
     */
    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }


    /**
     * 記事を取得する。
     *
     * @param id 記事ID
     * @return 記事
     */
    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto update(@PathVariable Long id) {
        return postsService.findById(id);
    }
}
