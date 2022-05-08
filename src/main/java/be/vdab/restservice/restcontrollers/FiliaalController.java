package be.vdab.restservice.restcontrollers;

import be.vdab.restservice.domain.Filiaal;
import be.vdab.restservice.exceptions.FiliaalNietGevondenException;
import be.vdab.restservice.services.FiliaalService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/filialen")
class FiliaalController {
    private final FiliaalService filiaalService;

    FiliaalController(FiliaalService filiaalService) {
        this.filiaalService = filiaalService;
    }

    @GetMapping("{id}")
    Filiaal get(@PathVariable long id) {
        return filiaalService.findById(id)
                .orElseThrow(FiliaalNietGevondenException::new);
    }

    @ExceptionHandler(FiliaalNietGevondenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void filiaalNietGevonden() {}

    @DeleteMapping("{id}")
    void delete(@PathVariable long id) {
        filiaalService.delete(id);
    }

    @PostMapping
    void post(@RequestBody @Valid Filiaal filiaal) {
        filiaalService.create(filiaal);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String,String> verkeerdeData(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        FieldError::getDefaultMessage));
    }

    @PutMapping("{id}")
    void put(@PathVariable long id,
             @RequestBody @Valid Filiaal filiaal) {
        if(filiaalService.findById(id).isEmpty()) {
            throw new FiliaalNietGevondenException();
        }
        filiaalService.update(filiaal.withId(id));
    }
}
