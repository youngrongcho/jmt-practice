package CoffeeOrderWebApp.practice.member.mapper;

import CoffeeOrderWebApp.practice.member.dto.MemberDto;
import CoffeeOrderWebApp.practice.member.entity.Member;
import CoffeeOrderWebApp.practice.stamp.Stamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-01T22:05:55+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member MemberPostDtoToMember(MemberDto.postDto postDto) {
        if ( postDto == null ) {
            return null;
        }

        Member member = new Member();

        member.setName( postDto.getName() );
        member.setEmail( postDto.getEmail() );
        member.setPhone( postDto.getPhone() );

        return member;
    }

    @Override
    public Member MemberPatchDtoToMember(MemberDto.patchDto patchDto) {
        if ( patchDto == null ) {
            return null;
        }

        Member member = new Member();

        member.setName( patchDto.getName() );
        member.setPhone( patchDto.getPhone() );

        return member;
    }

    @Override
    public MemberDto.responseDto MemberToMemberResponseDto(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberDto.responseDto.responseDtoBuilder responseDto = MemberDto.responseDto.builder();

        responseDto.stampCount( memberStampStampCount( member ) );
        if ( member.getMemberId() != null ) {
            responseDto.memberId( member.getMemberId() );
        }
        responseDto.name( member.getName() );
        responseDto.email( member.getEmail() );
        responseDto.phone( member.getPhone() );
        responseDto.status( member.getStatus() );
        responseDto.createdAt( member.getCreatedAt() );
        responseDto.modifiedAt( member.getModifiedAt() );

        return responseDto.build();
    }

    @Override
    public List<MemberDto.responseDto> MemberListToMemberResponseDtos(List<Member> memberList) {
        if ( memberList == null ) {
            return null;
        }

        List<MemberDto.responseDto> list = new ArrayList<MemberDto.responseDto>( memberList.size() );
        for ( Member member : memberList ) {
            list.add( MemberToMemberResponseDto( member ) );
        }

        return list;
    }

    private int memberStampStampCount(Member member) {
        if ( member == null ) {
            return 0;
        }
        Stamp stamp = member.getStamp();
        if ( stamp == null ) {
            return 0;
        }
        int stampCount = stamp.getStampCount();
        return stampCount;
    }
}