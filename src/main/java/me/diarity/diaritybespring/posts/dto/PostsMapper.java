package me.diarity.diaritybespring.posts.dto;

import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.users.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface PostsMapper {
    PostsMapper INSTANCE = Mappers.getMapper(PostsMapper.class);

    @Mappings({
            @Mapping(target = "author", source = "author"),
            @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "mapInstantToLocalDateTime"),
            @Mapping(target = "modifiedAt", source = "modifiedAt", qualifiedByName = "mapInstantToLocalDateTime"),
            @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "mapInstantToLocalDateTime"),
            @Mapping(target = "isLiked", ignore = true)
    })
    PostsResponse toResponse(Posts posts);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "author", source = "author"),
            @Mapping(target = "createdAt", expression = "java( java.time.Instant.now() )"),
            @Mapping(target = "modifiedAt", expression = "java( java.time.Instant.now() )"),
            @Mapping(target = "isPublic", constant = "true"),
            @Mapping(target = "isDeleted", constant = "false"),
            @Mapping(target = "deletedAt", expression = "java(null)"),
            @Mapping(target = "likesCount", constant = "0"),
            @Mapping(target = "commentsCount", constant = "0")
    })
    Posts toEntity(PostsCreateRequest postsCreateRequest, Users author);

    @Named("mapInstantToLocalDateTime")
    default LocalDateTime mapInstantToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }
}
