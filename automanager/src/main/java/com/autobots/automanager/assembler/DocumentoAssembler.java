package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.dtos.DocumentoDto;

@Component
public class DocumentoAssembler implements
        RepresentationModelAssembler<DocumentoDto, EntityModel<DocumentoDto>> {

    @Override
    public EntityModel<DocumentoDto> toModel(DocumentoDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(DocumentoControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(DocumentoControle.class)
                        .listarTodos())
                        .withRel("documentos"));
    }
}