package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.ServicoAssembler;
import com.autobots.automanager.dtos.ServicoDto;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.servicos.ServicoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

    @Autowired
    private ServicoService servico;

    @Autowired
    private ServicoAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ServicoDto>>> listarTodos() {

        List<EntityModel<ServicoDto>> servicos = servico.listarTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        servicos,
                        linkTo(methodOn(ServicoControle.class)
                                .listarTodos())
                                .withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ServicoDto>> buscarPorId(@PathVariable Long id) {

        ServicoDto dto = servico.buscarPorId(id);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ServicoDto>> cadastrar(@RequestBody Servico s) {

        ServicoDto dto = servico.cadastrar(s);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ServicoDto>> atualizar(
            @PathVariable Long id,
            @RequestBody Servico s) {

        ServicoDto dto = servico.atualizar(id, s);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        servico.deletar(id);

        return ResponseEntity.noContent().build();
    }
}