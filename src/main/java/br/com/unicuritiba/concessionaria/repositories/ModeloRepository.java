package br.com.unicuritiba.concessionaria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unicuritiba.concessionaria.models.Modelo;

public interface ModeloRepository 
	extends JpaRepository<Modelo, Long>{

}
