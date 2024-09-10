package enerhi.boardservice.repository;

import enerhi.boardservice.domain.Posts;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostsRepository {
    private final EntityManager em;

    public void save(Posts post) {
        em.persist(post);
    }

    public Posts findOne(Long id) {
        return em.find(Posts.class, id);
    }

    public List<Posts> findAll() {
        return em.createQuery("select p from Posts as p", Posts.class).getResultList();
    }
}
