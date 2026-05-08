package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.dtos.VeiculoDto;

@Component
public class VeiculoAssembler implements RepresentationModelAssembler<VeiculoDto, EntityModel<VeiculoDto>> {

    @Override
    public EntityModel<VeiculoDto> toModel(VeiculoDto dto) {

        EntityModel<VeiculoDto> model = EntityModel.of(dto,

                linkTo(methodOn(VeiculoControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(VeiculoControle.class)
                        .listarTodos())
                        .withRel("veiculos"));

        if (dto.getProprietarioId() != null) {

            model.add(
                    linkTo(methodOn(UsuarioControle.class)
                            .buscarPorId(dto.getProprietarioId()))
                            .withRel("proprietario"));
        }

        return model;
    }
}