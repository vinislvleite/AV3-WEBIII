package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.EnderecoAssembler;
import com.autobots.automanager.dtos.EnderecoDto;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.servicos.EnderecoServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

    @Autowired
    private EnderecoServico servico;
    @Autowired
    private EnderecoAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EnderecoDto>>> listarTodos() {
        List<EntityModel<EnderecoDto>> lista = servico.listarTodos().stream().map(assembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(EnderecoControle.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EnderecoDto>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(servico.buscarPorId(id)));
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<EnderecoDto>> cadastrarParaUsuario(@PathVariable Long usuarioId, @RequestBody Endereco endereco) {
        EnderecoDto dto = servico.cadastrarParaUsuario(usuarioId, endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(dto));
    }

    @PostMapping("/empresa/{empresaId}")
    public ResponseEntity<EntityModel<EnderecoDto>> cadastrarParaEmpresa(@PathVariable Long empresaId, @RequestBody Endereco endereco) {
        EnderecoDto dto = servico.cadastrarParaEmpresa(empresaId, endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EnderecoDto>> atualizar(@PathVariable Long id, @RequestBody Endereco endereco) {
        return ResponseEntity.ok(assembler.toModel(servico.atualizar(id, endereco)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        servico.deletar(id);
        return ResponseEntity.noContent().build();
    }
}