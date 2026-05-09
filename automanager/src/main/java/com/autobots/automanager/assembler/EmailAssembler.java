package com.autobots.automanager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmailControle;
import com.autobots.automanager.dtos.EmailDto;

@Component
public class EmailAssembler implements
        RepresentationModelAssembler<EmailDto, EntityModel<EmailDto>> {

    @Override
    public EntityModel<EmailDto> toModel(EmailDto dto) {

        return EntityModel.of(dto,

                linkTo(methodOn(EmailControle.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(EmailControle.class)
                        .listarTodos())
                        .withRel("emails"));
    }
}