package com.elbundo.Organizerbackend.repositories;

import com.elbundo.Organizerbackend.models.RefreshToken;
import com.elbundo.Organizerbackend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> getByUser(User user);
    void deleteByUser(User user);
}
