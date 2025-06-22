package me.diarity.diaritybespring.posts;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.comments.CommentsService;
import me.diarity.diaritybespring.posts.comments.dto.CommentsCreateRequest;
import me.diarity.diaritybespring.posts.comments.dto.CommentsResponse;
import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsMapper;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.posts.dto.PostsWithLikeResponse;
import me.diarity.diaritybespring.posts.likes.PostsLikesService;
import me.diarity.diaritybespring.posts.likes.dto.PostsLikesRequest;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
import me.diarity.diaritybespring.users.UsersRole;
import me.diarity.diaritybespring.users.dto.UsersMapper;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsService {
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;
    private final PostsLikesService postsLikesService;
    private final CommentsService commentsService;


    public List<PostsResponse> findAll(String userEmail) {
        if (userEmail.equals("anonymousUser")) {
            List<Posts> posts = postsRepository.findAllByOrderByCreatedAtDesc();
            PostsMapper postsMapper = PostsMapper.INSTANCE;
            return posts.stream().map(postsMapper::toResponse).toList();
        } else {
            Users user = usersRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
            List<Object[]> posts = postsRepository.findAllByOrderByCreatedAtDescWithLiked(user.getId());
            PostsMapper postsMapper = PostsMapper.INSTANCE;
            return posts.stream().map(
                    post -> {
                        PostsResponse postsResponse = postsMapper.toResponse((Posts) post[0]);
                        postsResponse.setIsLiked((Boolean) post[1]);
                        return postsResponse;
                    }

            ).toList();
        }
    }

    @Transactional
    public PostsResponse create(PostsCreateRequest postsCreateRequest, String userEmail) {
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        if (user.getRole() == UsersRole.READ_ONLY) {
            throw new IllegalArgumentException("읽기 전용 사용자는 게시글을 작성할 수 없습니다.");
        }
        Posts posts = postsRepository.save(PostsMapper.INSTANCE.toEntity(postsCreateRequest, user));
        return PostsMapper.INSTANCE.toResponse(posts);
    }

    public PostsResponse findById(Long id, String userEmail) {
        if (userEmail.equals("anonymousUser")) {
            Posts posts = postsRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
            return PostsMapper.INSTANCE.toResponse(posts);
        }
        else {
            Users user = usersRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
            PostsWithLikeResponse postsWithLikeResponse = postsRepository.findByIdWithLiked(id, user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
            return PostsMapper.INSTANCE.toResponse(postsWithLikeResponse, UsersMapper.INSTANCE.toResponse(user));
        }
    }

    @Transactional
    public PostsResponse like(Long id, String userEmail) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        postsLikesService.like(
                PostsLikesRequest.builder()
                        .post(posts)
                        .user(user)
                        .build()
        ).orElseThrow(() -> new IllegalArgumentException("이미 좋아요를 누른 게시글입니다."));
        posts.addLike();
        posts = postsRepository.save(posts);
        return PostsMapper.INSTANCE.toResponse(posts);
    }

    @Transactional
    public PostsResponse unlike(Long id, String string) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        Users user = usersRepository.findByEmail(string)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        postsLikesService.unlike(
                PostsLikesRequest.builder()
                        .post(posts)
                        .user(user)
                        .build()
        ).orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않은 게시글입니다."));
        posts.removeLike();
        posts = postsRepository.save(posts);
        return PostsMapper.INSTANCE.toResponse(posts);
    }

    @Transactional
    public CommentsResponse createComment(CommentsCreateRequest commentsCreateRequest, String userEmail, Long postId) {
        return commentsService.create(commentsCreateRequest, userEmail, postId);
    }

    public List<CommentsResponse> findAllComments(Long postId, String userEmail) {
        return commentsService.findAll(postId, userEmail);
    }

    @Transactional
    public CommentsResponse likeComment(Long commentId, String userEmail) {
        return commentsService.like(commentId, userEmail);
    }

    @Transactional
    public CommentsResponse unlikeComment(Long commentId, String userEmail) {
        return commentsService.unlike(commentId, userEmail);
    }

    public Integer countByUserId(Long id) {
        return postsRepository.countByAuthorId(id);
    }

    public List<PostsResponse> findAllByUser(Users user) {
        List<Posts> posts = postsRepository.findAllByAuthorOrderByCreatedAtDesc(user);
        return posts.stream().map(PostsMapper.INSTANCE::toResponse).toList();
    }

    public List<PostsResponse> findByUserLimitCount(Users user, Integer count) {
        if (count < 0) {
            throw new IllegalArgumentException("요청 게시글 개수는 0 이상의 정수여야 합니다.");
        }
        List<Posts> posts = postsRepository.findAllByAuthorOrderByCreatedAtDesc(user, Limit.of(count));
        return posts.stream().map(PostsMapper.INSTANCE::toResponse).toList();
    }
}
