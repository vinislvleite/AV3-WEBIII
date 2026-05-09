package com.autobots.automanager.servicos;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.UsuarioDto;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.repositorios.RepositorioEndereco;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UsuarioServico {

    @Autowired
    private RepositorioUsuario repositorio;
    @Autowired
    private RepositorioEndereco repositorioEndereco;
    @Autowired
    private RepositorioTelefone repositorioTelefone;
    @Autowired
    private RepositorioDocumento repositorioDocumento;
    @Autowired
    private RepositorioEmail repositorioEmail;

    public List<UsuarioDto> listarTodos() {
        return repositorio.findAll().stream().map(this::paraDto).collect(Collectors.toList());
    }

    public UsuarioDto buscarPorId(Long id) {
        Usuario usuario = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + id));
        return paraDto(usuario);
    }

    public UsuarioDto cadastrar(Usuario usuario) {
        return paraDto(repositorio.save(usuario));
    }

    public UsuarioDto atualizar(Long id, Usuario dados) {
        Usuario usuario = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + id));
        
        usuario.setNome(dados.getNome());
        usuario.setNomeSocial(dados.getNomeSocial());
        
        if (dados.getPerfis() != null) {
            usuario.setPerfis(dados.getPerfis());
        }

        if (dados.getEndereco() != null && dados.getEndereco().getId() != null) {
            Endereco endereco = repositorioEndereco.findById(dados.getEndereco().getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço não encontrado"));
            usuario.setEndereco(endereco);
        }

        if (dados.getTelefones() != null && !dados.getTelefones().isEmpty()) {
            Set<Telefone> telefones = dados.getTelefones().stream()
                .map(t -> repositorioTelefone.findById(t.getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Telefone não encontrado: " + t.getId())))
                .collect(Collectors.toSet());
            usuario.setTelefones(telefones);
        }

        if (dados.getDocumentos() != null && !dados.getDocumentos().isEmpty()) {
            Set<Documento> documentos = dados.getDocumentos().stream()
                .map(d -> repositorioDocumento.findById(d.getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Documento não encontrado: " + d.getId())))
                .collect(Collectors.toSet());
            usuario.setDocumentos(documentos);
        }

        if (dados.getEmails() != null && !dados.getEmails().isEmpty()) {
            Set<Email> emails = dados.getEmails().stream()
                .map(e -> repositorioEmail.findById(e.getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("E-mail não encontrado: " + e.getId())))
                .collect(Collectors.toSet());
            usuario.setEmails(emails);
        }

        return paraDto(repositorio.save(usuario));
    }

    public void deletar(Long id) {
        repositorio.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + id));
        repositorio.deleteById(id);
    }

    private UsuarioDto paraDto(Usuario u) {
    UsuarioDto dto = new UsuarioDto();
    dto.setId(u.getId());
    dto.setNome(u.getNome());
    dto.setNomeSocial(u.getNomeSocial());
    dto.setPerfis(u.getPerfis());
    if (u.getVeiculos() != null) {
        dto.setVeiculoIds(u.getVeiculos().stream()
            .map(Veiculo::getId)
            .collect(Collectors.toSet()));
    }
    if (u.getVendas() != null) {
        dto.setVendaIds(u.getVendas().stream()
            .map(Venda::getId)
            .collect(Collectors.toSet()));
    }
    if (u.getMercadorias() != null) {
        dto.setMercadoriaIds(u.getMercadorias().stream()
            .map(Mercadoria::getId)
            .collect(Collectors.toSet()));
    }

    return dto;
}
}