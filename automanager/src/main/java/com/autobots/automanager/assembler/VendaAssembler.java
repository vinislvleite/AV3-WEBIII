package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.dtos.VendaDto;

@Component
public class VendaAssembler implements
        RepresentationModelAssembler<VendaDto, EntityModel<VendaDto>> {

    @Override
    public EntityModel<VendaDto> toModel(VendaDto dto) {

        EntityModel<VendaDto> model = EntityModel.of(dto,

                linkTo(methodOn(VendaControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(VendaControle.class)
                        .listarTodas())
                        .withRel("vendas"));

        if (dto.getClienteId() != null) {

            model.add(
                    linkTo(methodOn(UsuarioControle.class)
                            .buscarPorId(dto.getClienteId()))
                            .withRel("cliente"));
        }

        if (dto.getFuncionarioId() != null) {

            model.add(
                    linkTo(methodOn(UsuarioControle.class)
                            .buscarPorId(dto.getFuncionarioId()))
                            .withRel("funcionario"));
        }

        if (dto.getVeiculoId() != null) {

            model.add(
                    linkTo(methodOn(VeiculoControle.class)
                            .buscarPorId(dto.getVeiculoId()))
                            .withRel("veiculo"));
        }

        return model;
    }
}