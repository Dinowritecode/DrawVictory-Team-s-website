package com.evencyan.dao;

import com.evencyan.domain.PendingUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingUserDAO extends CrudRepository<PendingUser, String> {
    PendingUser findOneByUsername(String username);
    PendingUser findOneByEmail(String email);
    List<PendingUser> findAllByUsername(String username);
    List<PendingUser> findAllByEmail(String email);
}
