package org.myf.wechatofficialaccountproject.infrastructure.util.interceptor;

import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.RequestLogDO;
import org.myf.wechatofficialaccountproject.application.dto.RequestLogDTO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.RequestLogRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.UserRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.RequestLogUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19 19:28
 * @Description: LoginInterceptor
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    RequestLogRepository requestLogRepository;
    @Autowired
    UserRepository userRepository;

    private final static String SSFH = "/ssfh/";

    static ExecutorService requestLogThreadPoolExecutor;
    static {
        AtomicInteger requestLogThreadNumber = new AtomicInteger(1);
        requestLogThreadPoolExecutor =
            new ThreadPoolExecutor(15, 40, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<>(60), task -> {
                Thread thread = new Thread(task);
                thread.setName(
                    "SaveRequestLogThread-" + requestLogThreadNumber.incrementAndGet() + "-" + thread.getName());
                return thread;
            });
    }

    private static final List<String> NEED_TO_LOGIN_URL_LIST = Lists.newArrayList();
    private static final List<String> NEED_NO_LOGIN_URL_LIST = Lists.newArrayList();

    static {
        // todo 补全
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/queryAllData");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/index");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/synchronousData");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/updateFoodData");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/updateKeyWordData");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/updateAccompanyData");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/keyWordImport");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/keyWordExport");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/synchronousData");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/keyWordExport");
        NEED_TO_LOGIN_URL_LIST.add("/ssfh/keyWordImport");

        NEED_NO_LOGIN_URL_LIST.add("/weChat/msg");
        NEED_NO_LOGIN_URL_LIST.add("/weChat/YnssMsg");
        NEED_NO_LOGIN_URL_LIST.add("/weChat/menuExcel");
        NEED_NO_LOGIN_URL_LIST.add("/wx/menuExcel");
        NEED_NO_LOGIN_URL_LIST.add("/wx/menuexcel");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/login");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/queryKeyWordData");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/queryAccompanyData");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/queryKeyTypeEnum");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/queryCharacterTypeEnum");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/queryDepartmentEnum");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/queryBelongUserList");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/queryCatrgoryList");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/queryBooleanEnum");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/queryBelongUserList");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/chat");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/getMessageList");
        NEED_NO_LOGIN_URL_LIST.add("/ssfh/canSendMessage");


    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        requestLogThreadPoolExecutor.submit(new SaveRequestLog(request));
        Object userName = request.getSession().getAttribute(WeChatUtil.USER_NAME);
        if (Objects.isNull(userName) || WeChatUtil.DEFAULT_USER_NAME.equals(userName)) {
            if (WeChatUtil.DEFAULT_USER_NAME.equals(userName)) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("尚未登录,请点击左上角进行登录~~");
                return false;
            } else if (!needNoLogin(request.getRequestURI())) {
                response.setStatus(HttpStatus.SEE_OTHER.value());
                response.setHeader("Location", request.getContextPath() + "/ssfh/login");
                return false;
            }
        } else if (request.getRequestURI().contains("/ssfh/login")) {
            response.setStatus(HttpStatus.SEE_OTHER.value());
            response.setHeader("Location", request.getContextPath() + "/ssfh/index");
            return false;
        }
        return true;
    }

    private Boolean needToLogin(String url) {
        return NEED_TO_LOGIN_URL_LIST.stream().filter(x -> url.contains(x)).findAny().isPresent();
    }

    private Boolean needNoLogin(String url) {
        return NEED_NO_LOGIN_URL_LIST.stream().filter(x -> url.contains(x)).findAny().isPresent();
    }

    @AllArgsConstructor
    class SaveRequestLog implements Callable<Void> {

        private HttpServletRequest request;

        @Override
        public Void call() {
            RequestLogDTO requestLogDTO = RequestLogUtil.buildRequestLogByHttpRequest(request);
            RequestLogDO requestLogDO = new RequestLogDO();
            BeanUtils.copyProperties(requestLogDTO, requestLogDO);
            requestLogRepository.saveRequestLog(requestLogDO);
            return null;
        }
    }
}
