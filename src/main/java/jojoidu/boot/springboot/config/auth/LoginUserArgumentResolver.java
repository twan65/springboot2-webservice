package jojoidu.boot.springboot.config.auth;

import jojoidu.boot.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    /**
     * コントローラのメソッドの特定パラメータを支援しているかを判断する。
     * ここではパラメータに＠LoginUserアノテーションがついていてパラメータのクラスタイプがSessionUser.classの場合true
     *
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    /**
     * パラメータに渡すオブジェクトを生成する。
     * ここではセッションからオブジェクトを持ってくる。
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                           NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

        return httpSession.getAttribute("user");
    }
}
