package jojoidu.boot.springboot.web;

import jojoidu.boot.springboot.domain.posts.Posts;
import jojoidu.boot.springboot.domain.posts.PostsRepository;
import jojoidu.boot.springboot.web.dto.PostsSaveRequestDto;
import jojoidu.boot.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    /**
     * 記事登録テストを行う。
     *
     * @throws Exception
     */
    @Test
    public void savePosts() throws Exception {
        // given
        String title = "title";
        String content = "content";

        // リクエストパラメータを作成
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        // URLを作成
        String url = "http://localhost:" + port + "/api/v1/posts";

        // RestTemplateを利用してAPIを呼び出す。（URL、パラメータ、戻り値の型）
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // 検証
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        // データを取得して検証を行う。
        List<Posts> postsList = postsRepository.findAll();

        assertThat(postsList.get(0).getTitle()).isEqualTo(title);
        assertThat(postsList.get(0).getContent()).isEqualTo(content);
    }

    /**
     * 記事更新テストを行う。
     *
     * @throws Exception
     */
    @Test
    public void updatePosts() throws Exception {

        // 記事登録
        Posts savePosts = postsRepository.save(Posts.builder()
        .title("title")
        .content("content")
        .author("author")
        .build());

        Long updateId = savePosts.getId();
        String updateTitle = "title2";
        String updateContent = "content2";

        // リクエストパラメータを作成
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(updateTitle)
                .content(updateContent)
                .build();

        // URLを作成
        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // RestTemplateを利用してAPIを呼び出す。（URL、HTTPメソッド、パラメータ、戻り値の型）
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // 検証
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        // データを取得して検証を行う。
        List<Posts> postsList = postsRepository.findAll();

        assertThat(postsList.get(0).getTitle()).isEqualTo(updateTitle);
        assertThat(postsList.get(0).getContent()).isEqualTo(updateContent);
    }
}
