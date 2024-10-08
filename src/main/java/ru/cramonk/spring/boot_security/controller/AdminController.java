package ru.cramonk.spring.boot_security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.cramonk.spring.boot_security.dto.UserDto;
import ru.cramonk.spring.boot_security.entity.Role;
import ru.cramonk.spring.boot_security.entity.User;
import ru.cramonk.spring.boot_security.service.RoleService;
import ru.cramonk.spring.boot_security.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }

    @GetMapping(value = "")
    public String getAdmin(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        UserDto userDto = UserDto.toDto(user);
        model.addAttribute("user", userDto);
        model.addAttribute("users", userService.findAll().stream()
                .map(UserDto::toDto).collect(Collectors.toList()));
        model.addAttribute("allRoles", roleService.findAll().stream()
                .map(Role::getName)
                .map(s->s.replace("ROLE_", ""))
                .collect(Collectors.toList()));
        return "admin";
    }

    @PostMapping(value = "/new")
    public String registerUser(@ModelAttribute("user") UserDto userDto) {
        User user = UserDto.fromDto(userDto);
        userService.register(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/update")
    public String updateUser(@ModelAttribute("user") UserDto userDto) {
        User user = UserDto.fromDto(userDto);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
