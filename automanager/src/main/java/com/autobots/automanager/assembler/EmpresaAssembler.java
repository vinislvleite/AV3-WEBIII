package com.autobots.automanager.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.dtos.EmpresaDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EmpresaAssembler implements RepresentationModelAssembler<EmpresaDto, EntityModel<EmpresaDto>> {

    @Override
    public EntityModel<EmpresaDto> toModel(EmpresaDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(EmpresaControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(EmpresaControle.class)
                        .listarTodas())
                        .withRel("empresas"));
    }
}