package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.CredencialCodigoBarraControle;
import com.autobots.automanager.dtos.CredencialCodigoBarraDto;

@Component
public class CredencialCodigoBarraAssembler implements
        RepresentationModelAssembler<CredencialCodigoBarraDto, EntityModel<CredencialCodigoBarraDto>> {

    @Override
    public EntityModel<CredencialCodigoBarraDto> toModel(CredencialCodigoBarraDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(CredencialCodigoBarraControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(CredencialCodigoBarraControle.class)
                        .listarTodas())
                        .withRel("credenciais-codigo-barra"));
    }
}