package me.diarity.diaritybespring.users.dto;

import me.diarity.diaritybespring.users.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    UsersResponse toResponse(Users user);
}
