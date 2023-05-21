package org.myf.wechatofficialaccountproject.interfaces.facade;

import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-05-21 22:03
 * @Description: CustomErrorFacade
 */
@Controller
public class CustomErrorFacade implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        if (Objects.nonNull(request.getSession())
            && Objects.nonNull(request.getSession().getAttribute(WeChatUtil.USER_NAME))
            && Objects.nonNull(request.getSession().getAttribute(WeChatUtil.USER_PHOTO))) {
            return "redirect:"  + "/ssfh/index?userName="
                + request.getSession().getAttribute(WeChatUtil.USER_NAME) + "&userPhoto="
                + request.getSession().getAttribute(WeChatUtil.USER_PHOTO) + "&loginPassword="
                    + request.getSession().getAttribute(WeChatUtil.LOGIN_PASSWORD);
        }
        return "redirect:" + "/ssfh/index";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
