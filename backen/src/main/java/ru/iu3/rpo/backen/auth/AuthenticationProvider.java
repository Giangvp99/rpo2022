package ru.iu3.rpo.backen.auth;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
        import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
        import org.springframework.security.core.AuthenticationException;
        import org.springframework.security.core.authority.AuthorityUtils;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.core.userdetails.UsernameNotFoundException;
        import org.springframework.security.web.authentication.www.NonceExpiredException;
        import org.springframework.stereotype.Component;
        import ru.iu3.rpo.backen.models.User;
        import ru.iu3.rpo.backen.repositories.UserRepository;

        import java.time.LocalDateTime;
        import java.util.Optional;
@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserRepository userRepository;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }
    @Override
    protected UserDetails retrieveUser(String userName, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws  AuthenticationException{
        Object token = usernamePasswordAuthenticationToken.getCredentials();
        Optional<User> currentUser = userRepository.findByToken(String.valueOf(token));
        if(!currentUser.isPresent())
            throw new UsernameNotFoundException("user is not found");
        User user = currentUser.get();

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.login, user.password, true, true, true, true, AuthorityUtils.createAuthorityList("USER"));
        return userDetails;
    }
}