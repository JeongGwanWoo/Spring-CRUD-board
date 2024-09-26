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

    public List<Posts> findName(String keyward, int nowPage, int pagePostNumber) {
        int first = ((pagePostNumber * nowPage) - 1)-(pagePostNumber - 1);
        int max = pagePostNumber;
        return em.createQuery("select p from Posts as p where name like '%" + keyward + "%' and status like 'INCLUDE' order by id desc", Posts.class)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }

    public List<Posts> findTitle(String keyward, int nowPage, int pagePostNumber) {
        int first = ((pagePostNumber * nowPage) - 1)-(pagePostNumber - 1);
        int max = pagePostNumber;
        return em.createQuery("select p from Posts as p where title like '%" + keyward + "%' and status like 'INCLUDE' order by id desc", Posts.class)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }

    public List<Posts> page(int nowPage, int pagePostNumber) {
        int first = ((pagePostNumber * nowPage) - 1)-(pagePostNumber - 1);
        int max = pagePostNumber;
        return em.createQuery("select p from Posts as p where status like 'INCLUDE' order by id desc", Posts.class)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
}
