package wheresoever.quickprotoserver.domain.post.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import wheresoever.quickprotoserver.domain.post.dao.PostRepository;
import wheresoever.quickprotoserver.domain.post.exception.PostNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @Autowired
    EntityManager em;

    @Autowired
    EntityManagerFactory emf;


    @Test
    void 이미_글_삭제된_경우() {
        //given
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            postService.delete(1L);
        }).isInstanceOf(PostNotFoundException.class);
    }
}