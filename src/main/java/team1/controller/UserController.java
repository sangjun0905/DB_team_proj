package team1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team1.domain.user.User;
import team1.dto.user.UserCreateRequest;
import team1.dto.user.UserUpdateRequest;
import team1.service.user.UserService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody UserCreateRequest req) throws SQLException {
        return ResponseEntity.ok(userService.createUser(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) throws SQLException {
        User u = userService.getById(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }

    @GetMapping
    public ResponseEntity<User> getByEmail(@RequestParam("email") String email) throws SQLException {
        User u = userService.getByEmail(email);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProfile(@PathVariable String id, @RequestBody UserUpdateRequest req) throws SQLException {
        userService.updateProfile(id, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable String id) throws SQLException {
        userService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}
