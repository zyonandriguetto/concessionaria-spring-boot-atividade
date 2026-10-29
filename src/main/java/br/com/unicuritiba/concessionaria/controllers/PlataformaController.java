package br.com.unicuritiba.concessionaria.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unicuritiba.concessionaria.models.Modelo;
import br.com.unicuritiba.concessionaria.models.Plataforma;
import br.com.unicuritiba.concessionaria.repositories.PlataformaRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/plataforma")
public class PlataformaController {

	final PlataformaRepository repository;

	PlataformaController(PlataformaRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public ResponseEntity<List<Plataforma>> getAllPlataformas() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Plataforma> getPlataformaById(@PathVariable long id) {
		return repository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/{id}/modelos")
	public ResponseEntity<List<Modelo>> getModelosDaPlataforma(@PathVariable long id) {
		return repository.findById(id)
				.map(plataforma -> ResponseEntity.ok(plataforma.getModelos()))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Plataforma> savePlataforma(@RequestBody Plataforma plataforma) {
		plataforma.setId(0);
		return ResponseEntity.ok(repository.save(plataforma));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Plataforma> updatePlataforma(
			@PathVariable long id,
			@RequestBody Plataforma dados) {
		return repository.findById(id)
				.map(plataforma -> {
					plataforma.setNome(dados.getNome());
					return ResponseEntity.ok(repository.save(plataforma));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removePlataforma(@PathVariable long id) {
		var plataforma = repository.findById(id);
		if (plataforma.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		var modelos = plataforma.get().getModelos();
		if (modelos != null && !modelos.isEmpty()) {
			// não deixa apagar uma plataforma que ainda tem modelos
			return ResponseEntity.status(409).build();
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
