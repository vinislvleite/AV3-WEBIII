package com.autobots.automanager.servicos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.CredencialUsuarioSenhaDto;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioCredencialUsuarioSenha;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class CredencialUsuarioSenhaServico {

    @Autowired
    private RepositorioCredencialUsuarioSenha repositorio;
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<CredencialUsuarioSenhaDto> listarTodas() {
        return repositorio.findAll().stream().map(this::paraDto).collect(Collectors.toList());
    }

    public CredencialUsuarioSenhaDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Credencial não encontrada: " + id)));
    }

    public CredencialUsuarioSenhaDto cadastrar(Long usuarioId, CredencialUsuarioSenha credencial) {
    Usuario usuario = repositorioUsuario.findById(usuarioId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + usuarioId));
    
    if (credencial.getCriacao() == null) {
        credencial.setCriacao(new Date());
    }

    if (credencial.getUltimoAcesso() == null) {
        credencial.setUltimoAcesso(new Date());
    }
    
    CredencialUsuarioSenha credencialSalva = repositorio.save(credencial);
    usuario.getCredenciais().add(credencialSalva);
    repositorioUsuario.save(usuario);
    
    return paraDto(credencialSalva);
    }

    public CredencialUsuarioSenhaDto atualizar(Long id, CredencialUsuarioSenha dados) {
        CredencialUsuarioSenha credencial = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Credencial não encontrada: " + id));
        credencial.setNomeUsuario(dados.getNomeUsuario());
        credencial.setSenha(dados.getSenha());
        credencial.setInativo(dados.isInativo());
        return paraDto(repositorio.save(credencial));
    }

    public void deletar(Long id) {
        repositorio.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Credencial não encontrada: " + id));
        repositorio.deleteById(id);
    }

    private CredencialUsuarioSenhaDto paraDto(CredencialUsuarioSenha c) {
        CredencialUsuarioSenhaDto dto = new CredencialUsuarioSenhaDto();
        dto.setId(c.getId());
        dto.setNomeUsuario(c.getNomeUsuario());
        dto.setInativo(c.isInativo());
        dto.setCriacao(c.getCriacao());
        return dto;
    }
}