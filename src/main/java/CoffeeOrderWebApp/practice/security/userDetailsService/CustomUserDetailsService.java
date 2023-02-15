package CoffeeOrderWebApp.practice.security.userDetailsService;

import CoffeeOrderWebApp.practice.exception.ExceptionEnum;
import CoffeeOrderWebApp.practice.exception.LogicException;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.repository.MemberRepository;
import CoffeeOrderWebApp.practice.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //로그인 입력 정보를 바탕으로, DB에서 멤버 찾기
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member foundMember = optionalMember.orElseThrow(()->new LogicException(ExceptionEnum.MEMBER_NOT_FOUND));

        //userDetails 리턴
        return new CustomUserDetails(foundMember);
    }

    public final class CustomUserDetails extends Member implements UserDetails{ // final class -> 다른 클래스가 상속 불가능
        public CustomUserDetails(Member member) {
            //35번 라인에서 가져올 값. 반환할 값. 멤버아이디는 필요할 것 같음. 자기가 쓴 글 자기만 볼 수 있게 하려면..?
//            setMemberId(member.getMemberId());
//            setName(member.getName());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
