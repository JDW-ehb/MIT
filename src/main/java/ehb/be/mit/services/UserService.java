package ehb.be.mit.services;

import ehb.be.mit.models.User;
import ehb.be.mit.repositories.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(IUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(String username,
                         String email,
                         String rawPassword,
                         String country) {

        if (repo.existsByEmail(email))
            throw new IllegalArgumentException("Email already used");

        if (repo.existsByUsername(username))
            throw new IllegalArgumentException("Username already used");

        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setCountry(country);
        user.setRole("USER");
        user.setPasswordHash(encoder.encode(rawPassword));

        return repo.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }
}
