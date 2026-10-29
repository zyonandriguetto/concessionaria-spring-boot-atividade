package br.com.unicuritiba.concessionaria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unicuritiba.concessionaria.models.Plataforma;

public interface PlataformaRepository 
		extends JpaRepository<Plataforma, Long> {

}
