package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.dtos.TelefoneDto;

@Component
public class TelefoneAssembler implements
        RepresentationModelAssembler<TelefoneDto, EntityModel<TelefoneDto>> {

    @Override
    public EntityModel<TelefoneDto> toModel(TelefoneDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(TelefoneControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(TelefoneControle.class)
                        .listarTodos())
                        .withRel("telefones"));
    }
}