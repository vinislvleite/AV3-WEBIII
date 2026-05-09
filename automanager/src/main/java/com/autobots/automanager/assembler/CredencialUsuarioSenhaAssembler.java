package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.CredencialUsuarioSenhaControle;
import com.autobots.automanager.dtos.CredencialUsuarioSenhaDto;

@Component
public class CredencialUsuarioSenhaAssembler implements
        RepresentationModelAssembler<CredencialUsuarioSenhaDto, EntityModel<CredencialUsuarioSenhaDto>> {

    @Override
    public EntityModel<CredencialUsuarioSenhaDto> toModel(CredencialUsuarioSenhaDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(CredencialUsuarioSenhaControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(CredencialUsuarioSenhaControle.class)
                        .listarTodas())
                        .withRel("credenciais-usuario-senha"));
    }
}