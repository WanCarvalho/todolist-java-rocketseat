package br.com.wancarvalho.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.wancarvalho.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // garante que o spring gerencie a classe
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks/")) {

            var authorization = request.getHeader("Authorization");

            // retirar a descricao do metodo de authz que vem na response
            var authEncoded = authorization.substring("Basic".length()).trim();

            // faz a decodificação da base64 para um array de caracteres
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            // transforma em string para manipular em seguida
            var authString = new String(authDecode);

            // manipula e separa as credenciais em um array de strings
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            // checa se o usuário possui cadastro no banco
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401);
            } else {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }

            }

        } else {
            filterChain.doFilter(request, response);
        }

    }

}
