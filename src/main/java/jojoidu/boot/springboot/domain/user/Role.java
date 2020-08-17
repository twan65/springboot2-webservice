package jojoidu.boot.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "ゲスト"),
    USER("ROLE_USER", "ユーザー");

    private final String key;
    private final String title;
}
