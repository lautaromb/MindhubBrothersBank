package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/web/manager.html").hasAuthority("ADMIN")
                .antMatchers("/web/accounts.html", "/web/account.html", "/web/cards.html", "/web/create-cards.html", "/web/transfers.html").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET, "/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET, "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards/delete").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/accounts/delete/{id}").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/accounts/change/{id}").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.DELETE, "/api/cards").hasAuthority("CLIENT")
                //.antMatchers(HttpMethod.POST, "/api/cards").hasAuthority("CLIENT")

                //.antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/web/js/**", "/web/styles/**").permitAll();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");


        //desactiva el checkeo de CSRF tokens
        http.csrf().disable();

        // Desabilita frameOptions para que h2-console pueda ser accedida
        http.headers().frameOptions().disable();

        //Si el usuario no esta autenticado, solo envia una respuesta de fallo
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        //Si el login es exitoso, limpia las flags que piden autorización
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        //Si el login falla, solo envía la respuesta exitosa
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());


    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
}
