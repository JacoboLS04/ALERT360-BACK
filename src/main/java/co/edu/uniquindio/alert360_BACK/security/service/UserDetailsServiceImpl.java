package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.entity.UserEntity;
import co.edu.uniquindio.alert360_BACK.security.repository.UserEntityRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserEntityRepositoy userEntityRepositoy;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userEntityRepositoy.findByUsernameOrEmail(username, username);
        if (!userEntity.isPresent()) {
        }
            return UserPrincipal.build(userEntity.get());
        }
}

