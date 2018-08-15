package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model)
    {
        User user = new User();
        model.addAttribute("user",user);
        return "registration";
    }

    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model)
    {
        model.addAttribute("user", user);
        if (result.hasErrors())
        {
            return "registration";
        }
        else
        {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "index";
    }


    @RequestMapping("/")
    public String index()
    {
        return "index";
    }

    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }

    @RequestMapping("/secure")
    public String secure()
    {
        return "secure";
    }

    @RequestMapping("/bobspage")
    public String bobspage(){

        String page = "index";
        if (getUser().getUsername().equalsIgnoreCase("bob")){
            page = "bobspage";
        }

        return page;

    }
    private User getUser(Authentication authentication){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           User user = userRepository.findByUsername(authentication.getName());
        return user;
    }
}


