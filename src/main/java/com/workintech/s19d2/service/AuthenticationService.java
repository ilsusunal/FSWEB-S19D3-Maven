package com.workintech.s19d2.service;

import com.workintech.s19d2.repository.MemberRepository;
import com.workintech.s19d2.repository.RoleRepository;
import com.workintech.s19d2.entity.Member;
import com.workintech.s19d2.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService {
    private MemberRepository memberRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public AuthenticationService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Member register(String email, String password){
        String encodedPassword = passwordEncoder.encode(password);

        Optional<Member> userExisting = memberRepository.findByEmail(email);
        if (userExisting.isPresent()){
            throw new RuntimeException("User with given email already exist");
        }

        List<Role> roles = new ArrayList<>();

//        Optional<Role> role1 = roleRepository.findByAuthority("USER");
//        if(role1.isPresent()){
//            roles.add(role1.get());
//        }else {
//            Role userRole = new Role();
//            userRole.setAuthority("USER");
//            roles.add(roleRepository.save(userRole));
//        }

        Optional<Role> role2 = roleRepository.findByAuthority("ADMIN");
        if(role2.isPresent()){
            roles.add(role2.get());
        }else {
            Role adminRole = new Role();
            adminRole.setAuthority("ADMIN");
            roles.add(roleRepository.save(adminRole));
        }


        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);
        member.setRoles(roles);

        return memberRepository.save(member);
    }
}
