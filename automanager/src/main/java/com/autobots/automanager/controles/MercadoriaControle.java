package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.MercadoriaAssembler;
import com.autobots.automanager.dtos.MercadoriaDto;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.servicos.MercadoriaServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

    @Autowired
    private MercadoriaServico servico;

    @Autowired
    private MercadoriaAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<MercadoriaDto>>> listarTodas() {

        List<EntityModel<MercadoriaDto>> mercadorias = servico.listarTodas()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        mercadorias,
                        linkTo(methodOn(MercadoriaControle.class)
                                .listarTodas())
                                .withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MercadoriaDto>> buscarPorId(@PathVariable Long id) {

        MercadoriaDto dto = servico.buscarPorId(id);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping
    public ResponseEntity<EntityModel<MercadoriaDto>> cadastrar(@RequestBody Mercadoria mercadoria) {

        MercadoriaDto dto = servico.cadastrar(mercadoria);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<MercadoriaDto>> atualizar(
            @PathVariable Long id,
            @RequestBody Mercadoria mercadoria) {

        MercadoriaDto dto = servico.atualizar(id, mercadoria);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        servico.deletar(id);

        return ResponseEntity.noContent().build();
    }
}