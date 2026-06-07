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
    @Mapping(source = "tagsList", target = "tags")
    PartyResDTO toResDTO (PartyEntity party);

    List<PartyResDTO> toResDTOList (List<PartyEntity> parties);

    PartyEntity toEntity(PartyReqDTO partyReqDTO);


    PartyUsersDto toUserFromEntity(PartyEntity party);


    default Set<String> tagsToStrings(List<TagEntity> tags ){
        if(tags==null) return null;
        return tags.stream()
                .map(TagEntity::getName)
                .collect(Collectors.toSet());
    }
}
