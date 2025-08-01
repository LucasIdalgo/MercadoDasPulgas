package com.challenge.Desafio.Repositories;

import com.challenge.Desafio.Entities.MoedasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoedaRepository extends JpaRepository<MoedasEntity, Long> {
}
