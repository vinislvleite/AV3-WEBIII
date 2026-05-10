package com.autobots.automanager.dtos;

import java.util.Date;
import java.util.Set;
import lombok.Data;

@Data
public class EmpresaDto {

    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private Date cadastro;

    private Set<UsuarioDto> usuarios;
    private Set<Long> mercadoriaIds;
    private Set<Long> servicoIds;
    private Set<Long> vendaIds;
}