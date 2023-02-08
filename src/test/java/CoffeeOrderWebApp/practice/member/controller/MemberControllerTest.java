package CoffeeOrderWebApp.practice.member.controller;

import CoffeeOrderWebApp.practice.member.dto.MemberDto;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.member.mapper.MemberMapper;
import CoffeeOrderWebApp.practice.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = MemberController.class) //슬라이스
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    @Test
    public void postMember() throws Exception {
        ConstraintDescriptions requestConstraints = new ConstraintDescriptions(MemberDto.postDto.class);
        List<String> nameAttribute = requestConstraints.descriptionsForProperty("name");
        List<String> emailAttribute = requestConstraints.descriptionsForProperty("email");
        List<String> phoneAttribute = requestConstraints.descriptionsForProperty("phone");
        List<String> passwordAttribute = requestConstraints.descriptionsForProperty("password");

        MemberDto.postDto postDto = MemberDto.postDto.builder()
                .phone("010-1111-1111")
                .name("조영롱")
                .email("www@naver.com")
                .password("1111111").build();

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
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andDo(document("post-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름")
                                        .attributes(key("constraints").value(nameAttribute)),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                                        .attributes(key("constraints").value(emailAttribute)),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호")
                                        .attributes(key("constraints").value(phoneAttribute)),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀 번호")
                                        .attributes(key("constraints").value(passwordAttribute))
                        ),
                        responseHeaders(
                                headerWithName("location").description("회원 정보 엔드포인트")
                        )
                ));
    }

    @Test
    void patchMember() throws Exception {
        ConstraintDescriptions requestConstraints = new ConstraintDescriptions(MemberDto.patchDto.class);
        List<String> nameAttribute = requestConstraints.descriptionsForProperty("name");
        List<String> phoneAttribute = requestConstraints.descriptionsForProperty("phone");

        //given
        MemberDto.patchDto patchDto = MemberDto.patchDto.builder()
                .name("영롱").build();
        String content = gson.toJson(patchDto);

        long memberId = 1L;
        MemberDto.responseDto response = MemberDto.responseDto.builder()
                .memberId(memberId).name("영롱").email("aaa@naver.com")
                .phone("010-1111-2222").stampCount(6).status(Member.Status.MEMBER_ACTIVE.getStatus())
                .createdAt(LocalDateTime.of(2023, 4, 3, 3, 3, 0))
                .modifiedAt(LocalDateTime.of(2023, 4, 3, 3, 3, 0)).build();

        given(mapper.MemberPatchDtoToMember(Mockito.any(MemberDto.patchDto.class))).willReturn(new Member());
        given(memberService.modifyMember(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.MemberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when, then
        mockMvc.perform(
                patch("/jmt/member/" + memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("memberId").value(memberId))
                .andExpect(jsonPath("name").value("영롱"))
                .andExpect(jsonPath("email").value("aaa@naver.com"))
                .andExpect(jsonPath("phone").value("010-1111-2222"))
                .andExpect(jsonPath("stampCount").value(6))
                .andExpect(jsonPath("status").value(Member.Status.MEMBER_ACTIVE.getStatus()))
                .andExpect(jsonPath("createdAt").value("2023-04-03T03:03:00"))
                .andExpect(jsonPath("modifiedAt").value("2023-04-03T03:03:00"))
                .andDo(document("patch-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름")
                                        .attributes(key("constraints").value(nameAttribute)).optional(),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호")
                                        .attributes(key("constraints").value(phoneAttribute)).optional()
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("stampCount").type(JsonFieldType.NUMBER).description("스탬프 갯수"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("회원 상태"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("회원 가입 날짜"),
                                fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("회원 정보 업데이트 날짜")
                        )
                ));
    }

    @Test
    void getMember() throws Exception {
        //given
        long memberId = 1L;
        MemberDto.responseDto response = MemberDto.responseDto.builder()
                .memberId(memberId).name("영롱").email("aaa@naver.com")
                .phone("010-1111-2222").stampCount(6).status(Member.Status.MEMBER_ACTIVE.getStatus())
                .createdAt(LocalDateTime.of(2023, 4, 3, 3, 3, 0))
                .modifiedAt(LocalDateTime.of(2023, 4, 3, 3, 3, 0)).build();

        given(memberService.getMember(Mockito.anyLong())).willReturn(new Member());
        given(mapper.MemberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/jmt/member/{member-id}", memberId)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("memberId").value(memberId))
                .andExpect(jsonPath("name").value("영롱"))
                .andExpect(jsonPath("email").value("aaa@naver.com"))
                .andExpect(jsonPath("phone").value("010-1111-2222"))
                .andExpect(jsonPath("stampCount").value(6))
                .andExpect(jsonPath("status").value(Member.Status.MEMBER_ACTIVE.getStatus()))
                .andExpect(jsonPath("createdAt").value("2023-04-03T03:03:00"))
                .andExpect(jsonPath("modifiedAt").value("2023-04-03T03:03:00"))
                .andDo(document("get-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("member-id").description("회원 번호")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("stampCount").type(JsonFieldType.NUMBER).description("스탬프 갯수"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("회원 상태"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("회원 가입 날짜"),
                                fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("회원 정보 업데이트 날짜")
                        )
                ));
    }

    @Test
    void getMembers() throws Exception { // 이거 마저 만들기!
        //given
        Pageable pageRequest = PageRequest.of(1, 1, Sort.by(Sort.Direction.DESC, "memberId"));

        List<MemberDto.responseDto> response = List.of(MemberDto.responseDto.builder()
                .memberId(1L).name("영롱").email("aaa@naver.com")
                .phone("010-1111-2222").stampCount(6).status(Member.Status.MEMBER_ACTIVE.getStatus())
                .createdAt(LocalDateTime.of(2023, 4, 3, 3, 3, 0))
                .modifiedAt(LocalDateTime.of(2023, 4, 3, 3, 3, 0)).build());

        Page<Member> memberPage = new PageImpl<>(List.of(new Member())); // 여기에 페이지 정보 주기

        given(memberService.getMembers(Mockito.any())).willReturn(memberPage);
        given(mapper.MemberListToMemberResponseDtos(Mockito.anyList())).willReturn(response);

        //when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/jmt/member")
                .param("page", "1")
                .param("size", "1")
                .param("sort", "memberId,desc")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].memberId").value(1))
                .andExpect(jsonPath("$.data[0].name").value("영롱"))
                .andExpect(jsonPath("$.data[0].email").value("aaa@naver.com"))
                .andExpect(jsonPath("$.data[0].phone").value("010-1111-2222"))
                .andExpect(jsonPath("$.data[0].stampCount").value(6))
                .andExpect(jsonPath("$.data[0].status").value("활동 회원"))
                .andExpect(jsonPath("$.data[0].createdAt").value("2023-04-03T03:03:00"))
                .andExpect(jsonPath("$.data[0].modifiedAt").value("2023-04-03T03:03:00"))
                .andExpect(jsonPath("$.pageInfo.pageNumber").value(pageRequest.getPageNumber()))
                .andExpect(jsonPath("$.pageInfo.size").value(pageRequest.getPageSize()))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(1))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(1))
                .andDo(document("get-members",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회 페이지 번호"),
                                parameterWithName("size").description("페이지 내 요소 갯수"),
                                parameterWithName("sort").description("정렬 기준 및 방식. ex) memberId,desc")
                        ),
                        responseFields(
                                fieldWithPath("data[0].memberId").description("회원 번호"),
                                fieldWithPath("data[0].name").description("회원 이름"),
                                fieldWithPath("data[0].email").description("이메일"),
                                fieldWithPath("data[0].phone").description("휴대폰 번호"),
                                fieldWithPath("data[0].stampCount").description("스탬프 갯수"),
                                fieldWithPath("data[0].status").description("회원 상태"),
                                fieldWithPath("data[0].createdAt").description("회원 가입 날짜"),
                                fieldWithPath("data[0].modifiedAt").description("회원 정보 업데이트 날짜"),
                                fieldWithPath("pageInfo.pageNumber").description("페이지 번호"),
                                fieldWithPath("pageInfo.size").description("페이지 내 요소 갯수"),
                                fieldWithPath("pageInfo.totalPages").description("전체 페이지 수"),
                                fieldWithPath("pageInfo.totalElements").description("전체 요소 갯수")
                        )
                ));
    }

    @Test
    void deleteMember() throws Exception {
        //given
        long memberId = 1L;
        doNothing().when(memberService).deleteMember(Mockito.anyLong());

        //when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/jmt/member/{member-id}", memberId)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent())
                .andDo(document("delete-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("member-id").description("회원 번호")
                        )
                ));
    }
}