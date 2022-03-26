package com.hoaxify.ws.hoax;

import com.hoaxify.ws.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoaxRepository extends JpaRepository<Hoax, Long> {
    Page<Hoax> findByUser(User user, Pageable pageable);

    Page<Hoax> findByIdLessThan(long id, Pageable pageable);

    Page<Hoax> findByIdLessThanAndUser(long id, User user, Pageable pageable);

    long countByIdGreaterThan(long id);

    long countByIdGreaterThanAndUser(long id, User user);

    List<Hoax> findByIdGreaterThan(long id, Sort sort);

    List<Hoax> findByIdGreaterThanAndUser(long id, Sort sort, User user);
}
