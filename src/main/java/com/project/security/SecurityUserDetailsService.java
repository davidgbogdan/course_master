package com.project.security;

import com.project.repository.StudentRepository;
import com.project.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;

    @Override
    public SecurityUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return teacherRepository.findById(username)
                .map(teacher -> {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("TEACHER"));
                    return new SecurityUserDetails(teacher.getUsername(), teacher.getPassword(), authorities);
                })
                .orElseGet(() -> studentRepository.findById(username)
                        .map(student -> {
                            List<GrantedAuthority> authorities = new ArrayList<>();
                            authorities.add(new SimpleGrantedAuthority("STUDENT"));
                            return new SecurityUserDetails(student.getUsername(), student.getPassword(), authorities);
                        })
                        .orElseThrow(() -> new UsernameNotFoundException("Username or password invalid")));
    }
}
