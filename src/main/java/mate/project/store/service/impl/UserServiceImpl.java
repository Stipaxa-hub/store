package mate.project.store.service.impl;

import lombok.RequiredArgsConstructor;
import mate.project.store.dto.user.UserRegistrationRequestDto;
import mate.project.store.dto.user.UserResponseDto;
import mate.project.store.entity.User;
import mate.project.store.exception.RegistrationException;
import mate.project.store.mapper.UserMapper;
import mate.project.store.repository.user.UserRepository;
import mate.project.store.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
