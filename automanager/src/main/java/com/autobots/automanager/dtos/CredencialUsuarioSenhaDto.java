package com.autobots.automanager.dtos;

import java.util.Date;
import lombok.Data;

@Data
public class CredencialUsuarioSenhaDto {
    private Long id;
    private Date criacao;
    private Date ultimoAcesso;
    private boolean inativo;
    private String nomeUsuario;
    private String senha;
}