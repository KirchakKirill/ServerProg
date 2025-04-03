package com.example.ServerProgLab34.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class RequestLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;



        logger.info("Incoming: {} {} | IP: {} | Agent: {}",
                req.getMethod(),
                req.getRequestURI(),
                req.getRemoteAddr(),
                req.getHeader("User-Agent"));

        long startTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        long duration = System.currentTimeMillis() - startTime;

        logger.info("Completed: {} {} | Status: {} | Time: {}ms",
                req.getMethod(),
                req.getRequestURI(),
                res.getStatus(),
                duration);
    }
}