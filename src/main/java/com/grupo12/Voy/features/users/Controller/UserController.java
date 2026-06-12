package com.grupo12.Voy.features.users.Controller;


import com.grupo12.Voy.common.security.models.CredentialsEntity;
import com.grupo12.Voy.features.parties.Dto.PartyUsersDto;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketUsersDto;
import com.grupo12.Voy.features.users.Dto.NewUserDto;
import com.grupo12.Voy.features.users.Dto.UserDto;
import com.grupo12.Voy.features.users.Dto.UserFollowDto;
import com.grupo12.Voy.features.users.Dto.UserUpdateDto;
import com.grupo12.Voy.features.users.Service.IUsersService;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final IUsersService userService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<UserDto>> getAll(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ){
        return ResponseEntity.ok(userService.getAll(username,email));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deleteUserByAdmin(@PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> deleteUser(@AuthenticationPrincipal(expression = "usuario.externalId") UUID id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    ResponseEntity<UserDto> newUser(@RequestBody @Valid NewUserDto newUserDto){
        return new ResponseEntity<>(userService.newUser(newUserDto),HttpStatus.CREATED);
    }

    @PutMapping("/{idExternal}/update")
    @PreAuthorize("hasRole('ADMIN') or #idExternal == authentication.principal.userId")
    ResponseEntity<UserUpdateDto> updateUser(@PathVariable UUID idExternal,
                                             @RequestBody @Valid UserUpdateDto userUpdateDto){
        return ResponseEntity.ok(userService.updateUser(idExternal, userUpdateDto));
    }



    @GetMapping("/{idExternal}/follows")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<UserFollowDto>> listFollowList(@PathVariable UUID idExternal){
        return ResponseEntity.ok(userService.listFollowList(idExternal));
    }

    @PatchMapping("/{idUser}/follow/user/{idOtherUser}")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<UserFollowDto>> alterFollowUser(@PathVariable UUID idUser,@PathVariable UUID idOtherUser){
        return ResponseEntity.ok(userService.alterFollow(idUser,idOtherUser));
    }

    @GetMapping("/{idExternal}/followers")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<UserFollowDto>> listFollowersList(@PathVariable UUID idExternal){
       return ResponseEntity.ok(userService.listFollowersList(idExternal));
    }

    @GetMapping("/{idExternal}/myParties")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<PartyUsersDto>> listMyParties(@PathVariable UUID idExternal){
        return ResponseEntity.ok(userService.listMyParties(idExternal));
    }

    @GetMapping("/{idExternal}/followParties")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<PartyUsersDto>> listFollowedParties(@PathVariable UUID idExternal){
        return ResponseEntity.ok((userService.listFollowedParties(idExternal)));
    }

    @PatchMapping("/{idExternal}/follow/party/{idParty}")
    @PreAuthorize("#idExternal == authentication.principal.userId")
    ResponseEntity<List<PartyUsersDto>> alterFollowParty(@PathVariable UUID idExternal,
                                                         @PathVariable UUID idParty){
        return ResponseEntity.ok(userService.alterFollowParty(idExternal,idParty));
    }

    @GetMapping("/{idExternal}/myTickets")
    @PreAuthorize("#idExternal == authentication.principal.userId")
    ResponseEntity<List<TicketUsersDto>> listMyTickets(@PathVariable UUID idExternal){
        return ResponseEntity.ok((userService.listTickets(idExternal)));
    }

    @GetMapping("/{idExternal}/myReceipts")
    @PreAuthorize("#idExternal == authentication.principal.userId")
    ResponseEntity<List<ReceiptResponseDTO>> listMyReceipts(@PathVariable UUID idExternal){
        return ResponseEntity.ok((userService.listReceipt(idExternal)));
    }

    @PatchMapping("/verify/{idExternal}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> verifyUser(@PathVariable UUID idExternal){
        return ResponseEntity.ok(userService.userToPublic(idExternal));
    }
}
