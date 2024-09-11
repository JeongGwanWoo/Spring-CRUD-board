package enerhi.boardservice.repository;

import enerhi.boardservice.domain.Posts;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostsRepository {
    private final EntityManager em;

    public void save(Posts post) {
        log.info("save 실행 됨 !!");
        log.info("Name = " + post.getName());
        em.persist(post);
        log.info("Id = " + post.getId());
    }

    public Posts findOne(Long id) {
        return em.find(Posts.class, id);
    }

    public List<Posts> findAll() {
        return em.createQuery("select p from Posts as p", Posts.class).getResultList();
    }
}
