
package rest.omms;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

public class InvalidRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // ==========================
        // CORS
        // ==========================

        String origin = req.getHeader("Origin");

        if (origin != null) {

            // Production frontend
            if (origin.equals("https://omms-production-f8de.up.railway.app")
                    || origin.equals("http://localhost:8080")) {

                res.setHeader("Access-Control-Allow-Origin", origin);
            }
        }

        res.setHeader("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS");

        res.setHeader("Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, Authorization, X-Requested-With");

        res.setHeader("Access-Control-Allow-Credentials", "true");

        res.setHeader("Access-Control-Max-Age", "3600");

        // Handle preflight request
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // ==========================
        // Existing logging
        // ==========================

        String method = req.getMethod();
        String context = req.getServletContext().getContextPath();
        String clientAddress = req.getRemoteAddr();

        System.out.println(
                "[" + method + "]"
                        + "[" + context + "]"
                        + "[" + clientAddress + "]");

        // Continue processing
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
