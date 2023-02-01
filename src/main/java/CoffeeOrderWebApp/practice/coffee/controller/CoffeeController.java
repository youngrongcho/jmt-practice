package CoffeeOrderWebApp.practice.coffee.controller;

import CoffeeOrderWebApp.practice.coffee.dto.CoffeeDto;
import CoffeeOrderWebApp.practice.coffee.entity.Coffee;
import CoffeeOrderWebApp.practice.coffee.mapper.CoffeeMapper;
import CoffeeOrderWebApp.practice.coffee.service.CoffeeService;
import CoffeeOrderWebApp.practice.dto.MultiDto;
import CoffeeOrderWebApp.practice.utils.Uri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/jmt/coffee")
public class CoffeeController {
    private final String DEFAULT_URI = "/jmt/coffee/";

    private CoffeeMapper mapper;
    private CoffeeService coffeeService;

    public CoffeeController(CoffeeMapper mapper, CoffeeService coffeeService) {
        this.mapper = mapper;
        this.coffeeService = coffeeService;
    }

    @PostMapping
    public ResponseEntity postCoffee(@RequestBody @Valid CoffeeDto.postDto postDto){
        Coffee coffee = mapper.CoffeePostDtoToCoffee(postDto);
        Coffee createdCoffee = coffeeService.createCoffee(coffee);
        URI location = Uri.createUri(DEFAULT_URI, Long.toString(createdCoffee.getCoffeeId()));
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{coffee-id}")
    public ResponseEntity patchCoffee(@PathVariable("coffee-id") @Positive long coffeeId, @RequestBody CoffeeDto.patchDto patchDto){
        Coffee coffee = mapper.CoffeePatchDtoToCoffee(patchDto);
        coffee.setCoffeeId(coffeeId);
        Coffee modifiedCoffee = coffeeService.modifyCoffee(coffee);
        return new ResponseEntity(mapper.CoffeeToCoffeeResponseDto(modifiedCoffee), HttpStatus.OK);
    }

    @GetMapping("/{coffee-id}")
    public ResponseEntity getCoffee(@PathVariable("coffee-id") @Positive long coffeeId){
        Coffee coffee = coffeeService.getCoffee(coffeeId);
        return new ResponseEntity(mapper.CoffeeToCoffeeResponseDto(coffee), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getCoffees(Pageable pageable){
        Page<Coffee> coffeePage = coffeeService.getCoffees(pageable);
        List<Coffee> coffeeList = coffeePage.getContent();
        return new ResponseEntity(
                new MultiDto<>(mapper.CoffeesToCoffeeResponseDtos(coffeeList),coffeePage), HttpStatus.OK);
    }

    @DeleteMapping("/{coffee-id}")
    public ResponseEntity deleteCoffee(@PathVariable("coffee-id") @Positive long coffeeId){
        coffeeService.deleteCoffee(coffeeId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
