package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.dtos.ServicoDto;

@Component
public class ServicoAssembler implements RepresentationModelAssembler<ServicoDto, EntityModel<ServicoDto>> {

    @Override
    public EntityModel<ServicoDto> toModel(ServicoDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(ServicoControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(ServicoControle.class)
                        .listarTodos())
                        .withRel("servicos"));
    }
}