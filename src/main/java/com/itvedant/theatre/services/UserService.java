package com.itvedant.theatre.services;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itvedant.theatre.models.User;
import com.itvedant.theatre.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public User registerUser(User user) {

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException(
					"Email is already registered"
					);
		}

		user.setPassword(
				passwordEncoder.encode(user.getPassword())
				);

		user.setRole("USER");

		return userRepository.save(user);
	}
}
