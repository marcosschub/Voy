package com.grupo12.Voy.features.users.Service;


import com.grupo12.Voy.common.exceptions.EntityDuplicatedException;
import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.common.security.enums.Roles;
import com.grupo12.Voy.common.security.models.CredentialsEntity;
import com.grupo12.Voy.common.security.models.RoleEntity;
import com.grupo12.Voy.common.security.repository.CredentialsRepository;
import com.grupo12.Voy.common.security.repository.RoleRepository;
import com.grupo12.Voy.features.parties.Dto.PartyUsersDto;
import com.grupo12.Voy.features.parties.mapper.PartyMapper;
import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.parties.repository.PartyRepository;
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
import com.grupo12.Voy.features.users.specification.UserSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsersService implements IUsersService{
    private UserRepository userRepository;
    private PartyRepository partyRepository;
    private UserMapper userMapper;
    private NewUserDtoMapper newUserDtoMapper;
    private UserFollowMapper userFollowMapper;
    private UserUpdateMapper userUpdateMapper;
    private TicketMapper ticketMapper;
    private ReceiptMapper receiptMapper;
    private PartyMapper partyMapper;

    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private CredentialsRepository credentialsRepository;

    @Override
    public UserDto findByExternalId(UUID userUuid){
        return userMapper.userToDto(getUser(userUuid));
    }

    @Override
    public List<UserDto> getAll(String username, String email){
        PredicateSpecification<UserEntity> spec = PredicateSpecification.allOf(
                UserSpecification.usernameContains(username),
                UserSpecification.emailContains(email)
        );

        return userRepository.findAll(spec).stream().map(userMapper::userToDto).toList();
    }


    @Override
    public void deleteUser(UUID externalId){
        userRepository.delete(getUser(externalId));
    }

    @Override
    @Transactional
    public UserDto newUser(NewUserDto newUserDto) {
        if (userRepository.existsByEmail(newUserDto.email()) ||
                userRepository.existsByUsername(newUserDto.username())) {
            throw new EntityDuplicatedException("Usuario duplicado");
        }

        UserEntity user = newUserDtoMapper.newUserToEntity(newUserDto);

        UserEntity userEntity = userRepository.save(user);

        RoleEntity defaultRole = roleRepository.findByRole(Roles.ROLE_USER);

        CredentialsEntity credentials = CredentialsEntity.builder()
                .username(newUserDto.username())
                .enabled(true)
                .usuario(userEntity)
                .roles(new HashSet<>(Set.of(defaultRole)))
                .password(passwordEncoder.encode(newUserDto.password()))
                .build();

        credentialsRepository.save(credentials);

        return userMapper.userToDto(userEntity);
    }

    @Override
    @Transactional
    public UserUpdateDto updateUser(UUID userUuid, UserUpdateDto userUpdateDto){
       UserEntity user = getUser(userUuid);
       user.setUsername(userUpdateDto.username());
       user.getCredentials().setUsername(userUpdateDto.username());
       user.getCredentials().setPassword(userUpdateDto.password());
       return userUpdateMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserFollowDto> listFollowList(UUID userUuid){
          UserEntity user = getUser(userUuid);
          return user.getFollowsList().stream().map(userFollowMapper::toDto).toList();
    }

    public List<UserFollowDto> alterFollow(UUID userId, UUID otherUserId){
        if(userId.equals(otherUserId)){
            throw new EntityDuplicatedException("No puede un usuario seguirse a si mismo");
        }
        UserEntity user = getUser(userId);
        UserEntity otherUser = getUser(otherUserId);
        if(user.getFollowsList().contains(otherUser))
            user.getFollowsList().remove(otherUser);
        else
            user.getFollowsList().add(otherUser);
        return userRepository
                .save(user)
                .getFollowsList()
                .stream()
                .map(userFollowMapper::toDto)
                .toList();
    }

    @Override
    public List<UserFollowDto> listFollowersList(UUID userUuid){
        UserEntity  user = getUser(userUuid);
        return user.getFollowersList().stream().map(userFollowMapper::toDto).toList();
    }

    @Override
    public List<PartyUsersDto> listFollowedParties(UUID userUuid){
        UserEntity user = getUser(userUuid);
        return user.getFollowedParties().stream().map(partyMapper::toUserFromEntity).toList();
    }

    @Override
    public List<PartyUsersDto> alterFollowParty(UUID userId, UUID partyId){
        UserEntity user = getUser(userId);
        PartyEntity party = partyRepository
                .findByExternalIdAndLogicStateTrue(partyId)
                .orElseThrow(()-> new EntityNotFoundException("Evento no encontrado"));
        if(user.getFollowedParties().contains(party))
            user.getFollowedParties().remove(party);
        else
            user.getFollowedParties().add(party);
        return user
                .getFollowedParties()
                .stream()
                .map(partyMapper::toUserFromEntity)
                .toList();
    }

    @Override
    public List<PartyUsersDto> listMyParties(UUID userId){
        UserEntity user = getUser(userId);
        return user.getMyParties().stream().map(partyMapper::toUserFromEntity).toList();
    }

    @Override
    public List<TicketUsersDto> listTickets(UUID userUuid){
        UserEntity user = getUser(userUuid);
        return user.getMyTickets().stream().map(ticketMapper::toUsersDto).toList();
    }

    @Override
    public List<ReceiptResponseDTO> listReceipt(UUID userUuid){
        UserEntity user = getUser(userUuid);
        return user.getMyReceipts().stream().map(receiptMapper::toResponseDTO).toList();
    }

    @Override
    public UserDto userToPublic(UUID userId){
        UserEntity user = userRepository
                .findByExternalId(userId)
                .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado"));
        user.setAccesibilityUser(true);
        RoleEntity rol = new RoleEntity(Roles.ROLE_ORGANIZATOR);

        user.getCredentials().getRoles().remove(Roles.ROLE_USER);
        user.getCredentials().getRoles().add(rol);

        return userMapper.userToDto(userRepository.save(user));
    }

    private UserEntity getUser(UUID userId){
        return userRepository
                .findByExternalId(userId)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
    }


}