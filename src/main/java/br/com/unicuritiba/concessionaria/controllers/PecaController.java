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
import br.com.unicuritiba.concessionaria.repositories.ModeloRepository;
import br.com.unicuritiba.concessionaria.repositories.PecaRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/peca")
public class PecaController {

	final PecaRepository repository;
	final ModeloRepository modeloRepository;

	PecaController(PecaRepository repository,
			ModeloRepository modeloRepository) {
		this.repository = repository;
		this.modeloRepository = modeloRepository;
	}

	@GetMapping
	public ResponseEntity<List<Peca>> getAllPecas() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Peca> getPecaById(@PathVariable long id) {
		return repository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Peca> savePeca(@RequestBody Peca peca) {
		Modelo modelo = buscarModelo(peca);
		if (modelo == null) {
			return ResponseEntity.badRequest().build();
		}
		peca.setId(0);
		peca.setModelo(modelo);
		return ResponseEntity.ok(repository.save(peca));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Peca> updatePeca(
			@PathVariable long id,
			@RequestBody Peca dados) {
		var existente = repository.findById(id);
		if (existente.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Modelo modelo = buscarModelo(dados);
		if (modelo == null) {
			return ResponseEntity.badRequest().build();
		}
		Peca peca = existente.get();
		peca.setNome(dados.getNome());
		peca.setCodigo(dados.getCodigo());
		peca.setFabricante(dados.getFabricante());
		peca.setPreco(dados.getPreco());
		peca.setModelo(modelo);
		return ResponseEntity.ok(repository.save(peca));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removePeca(@PathVariable long id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// Busca no banco o modelo informado pelo id no JSON
	private Modelo buscarModelo(Peca peca) {
		if (peca.getModelo() == null) {
			return null;
		}
		return modeloRepository
				.findById(peca.getModelo().getId())
				.orElse(null);
	}
}
