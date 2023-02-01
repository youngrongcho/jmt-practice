package CoffeeOrderWebApp.practice.coffee.service;

import CoffeeOrderWebApp.practice.coffee.entity.Coffee;
import CoffeeOrderWebApp.practice.coffee.repository.CoffeeRepository;
import CoffeeOrderWebApp.practice.exception.ExceptionEnum;
import CoffeeOrderWebApp.practice.exception.LogicException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CoffeeService {
    private CoffeeRepository coffeeRepository;

    public CoffeeService(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    public Coffee createCoffee(Coffee coffee){
        verifyExistCoffee(coffee);
        return coffeeRepository.save(coffee);
    }

    public Coffee modifyCoffee(Coffee coffee){
        Coffee foundCoffee = findCoffee(coffee.getCoffeeId());

        Optional.ofNullable(coffee.getKorName()).ifPresent(name -> foundCoffee.setKorName(name));
        Optional.ofNullable(coffee.getEngName()).ifPresent(name -> foundCoffee.setEngName(name));
        Optional.ofNullable(coffee.getPrice()).ifPresent(price -> foundCoffee.setPrice(price));

        return coffeeRepository.save(foundCoffee);
    }

    public Coffee getCoffee(long coffeeId){
        return findCoffee(coffeeId);
    }

    public Page<Coffee> getCoffees(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize(), pageable.getSort());
        Page<Coffee> coffeePage = coffeeRepository.findAll(pageRequest);
        return coffeePage;
    }

    public void deleteCoffee(long coffeeId){
        findCoffee(coffeeId);
        coffeeRepository.deleteById(coffeeId);
    }

    private void verifyExistCoffee(Coffee coffee) {
        Optional<Coffee> optionalCoffee = coffeeRepository.findByCoffeeCode(coffee.getCoffeeCode());
        optionalCoffee.ifPresent(e->{throw new LogicException(ExceptionEnum.COFFEE_EXISTS);});
    }

    public Coffee findCoffee(long coffeeId) {
        Optional<Coffee> optionalCoffeeEntity = coffeeRepository.findById(coffeeId);
        Coffee foundCoffee = optionalCoffeeEntity.orElseThrow(()->new LogicException(ExceptionEnum.COFFEE_NOT_FOUND));
        return foundCoffee;
    }
}
