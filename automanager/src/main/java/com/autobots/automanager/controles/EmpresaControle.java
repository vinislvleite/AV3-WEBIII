package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.EmpresaAssembler;
import com.autobots.automanager.dtos.EmpresaDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.servicos.EmpresaServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

    @Autowired
    private EmpresaServico servico;

    @Autowired
    private EmpresaAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EmpresaDto>>> listarTodas() {

        List<EntityModel<EmpresaDto>> empresas = servico.listarTodas()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        empresas,
                        linkTo(methodOn(EmpresaControle.class)
                                .listarTodas())
                                .withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EmpresaDto>> buscarPorId(@PathVariable Long id) {

        EmpresaDto dto = servico.buscarPorId(id);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping
    public ResponseEntity<EntityModel<EmpresaDto>> cadastrar(@RequestBody Empresa empresa) {

        EmpresaDto dto = servico.cadastrar(empresa);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EmpresaDto>> atualizar(
            @PathVariable Long id,
            @RequestBody Empresa empresa) {

        EmpresaDto dto = servico.atualizar(id, empresa);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        servico.deletar(id);

        return ResponseEntity.noContent().build();
    }
}