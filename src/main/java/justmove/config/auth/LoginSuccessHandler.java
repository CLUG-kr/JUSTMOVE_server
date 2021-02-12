package justmove.config.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            String redirectUri = (String) session.getAttribute("prevPage");
            if (redirectUri != null) {
                session.removeAttribute("prevPage");
                getRedirectStrategy().sendRedirect(request, response, redirectUri);
            } else {
                getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/");
            }
        } else {
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/");
        }
    }
}
