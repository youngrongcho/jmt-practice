package CoffeeOrderWebApp.practice.coffee.mapper;

import CoffeeOrderWebApp.practice.coffee.dto.CoffeeDto;
import CoffeeOrderWebApp.practice.coffee.entity.Coffee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoffeeMapper {
    Coffee CoffeePostDtoToCoffee(CoffeeDto.postDto postDto);
    Coffee CoffeePatchDtoToCoffee(CoffeeDto.patchDto patchDto);

//    @Mapping(source = "status.status", target = "status")
    CoffeeDto.responseDto CoffeeToCoffeeResponseDto(Coffee coffee);

    List<CoffeeDto.responseDto> CoffeesToCoffeeResponseDtos(List<Coffee> coffeeList);
}
