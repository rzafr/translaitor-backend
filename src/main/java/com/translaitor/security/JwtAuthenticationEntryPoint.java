package com.translaitor.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.translaitor.error.model.impl.ApiErrorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    /**
     * Handles the commencement of an authentication scheme.
     * This method is triggered when an authentication exception is thrown.
     * It sets the response status to 401 (Unauthorized) and returns a JSON representation of the error.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {

        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("application/json");

        ApiErrorImpl apiError = new ApiErrorImpl(HttpStatus.UNAUTHORIZED, e.getMessage());
        String strApiError = mapper.writeValueAsString(apiError);

        PrintWriter writer = httpServletResponse.getWriter();
        writer.println(strApiError);

    }
}
