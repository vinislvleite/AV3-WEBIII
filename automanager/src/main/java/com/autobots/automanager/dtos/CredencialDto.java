package com.autobots.automanager.dtos;

import java.util.Date;
import lombok.Data;

@Data
public class CredencialDto {
    private Long id;
    private Date criacao;
    private Date ultimoAcesso;
    private boolean inativo;
}