package me.diarity.diaritybespring.tasks.dto;

import me.diarity.diaritybespring.tasks.entity.Tasks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface TasksMapper {
    TasksMapper INSTANCE = Mappers.getMapper(TasksMapper.class);

    @Mappings({
            @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "mapInstantToLocalDateTime"),
            @Mapping(target = "userEmail", source = "user.email"),
    })
    TasksResponse toTasksResponse(Tasks tasks);


    @Named("mapInstantToLocalDateTime")
    default LocalDateTime mapInstantToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }
}
