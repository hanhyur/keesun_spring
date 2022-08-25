package me.gracenam;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

/*  @Bean
  public HandlerMapping handlerMapping() {
    RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
    handlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return handlerMapping;
  }

  @Bean
  public HandlerAdapter handlerAdapter() {
    RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
    return handlerAdapter;
  }*/

/*  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix("/WEB-INF/");
    viewResolver.setSuffix(".jsp");
    return viewResolver;
  }*/

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    registry.jsp("/WEB-INF", ".jsp");
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {

  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

  }
}