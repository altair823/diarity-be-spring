package me.diarity.diaritybespring.posts.comments.dto;

import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.posts.comments.entity.Comments;
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
public interface CommentsMapper {
    CommentsMapper INSTANCE = Mappers.getMapper(CommentsMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "author", source = "author"),
            @Mapping(target = "post", source = "post"),
            @Mapping(target = "content", source = "commentsCreateRequest.content"),
            @Mapping(target = "createdAt", expression = "java( java.time.Instant.now() )"),
            @Mapping(target = "modifiedAt", expression = "java( java.time.Instant.now() )"),
            @Mapping(target = "isDeleted", constant = "false"),
            @Mapping(target = "deletedAt", expression = "java(null)"),
            @Mapping(target = "likesCount", constant = "0")
    })
    Comments toEntity(CommentsCreateRequest commentsCreateRequest, Users author, Posts post);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "userId", source = "author.id"),
            @Mapping(target = "displayName", source = "author.displayName"),
            @Mapping(target = "picture", source = "author.picture"),
            @Mapping(target = "content", source = "content"),
            @Mapping(target = "likesCount", source = "likesCount"),
            @Mapping(target = "isLiked", expression = "java(false)"),
            @Mapping(target = "postId", source = "post.id"),
            @Mapping(target = "parentCommentId", expression = "java(null)"),
            @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "mapInstantToLocalDateTime"),
            @Mapping(target = "modifiedAt", source = "modifiedAt", qualifiedByName = "mapInstantToLocalDateTime")
    })
    CommentsResponse toResponse(Comments comment);

    @Named("mapInstantToLocalDateTime")
    default LocalDateTime mapInstantToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }
}
