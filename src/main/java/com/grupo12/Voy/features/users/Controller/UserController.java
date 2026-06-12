package com.grupo12.Voy.features.users.Controller;


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

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<UserUpdateDto> updateUser(@AuthenticationPrincipal(expression = "usuario.externalId") UUID idExternal,
                                             @RequestBody @Valid UserUpdateDto userUpdateDto){
        return ResponseEntity.ok(userService.updateUser(idExternal, userUpdateDto));
    }


    @GetMapping("/follows")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<UserFollowDto>> listFollowList(@AuthenticationPrincipal(expression = "usuario.externalId") UUID idExternal){
        return ResponseEntity.ok(userService.listFollowList(idExternal));
    }

    @PatchMapping("/follow/user/{idOtherUser}")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<UserFollowDto>> alterFollowUser(@AuthenticationPrincipal(expression = "usuario.externalId")UUID idUser,
                                                        @PathVariable UUID idOtherUser){
        return ResponseEntity.ok(userService.alterFollow(idUser,idOtherUser));
    }

    @GetMapping("/followers")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<UserFollowDto>> listFollowersList(@AuthenticationPrincipal(expression = "usuario.externalId") UUID idExternal){
       return ResponseEntity.ok(userService.listFollowersList(idExternal));
    }

    @GetMapping("/myParties")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<PartyUsersDto>> listMyParties(@AuthenticationPrincipal(expression = "usuario.externalId") UUID idExternal){
        return ResponseEntity.ok(userService.listMyParties(idExternal));
    }

    @GetMapping("/followParties")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<PartyUsersDto>> listFollowedParties(@AuthenticationPrincipal(expression = "usuario.externalId") UUID idExternal){
        return ResponseEntity.ok((userService.listFollowedParties(idExternal)));
    }

    @PatchMapping("/follow/party/{idParty}")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<PartyUsersDto>> alterFollowParty(@AuthenticationPrincipal(expression = "usuario.externalId")UUID idExternal,
                                                         @PathVariable UUID idParty){
        return ResponseEntity.ok(userService.alterFollowParty(idExternal,idParty));
    }

    @GetMapping("/myTickets")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<TicketUsersDto>> listMyTickets(@AuthenticationPrincipal(expression = "usuario.externalId")UUID idExternal){
        return ResponseEntity.ok((userService.listTickets(idExternal)));
    }

    @GetMapping("/myReceipts")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<ReceiptResponseDTO>> listMyReceipts(@AuthenticationPrincipal(expression = "usuario.externalId")UUID idExternal){
        return ResponseEntity.ok((userService.listReceipt(idExternal)));
    }

    @PatchMapping("/verify/{idExternal}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> verifyUser(@PathVariable UUID idExternal){
        return ResponseEntity.ok(userService.userToPublic(idExternal));
    }
}
