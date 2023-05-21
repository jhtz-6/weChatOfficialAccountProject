package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.RequestLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19 21:57
 * @Description: RequestLogUtil
 */
public final class RequestLogUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogUtil.class);

    private static String[] mobileGateWayHeaders =
        {"ZXWAP", "chinamobile.com", "monternet.com", "infoX", "XMS 724Solutions HTG", "Bytemobile"};

    private static String[] pcHeaders =
        {"Windows 98", "Windows ME", "Windows 2000", "Windows XP", "Windows NT", "Ubuntu"};

    private static String[] mobileUserAgents =
        {"Nokia", "SAMSUNG", "MIDP-2", "CLDC1.1", "SymbianOS", "MAUI", "UNTRUSTED/1.0", "Windows CE", "iPhone", "iPad",
            "Android", "BlackBerry", "UCWEB", "ucweb", "BREW", "J2ME", "YULONG", "YuLong", "COOLPAD", "TIANYU", "TY-",
            "K-Touch", "Haier", "DOPOD", "Lenovo", "LENOVO", "HUAQIN", "AIGO-", "CTC/1.0", "CTC/2.0", "CMCC", "DAXIAN",
            "MOT-", "SonyEricsson", "GIONEE", "HTC", "ZTE", "HUAWEI", "webOS", "GoBrowser", "IEMobile", "WAP2.0"};

    public static RequestLogDTO buildRequestLogByHttpRequest(HttpServletRequest request) {
        RequestLogDTO requestLogDTO = new RequestLogDTO();
        try {
            requestLogDTO.setIpAddress(getIpByRequest(request));
            String clientType = isMobileDevice(request)
                ? "mobile" + (isIOSDevice(request) ? "-ios-" : "-android-") + (isWechat(request) ? "-wechat" : "")
                : "pc";
            requestLogDTO.setClientType(clientType);
            requestLogDTO.setUserAgent(request.getHeader("User-Agent"));
            requestLogDTO.setRequestUrl(request.getRequestURI());
            requestLogDTO.setRequestMethod(request.getMethod());
            requestLogDTO.setRequestParams(JSON.toJSONString(request.getParameterMap()));
            requestLogDTO.setLocation(getUserLocation(requestLogDTO.getIpAddress()));
            if(Objects.nonNull(request.getSession()) && Objects.nonNull(request.getSession().getAttribute(WeChatUtil.USER_NAME))){
                requestLogDTO.setUserName((String)request.getSession().getAttribute(WeChatUtil.USER_NAME));
            }
        } catch (Exception e) {
            LOGGER.error("buildRequestLogByHttpRequest.e", e);
        }

        return requestLogDTO;
    }

    private static String getIpByRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(",") > 0) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    private static boolean isWechat(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        return (userAgent != null) && (userAgent.indexOf("micromessenger") != -1);
    }

    private static boolean isMobileDevice(HttpServletRequest request) {
        boolean isMobile = false;
        boolean pcFlag = false;
        boolean mobileFlag = false;
        String via = request.getHeader("Via");
        String userAgent = request.getHeader("user-agent");
        for (int i = 0; (via != null) && (!via.trim().equals("")) && (i < mobileGateWayHeaders.length); i++) {
            if (via.contains(mobileGateWayHeaders[i])) {
                mobileFlag = true;
                break;
            }
        }
        for (int i = 0;
            (!mobileFlag) && (userAgent != null) && (!userAgent.trim().equals("")) && (i < mobileUserAgents.length);
            i++) {
            if (userAgent.contains(mobileUserAgents[i])) {
                mobileFlag = true;
                break;
            }
        }
        for (int i = 0; (userAgent != null) && (!userAgent.trim().equals("")) && (i < pcHeaders.length); i++) {
            if (userAgent.contains(pcHeaders[i])) {
                pcFlag = true;
                break;
            }
        }
        if ((mobileFlag) && (!pcFlag)) {
            isMobile = true;
        }
        return isMobile;
    }

    private static boolean isIOSDevice(HttpServletRequest request) {
        boolean isMobile = false;
        String[] ios_sys = {"iPhone", "iPad", "iPod"};
        String userAgent = request.getHeader("user-agent");
        for (int i = 0; (!isMobile) && (userAgent != null) && (!userAgent.trim().equals("")) && (i < ios_sys.length);
            i++) {
            if (userAgent.contains(ios_sys[i])) {
                isMobile = true;
                break;
            }
        }
        return isMobile;
    }

    private static String getUserLocation(String userIp) {
        if ("127.0.0.1".equals(userIp)) {
            return "本机";
        }
        String result = "";
        String location = "";
        try {
            BufferedReader in = null;
            if ("0:0:0:0:0:0:0:1".equals(userIp)) {
                userIp = "115.193.179.161";
            }
            String url =
                new StringBuilder().append("http://opendata.baidu.com/api.php?query=").append(userIp).toString();
            url = new StringBuilder().append(url)
                .append("&co=&resource_id=6006&t=1433920989928&ie=utf8&oe=utf8&format=json").toString();
            try {
                URL realUrl = new URL(url);
                URLConnection connection = realUrl.openConnection();
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.connect();
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result = new StringBuilder().append(result).append(line).toString();
                }
            } catch (Exception e) {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            location = (String)jsonObject1.get("location");
        } catch (Exception e) {
            LOGGER.error("getUserLocation.error:{},result:{}", e, result);
        }

        return location;
    }
}
