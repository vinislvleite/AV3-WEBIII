package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.CredencialCodigoBarraAssembler;
import com.autobots.automanager.dtos.CredencialCodigoBarraDto;
import com.autobots.automanager.entitades.CredencialCodigoBarra;
import com.autobots.automanager.servicos.CredencialCodigoBarraServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/credencial/codigo-barra")
public class CredencialCodigoBarraControle {

    @Autowired
    private CredencialCodigoBarraServico servico;

    @Autowired
    private CredencialCodigoBarraAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CredencialCodigoBarraDto>>> listarTodas() {

        List<EntityModel<CredencialCodigoBarraDto>> lista = servico.listarTodas()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        lista,
                        linkTo(methodOn(CredencialCodigoBarraControle.class)
                                .listarTodas())
                                .withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CredencialCodigoBarraDto>> buscarPorId(@PathVariable Long id) {

        CredencialCodigoBarraDto dto = servico.buscarPorId(id);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<CredencialCodigoBarraDto>> cadastrar(
            @PathVariable Long usuarioId,
            @RequestBody CredencialCodigoBarra credencial) {

        CredencialCodigoBarraDto dto = servico.cadastrar(usuarioId, credencial);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CredencialCodigoBarraDto>> atualizar(
            @PathVariable Long id,
            @RequestBody CredencialCodigoBarra credencial) {

        CredencialCodigoBarraDto dto = servico.atualizar(id, credencial);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        servico.deletar(id);

        return ResponseEntity.noContent().build();
    }
}