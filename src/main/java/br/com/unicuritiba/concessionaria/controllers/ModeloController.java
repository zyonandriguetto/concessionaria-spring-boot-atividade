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
import br.com.unicuritiba.concessionaria.models.Peca;
import br.com.unicuritiba.concessionaria.models.Plataforma;
import br.com.unicuritiba.concessionaria.repositories.ModeloRepository;
import br.com.unicuritiba.concessionaria.repositories.PlataformaRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/modelo")
public class ModeloController {

	final ModeloRepository repository;
	final PlataformaRepository plataformaRepository;

	ModeloController(ModeloRepository repository,
			PlataformaRepository plataformaRepository) {
		this.repository = repository;
		this.plataformaRepository = plataformaRepository;
	}

	@GetMapping
	public ResponseEntity<List<Modelo>> getAllModelos() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Modelo> getModeloById(@PathVariable long id) {
		return repository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/{id}/pecas")
	public ResponseEntity<List<Peca>> getPecasDoModelo(@PathVariable long id) {
		return repository.findById(id)
				.map(modelo -> ResponseEntity.ok(modelo.getPecas()))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Modelo> saveModelo(@RequestBody Modelo modelo) {
		Plataforma plataforma = buscarPlataforma(modelo);
		if (plataforma == null) {
			return ResponseEntity.badRequest().build();
		}
		modelo.setId(0);
		modelo.setPlataforma(plataforma);
		return ResponseEntity.ok(repository.save(modelo));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Modelo> updateModelo(
			@PathVariable long id,
			@RequestBody Modelo dados) {
		var existente = repository.findById(id);
		if (existente.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Plataforma plataforma = buscarPlataforma(dados);
		if (plataforma == null) {
			return ResponseEntity.badRequest().build();
		}
		Modelo modelo = existente.get();
		modelo.setNome(dados.getNome());
		modelo.setCarroceria(dados.getCarroceria());
		modelo.setCambio(dados.getCambio());
		modelo.setMotor(dados.getMotor());
		modelo.setAno(dados.getAno());
		modelo.setPlataforma(plataforma);
		return ResponseEntity.ok(repository.save(modelo));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeModelo(@PathVariable long id) {
		var modelo = repository.findById(id);
		if (modelo.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		var pecas = modelo.get().getPecas();
		if (pecas != null && !pecas.isEmpty()) {
			// não deixa apagar um modelo que ainda tem peças
			return ResponseEntity.status(409).build();
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// Busca no banco a plataforma informada pelo id no JSON
	private Plataforma buscarPlataforma(Modelo modelo) {
		if (modelo.getPlataforma() == null) {
			return null;
		}
		return plataformaRepository
				.findById(modelo.getPlataforma().getId())
				.orElse(null);
	}
}
