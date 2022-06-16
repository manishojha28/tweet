package com.api.authorization.swagger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AuthSwaggerTest {

	@Mock
	private MockHttpServletRequest httpServletRequest;

	@Mock
	private MockHttpServletResponse httpServletResponse;


	@Test
	public void testDoFilterSwagger() throws IOException, ServletException {
		MockFilterChain filterChain = new MockFilterChain();
		Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/auth/swagger");
		SwaggerFilter swaggerFilter = new SwaggerFilter();
		swaggerFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
		filterChain.doFilter(httpServletRequest, httpServletResponse);
		Mockito.verify(httpServletResponse).sendRedirect("/swagger-ui.html");
	}

	@Test
	public void testSwaggerTrue() throws Exception {
		Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/checklist/swagger");
		assertEquals("/checklist/swagger", httpServletRequest.getRequestURI());

	}

}
