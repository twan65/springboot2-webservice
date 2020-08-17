package jojoidu.boot.springboot.config.auth;

import jojoidu.boot.springboot.config.auth.dto.OAuthAttributes;
import jojoidu.boot.springboot.config.auth.dto.SessionUser;
import jojoidu.boot.springboot.domain.user.User;
import jojoidu.boot.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * Googleログイン後、ソーシャルサービスから取得したユーザー情報（name,email,pictureなど）をもとに
 * 入会及び情報修正、セッション保存などの機能を定義する。
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 現在、ログインしようとしているサービスを区分する。
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // Oauth2のログインする際、Keyとなりフィールド。Primary Keyのような意味。
        // Googleの場合は基本コード："sub"
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserServiceから取得したOauth2Userのattributeを保存するクラス
        OAuthAttributes attributes =
                OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // セッションにユーザー情報を保存
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey()))
                , attributes.getAttributes()
                , attributes.getNameAttributeKey());
    }

    /**
     * Emailでユーザーを検索し、存在ユーザーの場合は更新、存在しないユーザーは登録を行う。
     *
     * @param attributes ソーシャルサービスから取得したユーザー情報
     * @return ユーザーEntity
     */
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
