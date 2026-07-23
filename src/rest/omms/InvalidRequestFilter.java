package rest.omms;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(
    urlPatterns = "/*",
    dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.ERROR
    }
)
public class InvalidRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req =
            (HttpServletRequest) request;

        HttpServletResponse res =
            (HttpServletResponse) response;

        String origin = req.getHeader("Origin");

        boolean allowedOrigin =
            "https://omms-production-f8de.up.railway.app"
                .equals(origin)
            || "https://omms-805402377067.asia-southeast1.run.app".equals(origin)
            || "http://localhost:8080".equals(origin);

        if (allowedOrigin) {
            res.setHeader(
                "Access-Control-Allow-Origin",
                origin
            );

            res.setHeader(
                "Access-Control-Allow-Credentials",
                "true"
            );

            res.setHeader("Vary", "Origin");
        }

        res.setHeader(
            "Access-Control-Allow-Methods",
            "GET, POST, PUT, DELETE, OPTIONS"
        );

        res.setHeader(
            "Access-Control-Allow-Headers",
            "Origin, Content-Type, Accept, Authorization, " +
            "X-Requested-With"
        );

        res.setHeader(
            "Access-Control-Max-Age",
            "3600"
        );

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        String method = req.getMethod();
        String context = req.getContextPath();
        String clientAddress = req.getRemoteAddr();

        System.out.println(
            "[" + method + "]" +
            "[" + context + "]" +
            "[" + clientAddress + "]"
        );

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}