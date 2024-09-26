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
        em.persist(post);
    }

    public Posts findOne(Long id) {
        return em.find(Posts.class, id);
    }

    public List<Posts> findAll() {
        return em.createQuery("select p from Posts as p order by id desc", Posts.class).getResultList();
    }

    public List<Posts> findName(String keyward) {
        return em.createQuery("select p from Posts as p where name like '%" + keyward + "%' order by id desc", Posts.class).getResultList();
    }

    public List<Posts> findTitle(String keyward) {
        return em.createQuery("select p from Posts as p where title like '%" + keyward + "%' order by id desc", Posts.class).getResultList();
    }

    public List<Posts> page(int nowPage, int pagePostNumber) {
        return em.createQuery("select * " +
                "from (select rownum num,* from posts where status like 'INCLUDE' order by id desc) " +
                "where num between " + ((pagePostNumber * nowPage)-(pagePostNumber - 1)) +
                "and " + (pagePostNumber * nowPage), Posts.class).getResultList();
    }
}
