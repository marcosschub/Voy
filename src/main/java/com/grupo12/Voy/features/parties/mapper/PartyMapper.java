package com.grupo12.Voy.features.parties.mapper;

import com.grupo12.Voy.features.parties.Dto.PartyReqDTO;
import com.grupo12.Voy.features.parties.Dto.PartyResDTO;
import com.grupo12.Voy.features.parties.Dto.PartyUsersDto;
import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.tags.models.TagEntity;
import com.grupo12.Voy.features.users.Mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface PartyMapper {
    @Mapping(source = "organizer.email", target = "organizerName")
    @Mapping(source = "tagsSet", target="tags")
    PartyResDTO toResDTO (PartyEntity party);
    List<PartyResDTO> toResDTOList (List<PartyEntity> parties);

    @Mapping(target = "idParty", ignore = true)
    @Mapping(target = "idExternal", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "tagsSet", ignore = true)
    @Mapping(target = "logicState", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "usersSet", ignore = true)
    @Mapping(target = "ticketsParty", ignore = true)
    PartyEntity toEntity(PartyReqDTO partyReqDTO);


    PartyUsersDto toUserFromEntity(PartyEntity party);


    default Set<String> tagsToStrings(Set<TagEntity> tags ){
        if(tags==null) return null;
        return tags.stream()
                .map(TagEntity::getName)
                .collect(Collectors.toSet());
    }
}
