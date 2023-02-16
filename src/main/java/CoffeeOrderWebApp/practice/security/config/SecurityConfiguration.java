package CoffeeOrderWebApp.practice.security.config;

import CoffeeOrderWebApp.practice.member.service.MemberService;
import CoffeeOrderWebApp.practice.security.filter.CustomAuthenticationFilter;
import CoffeeOrderWebApp.practice.security.filter.VerificationFilter;
import CoffeeOrderWebApp.practice.security.handler.*;
import CoffeeOrderWebApp.practice.security.jwt.JwtTokenizer;
import CoffeeOrderWebApp.practice.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Value("${spring.security.oauth2.client.registration.google.clientId}")  // (1)
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}") // (2)
    private String clientSecret;
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        frameOptions()는 HTML 태그 중에서 <frame>이나 <iframe>, <object> 태그에서 페이지를 렌더링 할지의 여부를 결정하는 기능을 합니다.
//        Spring Security에서는 Clickjacking 공격을 막기 위해 기본적으로 frameOptions() 기능이 활성화 되어 있으며 디폴트 값은 DENY입니다.
//        즉, 위에서 언급한 HTML 태그를 이용한 페이지 렌더링을 허용하지 않겠다는 의미입니다.
//        .frameOptions().sameOrigin()을 호출하면 동일 출처로부터 들어오는 request만 페이지 렌더링을 허용합니다.
//        H2 웹 콘솔의 화면 자체가 내부적으로 태그를 사용하고 있기 때문에 개발 환경에서는 H2 웹 콘솔을 정상적으로 사용할 수 있도록 (1)과 같이 설정하면 됩니다.
        // 그럼 배포 환경에서는 저 옵션 안 씀?
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/*/members").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/*/members/**").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/*/members").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/*/members/**").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/*/members/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/*/coffees").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/*/coffees/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/*/coffees/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/*/coffees/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/*/orders").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH, "/*/orders/*").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/*/orders").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/*/orders/**").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/*/orders/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/*/question").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH, "/*/question/*").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/*/question/**").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/*/question/**").permitAll()
                        .antMatchers("/*/answer/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                ).oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2SuccessHandler(jwtTokenizer, authorityUtils, memberService)));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:80"));
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "PATCH", "GET", "DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {  // (2-1)
        @Override
        public void configure(HttpSecurity builder) throws Exception {  // (2-2)
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);  // (2-3)

            CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(jwtTokenizer, authenticationManager);  // (2-4)
            customAuthenticationFilter.setFilterProcessesUrl("/jmt/login");          // (2-5)
            customAuthenticationFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());  // (3) 추가
            customAuthenticationFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());

            VerificationFilter verificationFilter = new VerificationFilter(jwtTokenizer, authorityUtils);

            builder
                    .addFilter(customAuthenticationFilter)
                    .addFilterAfter(verificationFilter, OAuth2LoginAuthenticationFilter.class)
                    .addFilterAfter(verificationFilter, CustomAuthenticationFilter.class);
        }
    }

    // (3)
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        var clientRegistration = clientRegistration();    // (3-1)

        return new InMemoryClientRegistrationRepository(clientRegistration);   // (3-2)
    }

    // (4)
    private ClientRegistration clientRegistration() {
        // (4-1)
        return CommonOAuth2Provider
                .GOOGLE
                .getBuilder("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}

