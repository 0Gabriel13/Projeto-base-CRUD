package com.sistema.empresarial.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }
    
    @GetMapping("/home")
    public String home(Model model) {
        // Obtém as informações de autenticação do usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            // Adiciona o nome do usuário ao modelo para exibição na página
            String username = authentication.getName();
            model.addAttribute("username", username);
            
            // Você pode adicionar mais informações do usuário se necessário
            // model.addAttribute("userDetails", authentication.getPrincipal());
        }
        
        return "home";
    }
}