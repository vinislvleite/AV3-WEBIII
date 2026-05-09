package com.autobots.automanager.servicos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.CredencialCodigoBarraDto;
import com.autobots.automanager.entitades.CredencialCodigoBarra;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioCredencialCodigoBarra;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class CredencialCodigoBarraServico {

    @Autowired
    private RepositorioCredencialCodigoBarra repositorio;
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<CredencialCodigoBarraDto> listarTodas() {
        return repositorio.findAll().stream().map(this::paraDto).collect(Collectors.toList());
    }

    public CredencialCodigoBarraDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Credencial não encontrada: " + id)));
    }

    public CredencialCodigoBarraDto cadastrar(Long usuarioId, CredencialCodigoBarra credencial) {
        Usuario usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + usuarioId));
        
        if (credencial.getCriacao() == null) {
            credencial.setCriacao(new Date());
        }
        
        CredencialCodigoBarra credencialSalva = repositorio.save(credencial);
        usuario.getCredenciais().add(credencialSalva);
        repositorioUsuario.save(usuario);
        
        return paraDto(credencialSalva);
    }

    public CredencialCodigoBarraDto atualizar(Long id, CredencialCodigoBarra dados) {
        CredencialCodigoBarra credencial = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Credencial não encontrada: " + id));
        credencial.setCodigo(dados.getCodigo());
        credencial.setInativo(dados.isInativo());
        credencial.setUltimoAcesso(dados.getUltimoAcesso());
        return paraDto(repositorio.save(credencial));
    }

    public void deletar(Long id) {
        repositorio.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Credencial não encontrada: " + id));
        repositorio.deleteById(id);
    }

    private CredencialCodigoBarraDto paraDto(CredencialCodigoBarra c) {
        CredencialCodigoBarraDto dto = new CredencialCodigoBarraDto();
        dto.setId(c.getId());
        dto.setCriacao(c.getCriacao());
        dto.setUltimoAcesso(c.getUltimoAcesso());
        dto.setInativo(c.isInativo());
        dto.setCodigo(c.getCodigo());
        return dto;
    }
}