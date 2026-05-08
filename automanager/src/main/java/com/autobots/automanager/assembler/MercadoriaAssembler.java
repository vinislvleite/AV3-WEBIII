package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.dtos.MercadoriaDto;

@Component
public class MercadoriaAssembler implements RepresentationModelAssembler<MercadoriaDto, EntityModel<MercadoriaDto>> {

    @Override
    public EntityModel<MercadoriaDto> toModel(MercadoriaDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(MercadoriaControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(MercadoriaControle.class)
                        .listarTodas())
                        .withRel("mercadorias"));
    }
}