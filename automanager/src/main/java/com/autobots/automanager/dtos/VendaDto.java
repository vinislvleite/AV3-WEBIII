package com.autobots.automanager.dtos;

import java.util.Date;
import java.util.Set;
import lombok.Data;

@Data
public class VendaDto {
    private Long id;
    private Date cadastro;
    private String identificacao;
    private Long clienteId;
    private Long funcionarioId;
    private Long veiculoId;
    private Set<Long> mercadoriaIds;
    private Set<Long> servicoIds;
}