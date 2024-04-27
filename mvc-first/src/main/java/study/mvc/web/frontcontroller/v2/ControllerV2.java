package study.mvc.web.frontcontroller.v2;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import study.mvc.web.frontcontroller.MyView;

public interface ControllerV2 {
	MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}