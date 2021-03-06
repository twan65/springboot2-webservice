package jojoidu.boot.springboot.domain.posts;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After // 単体テストが終わるたびに実行されるメソッド
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void saveContent_getContent() {
        // given
        String title = "テストコンテンツ";
        String content = "テスト本文";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("example@test1.com")
                .build());

        // テーブルPostsにあるすべてのデーtを取得する。
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);

        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void saveBaseTimeEntity() {
        // given
        LocalDateTime now = LocalDateTime.of(2020, 8,12,0,0,0);
        postsRepository.save(Posts.builder()
        .title("title")
        .content("content")
        .author("author")
        .build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>createdDate=" + posts.getCreatedDate() + ", modifiedDate=" + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}
