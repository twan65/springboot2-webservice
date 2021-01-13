package jojoidu.boot.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jojoidu.boot.springboot.domain.posts.Posts;
import jojoidu.boot.springboot.domain.posts.PostsRepository;
import jojoidu.boot.springboot.web.dto.PostsSaveRequestDto;
import jojoidu.boot.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

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
    @WithMockUser(roles = "USER")
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

        // when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

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
    @WithMockUser(roles = "USER")
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
        // when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // データを取得して検証を行う。
        List<Posts> postsList = postsRepository.findAll();

        assertThat(postsList.get(0).getTitle()).isEqualTo(updateTitle);
        assertThat(postsList.get(0).getContent()).isEqualTo(updateContent);
    }
}
