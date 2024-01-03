package mate.project.store.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.user.UserRegistrationRequestDto;
import mate.project.store.dto.user.UserResponseDto;
import mate.project.store.entity.Role;
import mate.project.store.entity.User;
import mate.project.store.entity.enums.RoleName;
import mate.project.store.exception.EntityNotFoundException;
import mate.project.store.exception.RegistrationException;
import mate.project.store.mapper.UserMapper;
import mate.project.store.repository.role.RoleRepository;
import mate.project.store.repository.user.UserRepository;
import mate.project.store.service.ShoppingCartService;
import mate.project.store.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        Role defaultRoleForNewUser = roleRepository.findByRoleName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find default role"));
        user.setRoles(Set.of(defaultRoleForNewUser));
        User savedUser = userRepository.save(user);
        shoppingCartService.createShoppingCart(savedUser);
        return userMapper.toDto(savedUser);
    }
}
