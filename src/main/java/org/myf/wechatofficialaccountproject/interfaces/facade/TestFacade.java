package org.myf.wechatofficialaccountproject.interfaces.facade;

import org.myf.wechatofficialaccountproject.interfaces.webservice.TestServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("testFacade")
public class TestFacade {

    @Autowired
    private TestServer testServer;

    @GetMapping("/index")
    public String index() {
        System.out.println("12");
        testServer.teacher("12");
        System.out.println(testServer);;
        return testServer.toString();
    }
}
