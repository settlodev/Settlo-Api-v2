package co.tz.settlo.api.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // Log request details
        logRequest(wrappedRequest);

        // Continue with the filter chain
        filterChain.doFilter(wrappedRequest, response);
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        String requestBody = getRequestBody(request);
        String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        String headers = Collections.list(request.getHeaderNames())
                .stream()
                .map(headerName -> headerName + ": " + request.getHeader(headerName))
                .collect(Collectors.joining(", "));

        logger.info("REQUEST: {} {} {}\nHeaders: {}\nBody: {}",
                request.getMethod(),
                request.getRequestURI(),
                queryString,
                headers,
                requestBody);
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            try {
                return new String(content, request.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                logger.error("Failed to parse request body", e);
                return "[Error parsing request body]";
            }
        }
        return "";
    }
}
