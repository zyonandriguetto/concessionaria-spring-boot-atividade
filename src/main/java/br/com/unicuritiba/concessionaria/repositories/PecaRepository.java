package br.com.unicuritiba.concessionaria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unicuritiba.concessionaria.models.Peca;

public interface PecaRepository
		extends JpaRepository<Peca, Long> {

}
