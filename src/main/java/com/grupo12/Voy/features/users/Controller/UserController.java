package com.grupo12.Voy.features.users.Controller;


import com.grupo12.Voy.features.users.Dto.NewUserDto;
import com.grupo12.Voy.features.users.Dto.UserDto;
import com.grupo12.Voy.features.users.Service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UsersService userService;

    @GetMapping
    ResponseEntity<List<UserDto>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDto> findByID(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/{idExternall}")
    ResponseEntity<UserDto> findbyExternalId(@PathVariable UUID idExternal){
        return ResponseEntity.ok(userService.findByExternalId(idExternal));
    }

    @GetMapping ("/{email}")
    ResponseEntity<UserDto> findByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    ResponseEntity<NewUserDto> newUser(@RequestBody NewUserDto newUserDto){
        return new ResponseEntity<>(userService.newUser(newUserDto),HttpStatus.CREATED);
    }

    @PutMapping("/{idExternal}")
    ResponseEntity<UserDto> updateUser(@PathVariable UUID userUuid,@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUser(userUuid,userDto));
    }




}
