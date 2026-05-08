package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.UsuarioAssembler;
import com.autobots.automanager.dtos.UsuarioDto;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.servicos.UsuarioServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private UsuarioServico servico;

    @Autowired
    private UsuarioAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDto>>> listarTodos() {

        List<EntityModel<UsuarioDto>> usuarios = servico.listarTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        usuarios,
                        linkTo(methodOn(UsuarioControle.class)
                                .listarTodos())
                                .withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioDto>> buscarPorId(@PathVariable Long id) {

        UsuarioDto dto = servico.buscarPorId(id);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping
    public ResponseEntity<EntityModel<UsuarioDto>> cadastrar(@RequestBody Usuario usuario) {

        UsuarioDto dto = servico.cadastrar(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioDto>> atualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        UsuarioDto dto = servico.atualizar(id, usuario);

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        servico.deletar(id);

        return ResponseEntity.noContent().build();
    }
}