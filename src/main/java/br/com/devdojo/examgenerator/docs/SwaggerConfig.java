//package br.com.devdojo.examgenerator.docs;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//@Configuration
//public class SwaggerConfig {
//	
//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.apis(RequestHandlerSelectors.any())
//				.paths(PathSelectors.any())
//				.build()
//				.apiInfo(metaData());
//	}
//	
//	private ApiInfo metaData() {
//		return new ApiInfoBuilder()
//				.title("Exam generator by DevDojo")
//				.description("Software to generate exams based on questions")
//				.version("1.0")
//				.contact(new Contact("David Porto", "http", "david2@hotmail.com"))
//				.license("Apache License Version 2.0")
//				.licenseUrl("https://www.apache.org/licenses/LICENSE")
//				.build();
//	}
	
//	@Bean
//	public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
//	    return new BeanPostProcessor() {
//
//	        @Override
//	        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//	            if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
//	                customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
//	            }
//	            return bean;
//	        }
//
//	        private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
//	            List<T> copy = mappings.stream()
//	                .filter(mapping -> mapping.getPatternParser() == null)
//	                .collect(Collectors.toList());
//	            mappings.clear();
//	            mappings.addAll(copy);
//	        }
//
//	        @SuppressWarnings("unchecked")
//	        private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
//	            try {
//	                Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
//	                field.setAccessible(true);
//	                return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
//	            } catch (IllegalArgumentException | IllegalAccessException e) {
//	                throw new IllegalStateException(e);
//	            }
//	        }
//	    };
//	}
//}
