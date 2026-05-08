package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.VeiculoAssembler;
import com.autobots.automanager.dtos.VeiculoDto;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.servicos.VeiculoServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

    @Autowired
    private VeiculoServico servico;

    @Autowired
    private VeiculoAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<VeiculoDto>>> listarTodos() {

        List<EntityModel<VeiculoDto>> veiculos = servico.listarTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        veiculos,
                        linkTo(methodOn(VeiculoControle.class)
                                .listarTodos())
                                .withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<VeiculoDto>> buscarPorId(@PathVariable Long id) {

        VeiculoDto dto = servico.buscarPorId(id);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping
    public ResponseEntity<EntityModel<VeiculoDto>> cadastrar(@RequestBody Veiculo veiculo) {

        VeiculoDto dto = servico.cadastrar(veiculo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<VeiculoDto>> atualizar(
            @PathVariable Long id,
            @RequestBody Veiculo veiculo) {

        VeiculoDto dto = servico.atualizar(id, veiculo);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        servico.deletar(id);

        return ResponseEntity.noContent().build();
    }
}