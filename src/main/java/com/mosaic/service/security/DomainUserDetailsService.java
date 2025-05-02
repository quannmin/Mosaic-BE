package com.mosaic.service.security;

import com.mosaic.entity.User;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("userDetailService")
@RequiredArgsConstructor
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String input) throws UsernameNotFoundException {
        User user;
        if(new EmailValidator().isValid(input, null)) {
            user = userRepository.findUerByEmailOrUserNameOrPhoneNumber(input, input, input)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", input));
        } else if(isPhoneNumber(input)) {
            user = userRepository.findUerByEmailOrUserNameOrPhoneNumber(input, input, input)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "phone number", input));
        } else {
            user = userRepository.findUerByEmailOrUserNameOrPhoneNumber(input, input, input)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "user name", input));
        }

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        return new CustomUserDetails(input, user.getPassword(), grantedAuthorities, user.getId());
    }

    @Getter
    public static class CustomUserDetails extends org.springframework.security.core.userdetails.User {
        private final Long userId;

        public CustomUserDetails(String username, String password,
                                 Collection<? extends GrantedAuthority> authorities,
                                 Long userId) {
            super(username, password, authorities);
            this.userId = userId;
        }
    }

    private boolean isPhoneNumber(String input) {
        return input.matches("^(\\+84|0)[0-9]{9,10}$");
    }


}
