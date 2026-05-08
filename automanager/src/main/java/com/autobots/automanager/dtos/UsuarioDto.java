package com.autobots.automanager.dtos;

import java.util.Set;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import lombok.Data;

@Data
public class UsuarioDto {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<PerfilUsuario> perfis;
    private Set<Long> mercadoriaIds;
    private Set<Long> vendaIds;
    private Set<Long> veiculoIds;
}