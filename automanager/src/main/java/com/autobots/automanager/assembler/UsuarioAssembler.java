package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.dtos.UsuarioDto;

@Component
public class UsuarioAssembler implements RepresentationModelAssembler<UsuarioDto, EntityModel<UsuarioDto>> {

    @Override
    public EntityModel<UsuarioDto> toModel(UsuarioDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(UsuarioControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(UsuarioControle.class)
                        .listarTodos())
                        .withRel("usuarios"));
    }
}