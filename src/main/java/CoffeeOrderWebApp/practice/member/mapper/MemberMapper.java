package CoffeeOrderWebApp.practice.member.mapper;

import CoffeeOrderWebApp.practice.member.dto.MemberDto;
import CoffeeOrderWebApp.practice.member.entity.Member;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member MemberPostDtoToMember(MemberDto.postDto postDto);

    Member MemberPatchDtoToMember(MemberDto.patchDto patchDto);

    @Mapping(source = "stamp.stampCount", target = "stampCount")
    @Mapping(source = "status.status", target = "status")
    MemberDto.responseDto MemberToMemberResponseDto(Member member);

    List<MemberDto.responseDto> MemberListToMemberResponseDtos(List<Member> memberList);
}
