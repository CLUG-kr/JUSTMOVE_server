package justmove.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping()
public class AuthController {

    @Value("${config.base-url}")
    private String baseUrl;

    @GetMapping("/auth/{registrationId}")
    public String loginViaGoogle(@PathVariable("registrationId") String registrationId, HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);
        return "redirect:/oauth2/authorization/" + registrationId;
    }
}
