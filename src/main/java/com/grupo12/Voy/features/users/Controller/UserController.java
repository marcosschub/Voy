package com.grupo12.Voy.features.users.Controller;


import com.grupo12.Voy.features.users.Dto.NewUserDto;
import com.grupo12.Voy.features.users.Dto.UserDto;
import com.grupo12.Voy.features.users.Dto.UserFollowDto;
import com.grupo12.Voy.features.users.Dto.UserUpdateDto;
import com.grupo12.Voy.features.users.Service.IUsersService;
import jakarta.validation.Valid;
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

    private final IUsersService userService;

    @GetMapping
    ResponseEntity<List<UserDto>> getAll(){
        return ResponseEntity.ok(userService.getAll());
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
    ResponseEntity<NewUserDto> newUser(@RequestBody @Valid NewUserDto newUserDto){
        return new ResponseEntity<>(userService.newUser(newUserDto),HttpStatus.CREATED);
    }

    @PutMapping("/{idExternal}")
    ResponseEntity<UserUpdateDto> updateUser(@PathVariable UUID userUuid,@RequestBody UserUpdateDto userUpdateDto){
        return ResponseEntity.ok(userService.updateUser(userUuid, userUpdateDto));
    }

    @GetMapping("/{idExternal}/follows")
    ResponseEntity<List<UserFollowDto>> listFollowList(@PathVariable UUID idExternal){
        return ResponseEntity.ok(userService.listFollowList(idExternal));
    }

    @GetMapping("/{idExternal}/followers")
    ResponseEntity<List<UserFollowDto>> listFollowersList(@PathVariable UUID idExternal){
       return ResponseEntity.ok(userService.listFollowersList(idExternal));
    }




}
