package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.CredencialUsuarioSenhaAssembler;
import com.autobots.automanager.dtos.CredencialUsuarioSenhaDto;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.servicos.CredencialUsuarioSenhaServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/credencial-senha")
public class CredencialUsuarioSenhaControle {

    @Autowired
    private CredencialUsuarioSenhaServico servico;

    @Autowired
    private CredencialUsuarioSenhaAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CredencialUsuarioSenhaDto>>> listarTodas() {
        List<EntityModel<CredencialUsuarioSenhaDto>> lista = servico.listarTodas()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(lista,
                        linkTo(methodOn(CredencialUsuarioSenhaControle.class).listarTodas()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CredencialUsuarioSenhaDto>> buscarPorId(@PathVariable Long id) {
        CredencialUsuarioSenhaDto dto = servico.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    // URL Melhorada seguindo seu padrão REST: /usuario/{id}/credencial-senha
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<CredencialUsuarioSenhaDto>> cadastrar(
            @PathVariable Long usuarioId,
            @RequestBody CredencialUsuarioSenha credencial) {
        
        CredencialUsuarioSenhaDto dto = servico.cadastrar(usuarioId, credencial);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CredencialUsuarioSenhaDto>> atualizar(
            @PathVariable Long id,
            @RequestBody CredencialUsuarioSenha credencial) {
        CredencialUsuarioSenhaDto dto = servico.atualizar(id, credencial);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        servico.deletar(id);
        return ResponseEntity.noContent().build();
    }
}