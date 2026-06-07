package com.grupo12.Voy.features.users.Service;


import com.grupo12.Voy.common.exceptions.EntityDuplicatedException;
import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.features.parties.Dto.PartyUsersDto;
import com.grupo12.Voy.features.parties.mapper.PartyMapper;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.receipts.ReceiptMapper;
import com.grupo12.Voy.features.tickets.TicketMapper;
import com.grupo12.Voy.features.tickets.models.DTO.TicketUsersDto;
import com.grupo12.Voy.features.users.Dto.NewUserDto;
import com.grupo12.Voy.features.users.Dto.UserFollowDto;
import com.grupo12.Voy.features.users.Dto.UserUpdateDto;
import com.grupo12.Voy.features.users.Mapper.NewUserDtoMapper;
import com.grupo12.Voy.features.users.Mapper.UserFollowMapper;
import com.grupo12.Voy.features.users.Mapper.UserMapper;
import com.grupo12.Voy.features.users.Mapper.UserUpdateMapper;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import com.grupo12.Voy.features.users.Dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsersService implements IUsersService{
    private UserRepository userRepository;
    private UserMapper userMapper;
    private NewUserDtoMapper newUserDtoMapper;
    private UserFollowMapper userFollowMapper;
    private UserUpdateMapper userUpdateMapper;
    private TicketMapper ticketMapper;
    private ReceiptMapper receiptMapper;
    private PartyMapper partyMapper;

    @Override
    public UserDto findByExternalId(UUID userUuid){
        return userMapper.userToDto(userRepository.findByExternalId(userUuid)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado.")));
    }

    @Override
    public List<UserDto> getAll(){
        return userRepository.findAll().stream().map(userMapper::userToDto).toList();
    }


    @Override
    public UserDto findByEmail(String userEmail){
        return userMapper.userToDto(userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado")));
    }

    @Override
    public void deleteUser(UUID externalId){
        userRepository.delete(userRepository.findByExternalId(externalId)
                .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado")));
    }

    @Override
    public UserDto newUser(NewUserDto newUserDto){
        UserEntity user = newUserDtoMapper.newUserToEntity(newUserDto);
        if(userRepository.existsByEmail(newUserDto.email()) ||
                userRepository.existsByUsername(newUserDto.username())){
            throw new EntityDuplicatedException("Usuario duplicado");
        }
        return userMapper.userToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserUpdateDto updateUser(UUID userUuid, UserUpdateDto userUpdateDto){
       UserEntity user =userRepository
               .findByExternalId(userUuid)
               .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado"));
       user.setUsername(userUpdateDto.username());
       user.setPassword(userUpdateDto.password());
       return userUpdateMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserFollowDto> listFollowList(UUID userUuid){
          UserEntity  user = userRepository.findByExternalId(userUuid)
                  .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado"));
          return user.getFollowsList().stream().map(userFollowMapper::toDto).toList();
    }

    @Override
    public List<UserFollowDto> listFollowersList(UUID userUuid){
        UserEntity  user = userRepository.findByExternalId(userUuid)
                .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado"));
        return user.getFollowersList().stream().map(userFollowMapper::toDto).toList();
    }

    @Override
    public List<TicketUsersDto> listTickets(UUID userUuid){
        UserEntity user = userRepository.findByExternalId(userUuid)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
        return user.getMyTickets().stream().map(ticketMapper::toUsersDto).toList();
    }

    @Override
    public List<ReceiptResponseDTO> listReceipt(UUID userUuid){
        UserEntity user = userRepository.findByExternalId(userUuid)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
        return user.getMyReceipts().stream().map(receiptMapper::toResponseDTO).toList();
    }

    @Override
    public List<PartyUsersDto> listFollowedParties(UUID userUuid){
        UserEntity user = userRepository.findByExternalId(userUuid)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
        return user.getFollowedParties().stream().map(partyMapper::toUserFromEntity).toList();
    }

    @Override
    public List<PartyUsersDto> listMyParties(UUID userId){
        UserEntity user = userRepository.findByExternalId(userId).orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
        return user.getMyParties().stream().map(partyMapper::toUserFromEntity).toList();
    }
}