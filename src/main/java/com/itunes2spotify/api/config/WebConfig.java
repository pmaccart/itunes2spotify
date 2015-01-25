package com.itunes2spotify.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.itunes2spotify.api.converter.AccessTokenRequestConverter;
import com.itunes2spotify.api.endpoint.SpotifyEndpoint;
import com.itunes2spotify.api.endpoint.SpotifyRedirectEndpoint;
import com.itunes2spotify.api.service.SpotifyService;
import com.itunes2spotify.api.service.impl.SpotifyServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.sql.DataSource;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebMvc
@PropertySource("classpath:spotify.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
        ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
        ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
        contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);
        contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
        contentViewResolver.setDefaultViews(Arrays.<View> asList(new MappingJackson2JsonView()));
        return contentViewResolver;
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public SpotifyEndpoint spotifyEndpoint(@Qualifier("spotifyService") SpotifyService spotifyService) {
        SpotifyEndpoint endpoint = new SpotifyEndpoint();
        endpoint.setSpotifyService(spotifyService);
        return endpoint;
    }

    @Bean
    public SpotifyRedirectEndpoint spotifyRedirectEndpoint(@Qualifier("spotifyService") SpotifyService spotifyService) {
        SpotifyRedirectEndpoint endpoint = new SpotifyRedirectEndpoint();
        endpoint.setSpotifyService(spotifyService);
        return endpoint;
    }

    @Bean
    public SpotifyServiceImpl spotifyService(@Value("${spotifyServiceBaseUrl") String baseUrl,
                                         @Qualifier("spotifyRestTemplate") RestOperations spotifyRestTemplate) {
        SpotifyServiceImpl spotifyService = new SpotifyServiceImpl();
        spotifyService.setSpotifyRestTemplate(spotifyRestTemplate);
        return spotifyService;
    }

    @Bean
    public SpotifyServiceImpl spotifyRedirectService(@Qualifier("spotifyRedirectRestTemplate")
                                                     RestOperations spotifyRestTemplate, @Qualifier("trustedClientRestTemplate")
                                                     RestOperations trustedClientRestTemplate) {
        SpotifyServiceImpl spotifyService = new SpotifyServiceImpl();
        spotifyService.setSpotifyRestTemplate(spotifyRestTemplate);
        spotifyService.setTrustedClientRestTemplate(trustedClientRestTemplate);
        return spotifyService;
    }

    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
        conversionService.setConverters(Collections.singleton(new AccessTokenRequestConverter()));
        return conversionService;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new BufferedImageHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping();
        return objectMapper;
    }

    @Configuration
    @EnableOAuth2Client
    protected static class ResourceConfiguration {
        private String accessTokenUri;

        private String userAuthorizationUri;

        @Resource
        @Qualifier("accessTokenRequest")
        private AccessTokenRequest accessTokenRequest;

        @Bean
        public OAuth2ProtectedResourceDetails spotify(
                @Value("${spotify.accessTokenUri}") String accessTokenUri,
                @Value("${spotify.userAuthorizationUri}") String userAuthorizationUri,
                @Value("${spotify.clientId}") String clientId,
                @Value("${spotify.clientSecret}") String clientSecret
        ) {
            AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
            details.setId("spotify/itunes2spotify");
            details.setClientId(clientId);
            details.setClientSecret(clientSecret);
            details.setAccessTokenUri(accessTokenUri);
            details.setUserAuthorizationUri(userAuthorizationUri);
            details.setScope(Arrays.asList(
                    "user-read-private",
                    "user-read-email",
                    "user-library-modify",
                    "playlist-modify-private",
                    "playlist-modify-public"
            ));
//            details.setUseCurrentUri(false);
//            details.setPreEstablishedRedirectUri("http://localhost:8080/profile.html");
            return details;
        }

        @Bean
        public OAuth2ProtectedResourceDetails spotifyRedirect(
                @Value("${spotify.accessTokenUri}") String accessTokenUri,
                @Value("${spotify.userAuthorizationUri}") String userAuthorizationUri,
                @Value("${spotify.clientId}") String clientId,
                @Value("${spotify.clientSecret}") String clientSecret
        ) {
            AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
            details.setId("spotify/itunes2spotify-redirect");
            details.setClientId(clientId);
            details.setClientSecret(clientSecret);
            details.setAccessTokenUri(accessTokenUri);
            details.setUserAuthorizationUri(userAuthorizationUri);
            details.setScope(Arrays.asList(
                    "user-read-private",
                    "user-read-email",
                    "user-library-modify",
                    "playlist-modify-private",
                    "playlist-modify-public"
            ));
//            details.setUseCurrentUri(false);
//            details.setPreEstablishedRedirectUri("http://localhost:8080/profile.html");

            return details;
        }

        @Bean
        public OAuth2ProtectedResourceDetails trusted() {
            ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
            details.setId("sparklr/trusted");
            details.setClientId("my-client-with-registered-redirect");
            details.setAccessTokenUri(accessTokenUri);
            details.setScope(Arrays.asList("trust"));
            return details;
        }

        @Bean
        @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
        public OAuth2RestOperations spotifyRestTemplate(
                @Qualifier("spotify") OAuth2ProtectedResourceDetails spotify,
                @Qualifier("clientTokenServices") ClientTokenServices clientTokenServices) {
            OAuth2RestTemplate template = new OAuth2RestTemplate(spotify, new DefaultOAuth2ClientContext(accessTokenRequest));
            AccessTokenProviderChain provider = new AccessTokenProviderChain(Arrays.asList(new AuthorizationCodeAccessTokenProvider()));
            provider.setClientTokenServices(clientTokenServices);
            template.setAccessTokenProvider(provider);
            return template;
        }

        @Bean
        @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
        public OAuth2RestTemplate spotifyRedirectRestTemplate(
                @Qualifier("spotifyRedirect") OAuth2ProtectedResourceDetails spotifyRedirect,
                @Qualifier("clientTokenServices") ClientTokenServices clientTokenServices) {
            OAuth2RestTemplate template = new OAuth2RestTemplate(spotifyRedirect, new DefaultOAuth2ClientContext(accessTokenRequest));
            AccessTokenProviderChain provider = new AccessTokenProviderChain(Arrays.asList(new AuthorizationCodeAccessTokenProvider()));
            provider.setClientTokenServices(clientTokenServices);
            template.setAccessTokenProvider(provider);
            return template;
        }

        @Bean
        public OAuth2RestTemplate trustedClientRestTemplate() {
            return new OAuth2RestTemplate(trusted(), new DefaultOAuth2ClientContext());
        }

        @Bean
        public ClientTokenServices clientTokenServices(@Qualifier("dataSource") DataSource dataSource) {
            return new JdbcClientTokenServices(dataSource);
        }
    }

}
