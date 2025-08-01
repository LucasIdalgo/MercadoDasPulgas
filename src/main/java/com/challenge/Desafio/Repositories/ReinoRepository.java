package com.challenge.Desafio.Repositories;

import com.challenge.Desafio.Entities.ReinosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReinoRepository extends JpaRepository<ReinosEntity, Long> {
}
