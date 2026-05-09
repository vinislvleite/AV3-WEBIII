package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.VendaAssembler;
import com.autobots.automanager.dtos.VendaDto;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.servicos.VendaServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/venda")
public class VendaControle {

    @Autowired
    private VendaServico servico;

    @Autowired
    private VendaAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<VendaDto>>> listarTodas() {

        List<EntityModel<VendaDto>> lista = servico.listarTodas()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        lista,
                        linkTo(methodOn(VendaControle.class)
                                .listarTodas())
                                .withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<VendaDto>> buscarPorId(@PathVariable Long id) {

        VendaDto dto = servico.buscarPorId(id);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping
    public ResponseEntity<EntityModel<VendaDto>> cadastrar(
            @RequestBody Venda venda) {

        VendaDto dto = servico.cadastrar(venda);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<VendaDto>> atualizar(
            @PathVariable Long id,
            @RequestBody Venda venda) {

        VendaDto dto = servico.atualizar(id, venda);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        servico.deletar(id);

        return ResponseEntity.noContent().build();
    }
}