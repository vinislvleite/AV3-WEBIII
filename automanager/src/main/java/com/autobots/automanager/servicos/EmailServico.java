package com.autobots.automanager.servicos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.EmailDto;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class EmailServico {

    @Autowired
    private RepositorioEmail repositorio;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioEmail repositorioEmail;

    public List<EmailDto> listarTodos() {
        return repositorio.findAll().stream().map(this::paraDto).collect(Collectors.toList());
    }

    public EmailDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Email não encontrado: " + id)));
    }

    public EmailDto cadastrar(Long usuarioId, Email email) {
    Usuario usuario = repositorioUsuario.findById(usuarioId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + usuarioId));
    Email emailSalvo = repositorioEmail.save(email);
    usuario.getEmails().add(emailSalvo);
    repositorioUsuario.save(usuario);

    return paraDto(emailSalvo); 
}

    public EmailDto atualizar(Long id, Email dados) {
        Email email = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Email não encontrado: " + id));
        email.setEndereco(dados.getEndereco());
        return paraDto(repositorio.save(email));
    }

    public void deletar(Long id) {
        repositorio.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Email não encontrado: " + id));
        repositorio.deleteById(id);
    }

    private EmailDto paraDto(Email e) {
        EmailDto dto = new EmailDto();
        dto.setId(e.getId());
        dto.setEndereco(e.getEndereco());
        return dto;
    }
}