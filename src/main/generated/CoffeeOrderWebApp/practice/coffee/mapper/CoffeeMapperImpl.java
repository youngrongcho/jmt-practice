package CoffeeOrderWebApp.practice.coffee.mapper;

import CoffeeOrderWebApp.practice.coffee.dto.CoffeeDto;
import CoffeeOrderWebApp.practice.coffee.entity.Coffee;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-16T00:22:58+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class CoffeeMapperImpl implements CoffeeMapper {

    @Override
    public Coffee CoffeePostDtoToCoffee(CoffeeDto.postDto postDto) {
        if ( postDto == null ) {
            return null;
        }

        Coffee coffee = new Coffee();

        coffee.setKorName( postDto.getKorName() );
        coffee.setEngName( postDto.getEngName() );
        coffee.setPrice( postDto.getPrice() );
        coffee.setCoffeeCode( postDto.getCoffeeCode() );

        return coffee;
    }

    @Override
    public Coffee CoffeePatchDtoToCoffee(CoffeeDto.patchDto patchDto) {
        if ( patchDto == null ) {
            return null;
        }

        Coffee coffee = new Coffee();

        coffee.setKorName( patchDto.getKorName() );
        coffee.setEngName( patchDto.getEngName() );
        coffee.setPrice( patchDto.getPrice() );

        return coffee;
    }

    @Override
    public CoffeeDto.responseDto CoffeeToCoffeeResponseDto(Coffee coffee) {
        if ( coffee == null ) {
            return null;
        }

        CoffeeDto.responseDto.responseDtoBuilder responseDto = CoffeeDto.responseDto.builder();

        if ( coffee.getCoffeeId() != null ) {
            responseDto.coffeeId( coffee.getCoffeeId() );
        }
        responseDto.korName( coffee.getKorName() );
        responseDto.engName( coffee.getEngName() );
        responseDto.price( coffee.getPrice() );
        responseDto.status( coffee.getStatus() );
        responseDto.coffeeCode( coffee.getCoffeeCode() );
        responseDto.createdAt( coffee.getCreatedAt() );
        responseDto.modifiedAt( coffee.getModifiedAt() );

        return responseDto.build();
    }

    @Override
    public List<CoffeeDto.responseDto> CoffeesToCoffeeResponseDtos(List<Coffee> coffeeList) {
        if ( coffeeList == null ) {
            return null;
        }

        List<CoffeeDto.responseDto> list = new ArrayList<CoffeeDto.responseDto>( coffeeList.size() );
        for ( Coffee coffee : coffeeList ) {
            list.add( CoffeeToCoffeeResponseDto( coffee ) );
        }

        return list;
    }
}
