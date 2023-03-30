package com.curso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.curso.model.Telefone;

@Repository
@Transactional
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

}
