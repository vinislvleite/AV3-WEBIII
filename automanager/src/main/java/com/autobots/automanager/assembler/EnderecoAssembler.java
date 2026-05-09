package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.dtos.EnderecoDto;

@Component
public class EnderecoAssembler implements
        RepresentationModelAssembler<EnderecoDto, EntityModel<EnderecoDto>> {

    @Override
    public EntityModel<EnderecoDto> toModel(EnderecoDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(EnderecoControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(EnderecoControle.class)
                        .listarTodos())
                        .withRel("enderecos"));
    }
}