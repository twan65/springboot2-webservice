package jojoidu.boot.springboot.config.auth.dto;

import jojoidu.boot.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * 認証されたユーザーのみ保存するため、name,email,pictureのみ宣言
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
