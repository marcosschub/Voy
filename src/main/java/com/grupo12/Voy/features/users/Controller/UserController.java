package com.grupo12.Voy.features.users.Controller;


import com.grupo12.Voy.features.parties.Dto.PartyUsersDto;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketUsersDto;
import com.grupo12.Voy.features.users.Dto.NewUserDto;
import com.grupo12.Voy.features.users.Dto.UserDto;
import com.grupo12.Voy.features.users.Dto.UserFollowDto;
import com.grupo12.Voy.features.users.Dto.UserUpdateDto;
import com.grupo12.Voy.features.users.Service.IUsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final IUsersService userService;

    @GetMapping
    ResponseEntity<List<UserDto>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }


    @GetMapping("/{idExternal}")
    ResponseEntity<UserDto> findbyExternalId(@PathVariable UUID idExternal){
        return ResponseEntity.ok(userService.findByExternalId(idExternal));
    }

    @GetMapping ("/{email}")
    ResponseEntity<UserDto> findByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/{idExternal}/follows")
    ResponseEntity<List<UserFollowDto>> listFollowList(@PathVariable UUID idExternal){
        return ResponseEntity.ok(userService.listFollowList(idExternal));
    }

    @GetMapping("/{idExternal}/followers")
    ResponseEntity<List<UserFollowDto>> listFollowersList(@PathVariable UUID idExternal){
       return ResponseEntity.ok(userService.listFollowersList(idExternal));
    }

    @GetMapping("/{idExternal}/myParties")
    ResponseEntity<List<PartyUsersDto>> liistMyParties(@PathVariable UUID idExternal){
        return ResponseEntity.ok(userService.listMyParties(idExternal));
    }

    @GetMapping("/{idExternal}/followParties")
    ResponseEntity<List<PartyUsersDto>> listFollowedParties(@PathVariable UUID idExternal){
        return ResponseEntity.ok((userService.listFollowedParties(idExternal)));
    }

    @GetMapping("/{idExternal}/myTickets")
    ResponseEntity<List<TicketUsersDto>> listMyTickets(@PathVariable UUID idExternal){
        return ResponseEntity.ok((userService.listTickets(idExternal)));
    }

    @GetMapping("/{idExternal}/myReceipts")
    ResponseEntity<List<ReceiptResponseDTO>> listMyReceipts(@PathVariable UUID idExternal){
        return ResponseEntity.ok((userService.listReceipt(idExternal)));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    ResponseEntity<UserDto> newUser(@RequestBody @Valid NewUserDto newUserDto){
        return new ResponseEntity<>(userService.newUser(newUserDto),HttpStatus.CREATED);
    }

    @PutMapping("/{idExternal}")
    ResponseEntity<UserUpdateDto> updateUser(@PathVariable UUID idExternal,
                                             @RequestBody @Valid UserUpdateDto userUpdateDto){
        return ResponseEntity.ok(userService.updateUser(idExternal, userUpdateDto));
    }


}
