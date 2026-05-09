package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.assembler.DocumentoAssembler;
import com.autobots.automanager.dtos.DocumentoDto;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.servicos.DocumentoServico;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private DocumentoServico servico;

    @Autowired
    private DocumentoAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<DocumentoDto>>> listarTodos() {
        List<EntityModel<DocumentoDto>> lista = servico.listarTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        lista,
                        linkTo(methodOn(DocumentoControle.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DocumentoDto>> buscarPorId(@PathVariable Long id) {
        DocumentoDto dto = servico.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<EntityModel<DocumentoDto>> cadastrar(@PathVariable Long usuarioId, @RequestBody Documento documento) {
        DocumentoDto dto = servico.cadastrar(usuarioId, documento);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<DocumentoDto>> atualizar(@PathVariable Long id, @RequestBody Documento documento) {
        DocumentoDto dto = servico.atualizar(id, documento);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        servico.deletar(id);
        return ResponseEntity.noContent().build();
    }
}