package me.diarity.diaritybespring.users.dto;

import me.diarity.diaritybespring.auth.dto.LoginUserInfoResponse;
import me.diarity.diaritybespring.users.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    UsersResponse toResponse(Users user);

    @Mappings({
            @Mapping(
                    target = "id", ignore = true
            )
    })
    Users toEntity(UsersSaveRequest usersSaveRequest);

    @Mappings({
            @Mapping(
                    target = "role", constant = "NORMAL"
            ),
            @Mapping(
                    target = "displayName", source = "name"
            )
    }
    )
    UsersSaveRequest toRequest(LoginUserInfoResponse loginUserInfoResponse);
}
