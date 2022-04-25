package com.hoaxify.ws.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable(); // 403 Forbidden hatası engelleme

        //Browserda çıkan poppup'ı engellemek için .authauthenticationEntryPoint kısmından sonrası kullanılır.
        //http.httpBasic() kısmı authentication türümüzü belirtiyor.
        http.exceptionHandling().authenticationEntryPoint(new AuthEntryPoint());
        // authenticationEntryPoint authentication fail durumlarında client'a dönülecek cevabı yönetmek için kullanılır

        http.headers().frameOptions().disable();

        //Spring'e Authentication kontrolü yap diyoruz.
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/api/1.0/users/{username}").authenticated()
                .antMatchers(HttpMethod.POST, "/api/1.0/hoaxes").authenticated()
                .antMatchers(HttpMethod.POST, "/api/1.0/hoax-attachments").authenticated()
                .antMatchers(HttpMethod.POST, "/api/1.0/logout").authenticated()
                .and()
                .authorizeRequests().anyRequest().permitAll();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Her requestin içerisinde mutlaka creds'in gelmesine zorluyoruz.

        http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class); //UsernamePasswordAuthenticationFilter filtresinden önce çalıştır.
    }

    @Bean
        // BCryptPasswordEncoder'ı birden fazla yerde kullanmamıza yarar
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    TokenFilter tokenFilter() {
        return new TokenFilter();
    }
}
