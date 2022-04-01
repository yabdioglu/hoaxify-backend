package com.hoaxify.ws.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Page<User> findByUsernameNot(String username, Pageable page); // Bu username'e sahip olmayanları getir.

    @Transactional // Transaction yoksa Spring bizim yerimize bir transaction oluşturuyor.
    void deleteByUsername(String username);
}
