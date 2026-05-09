package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.TelefoneAssembler;
import com.autobots.automanager.dtos.TelefoneDto;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.servicos.TelefoneServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private TelefoneServico servico;

    @Autowired
    private TelefoneAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<TelefoneDto>>> listarTodos() {
        List<EntityModel<TelefoneDto>> lista = servico.listarTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(lista, linkTo(methodOn(TelefoneControle.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TelefoneDto>> buscarPorId(@PathVariable Long id) {
        TelefoneDto dto = servico.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<TelefoneDto>> cadastrarParaUsuario(@PathVariable Long usuarioId, @RequestBody Telefone telefone) {
        TelefoneDto dto = servico.cadastrarParaUsuario(usuarioId, telefone);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(dto));
    }

    @PostMapping("/empresa/{empresaId}")
    public ResponseEntity<EntityModel<TelefoneDto>> cadastrarParaEmpresa(@PathVariable Long empresaId, @RequestBody Telefone telefone) {
        TelefoneDto dto = servico.cadastrarParaEmpresa(empresaId, telefone);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<TelefoneDto>> atualizar(@PathVariable Long id, @RequestBody Telefone telefone) {
        TelefoneDto dto = servico.atualizar(id, telefone);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        servico.deletar(id);
        return ResponseEntity.noContent().build();
    }
}