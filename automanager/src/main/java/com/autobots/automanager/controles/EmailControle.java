package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.EmailAssembler;
import com.autobots.automanager.dtos.EmailDto;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.servicos.EmailServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/email")
public class EmailControle {

    @Autowired
    private EmailServico servico;
    @Autowired
    private EmailAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EmailDto>>> listarTodos() {
        List<EntityModel<EmailDto>> lista = servico.listarTodos().stream().map(assembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(EmailControle.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EmailDto>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(servico.buscarPorId(id)));
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<EntityModel<EmailDto>> cadastrar(@PathVariable Long usuarioId, @RequestBody Email email) {
        EmailDto dto = servico.cadastrar(usuarioId, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EmailDto>> atualizar(@PathVariable Long id, @RequestBody Email email) {
        return ResponseEntity.ok(assembler.toModel(servico.atualizar(id, email)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        servico.deletar(id);
        return ResponseEntity.noContent().build();
    }
}