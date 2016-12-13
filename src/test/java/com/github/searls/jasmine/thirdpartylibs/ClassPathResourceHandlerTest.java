package com.github.searls.jasmine.thirdpartylibs;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static com.github.searls.jasmine.thirdpartylibs.ProjectClassLoaderHelper.projectClassLoaderOf;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class ClassPathResourceHandlerTest {
  @Mock
  private Request baseRequest;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private Resource resource;
  @Mock
  private PrintWriter writer;

  private ClassPathResourceHandler subject;

  @Before
  public void before() {
    String jquery = "src/test/resources/webjars/jquery-1.10.2.jar";
    subject = new ClassPathResourceHandler(projectClassLoaderOf(jquery));
  }

  @Test
  public void whenTargetStartsWithClassPathThenRespondWithResourceContent() throws Exception {
    // given
    when(baseRequest.isHandled()).thenReturn(false);
    when(response.getWriter()).thenReturn(writer);

    // when
    subject.handle("/META-INF/resources/webjars/jquery/1.10.2/jquery.js", baseRequest, request, response);

    // then
    verify(writer).write(contains("jQuery JavaScript Library v1.10.2"));
    verify(baseRequest).setHandled(true);
  }

  @Test
  public void whenResourceIsMissingThenDoNotProcess() throws Exception {
    // given
    when(baseRequest.isHandled()).thenReturn(false);
    when(response.getWriter()).thenReturn(writer);

    // when
    subject.handle("/notExistingResource", baseRequest, request, response);

    // then
    verify(writer, never()).write(anyString());
    verify(baseRequest, never()).setHandled(true);
  }
}
