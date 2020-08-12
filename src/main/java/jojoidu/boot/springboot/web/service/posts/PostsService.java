package jojoidu.boot.springboot.web.service.posts;

import jojoidu.boot.springboot.domain.posts.Posts;
import jojoidu.boot.springboot.domain.posts.PostsRepository;
import jojoidu.boot.springboot.web.dto.PostsResponseDto;
import jojoidu.boot.springboot.web.dto.PostsSaveRequestDto;
import jojoidu.boot.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    /**
     * 記事を登録する。
     *
     * @param requestDto 記事詳細
     * @return 記事ID
     */
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    /**
     * 記事を更新する。
     *
     * @param id 記事ID
     * @param requestDto 更新記事詳細
     * @return 記事ID
     */
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        // 該当の記事を取得する。該当記事がない場合は「IllegalArgumentException」を発生させる。
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("該当の記事がありません。Id=" + id));

        // 特にSQLを投げなくてもJPAの永続性コンテキストのため、Updateされる。（Dirty checking）
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    /**
     * 記事を1件取得する。
     *
     * @param id 記事ID
     * @return 記事詳細
     */
    public PostsResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("該当の記事がありません。Id=" + id));

        return new PostsResponseDto(posts);
    }
}