package com.objects.marketbridge.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.common.utils.ClientIpUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LoggingFilter extends OncePerRequestFilter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    protected static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        MDC.put("traceId", UUID.randomUUID().toString());
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(new RequestWrapper(request), new ResponseWrapper(response), filterChain);
        }
        MDC.clear();
    }

    protected void doFilterWrapped(RequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            logRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            logResponse(response);
            response.copyBodyToResponse();
        }
    }

    private static void logRequest(RequestWrapper request) throws IOException {
        String queryString = request.getQueryString();

        ClientIpUtils.getClientIps(request)
                .forEach((headerType, ip) ->
                        log.info("{} : {}", headerType, ip));
        log.info("Request : {} uri=[{}] content-type=[{}]", request.getMethod(), queryString == null ? request.getRequestURI() : request.getRequestURI() + queryString, request.getContentType());

        logPayload("Request", request.getContentType(), request.getInputStream());
    }

    private static void logResponse(ContentCachingResponseWrapper response) throws IOException {
        logPayload("Response", response.getContentType(), response.getContentInputStream());
    }

    private static void logPayload(String prefix, String contentType, InputStream inputStream) throws IOException {
        boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));
        if (visible) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            if (content.length > 0) {
                String contentString = new String(content);
                try {
                    Object jsonObject = objectMapper.readValue(contentString, Object.class);
                    String prettyPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    log.info("{} Payload: {}", prefix, prettyPayload);
                } catch (IOException e) {
                    // JSON 변환 중 에러 발생 시 예외 처리
                    log.warn("Error occurred while formatting payload: {}", e.getMessage());
                    log.info("{} Payload: {}", prefix, contentString); // 예외 발생 시 원본 payload 출력
                }
            }
        } else {
            log.info("{} Payload: Binary Content", prefix);
        }
    }

    private static boolean isVisible(MediaType mediaType) {
        final List<MediaType> VISIBLE_TYPES = Arrays.asList(MediaType.valueOf("text/*"), MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.valueOf("application/*+json"), MediaType.valueOf("application/*+xml"), MediaType.MULTIPART_FORM_DATA);
        return VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    }
}