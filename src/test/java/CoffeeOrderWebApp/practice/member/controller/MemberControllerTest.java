package CoffeeOrderWebApp.practice.member.controller;

import CoffeeOrderWebApp.practice.member.dto.MemberDto;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.mapper.MemberMapper;
import CoffeeOrderWebApp.practice.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MemberController.class) //슬라이스
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    Gson gson = new Gson();

    @Test
    public void postMember() throws Exception {
        MemberDto.postDto postDto = MemberDto.postDto.builder()
                .phone("010-1111-1111")
                .name("조영롱")
                .email("www@naver.com").build();

        String content = gson.toJson(postDto);

        Member member = new Member();
        member.setMemberId(1L);

        given(mapper.MemberPostDtoToMember(Mockito.any(MemberDto.postDto.class))).willReturn(new Member());
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(member);

        mockMvc.perform(
                post("/jmt/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isCreated())
        .andExpect(header().exists(HttpHeaders.LOCATION));
    }

    @Test
    void patchMember() {
    }

    @Test
    void getMember() {
    }

    @Test
    void getMembers() {
    }

    @Test
    void deleteMember() {
    }
}