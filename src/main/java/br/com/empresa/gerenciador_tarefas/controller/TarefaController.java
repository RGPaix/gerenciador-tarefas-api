package br.com.empresa.gerenciador_tarefas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.empresa.gerenciador_tarefas.model.Tarefa;
import br.com.empresa.gerenciador_tarefas.persistence.TarefaRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired // Injeção de dependência: o Spring nos fornece uma instância do TarefaRepository.
    private TarefaRepository tarefaRepository;

    @PostMapping // Mapeia para requisições HTTP POST
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    @GetMapping // Mapeia para requisições HTTP GET
    public List<Tarefa> listarTarefas() {
        return tarefaRepository.findAll();
    }

    @GetMapping("/{id}") // O {id} na URL é passado como um parâmetro para o método
    public ResponseEntity<Tarefa> buscarTarefaPorId(@PathVariable Long id) {
        return tarefaRepository.findById(id)
                .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com a tarefa
                .orElse(ResponseEntity.notFound().build()); // Senão, retorna 404 Not Found
    }

    @PutMapping("/{id}") // Mapeia para requisições HTTP PUT
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa detalhes) {
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    tarefa.setTitulo(detalhes.getTitulo());
                    tarefa.setDescricao(detalhes.getDescricao());
                    tarefa.setConcluida(detalhes.isConcluida());
                    return ResponseEntity.ok(tarefaRepository.save(tarefa));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") // Mapeia para requisições HTTP DELETE
    public ResponseEntity<?> deletarTarefa(@PathVariable Long id) {
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    tarefaRepository.delete(tarefa);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
