package com.itvedant.theatre.controllers;
import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itvedant.theatre.models.User;
import com.itvedant.theatre.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final UserService userService;
	public AuthController(UserService userService) {
		this.userService = userService;
	}

	public static class RegisterRequest {

		@NotBlank
		@Size(max = 100)
		private String name;

		@NotBlank
		@Email
		@Size(max = 150)
		private String email;

		@NotBlank
		@Size(min = 8, max = 72)
		private String password;

		public RegisterRequest() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> register(
			@Valid @RequestBody RegisterRequest request) {

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());

		User savedUser = userService.registerUser(user);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of(
						"message", "Registration successful",
						"id", savedUser.getId(),
						"name", savedUser.getName(),
						"email", savedUser.getEmail()
						));
	}

	@GetMapping("/welcome")
	public Map<String, String> welcome(
			Authentication authentication) {

		return Map.of(
				"message", "Welcome to Theatre Management System",
				"email", authentication.getName()
				);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleException(
			IllegalArgumentException exception) {

		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(Map.of("error", exception.getMessage()));
	}
}
