package me.diarity.diaritybespring.posts;

import me.diarity.diaritybespring.posts.comments.CommentsService;
import me.diarity.diaritybespring.posts.likes.PostsLikesService;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
import me.diarity.diaritybespring.users.UsersRole;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
public class PostsServiceOnlyReadUserTest {
    @Mock
    private PostsRepository postsRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private PostsLikesService postsLikesService;
    @Mock
    private CommentsService commentsService;

    @InjectMocks
    private PostsService postsService;

    private final Users author = Users.builder()
            .id(1L)
            .email("testemail@gmail.com")
            .name("testUser")
            .picture("testPicture")
            .role(UsersRole.READ_ONLY)
            .displayName("testUser")
            .build();

    @BeforeEach
    public void setUp() {
        // Create a mock Authentication object
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(author.getEmail());

        // Create a mock SecurityContext object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }
}
