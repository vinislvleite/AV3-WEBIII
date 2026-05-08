package com.autobots.automanager.servicos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.UsuarioDto;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UsuarioServico {

    @Autowired
    private RepositorioUsuario repositorio;

    public List<UsuarioDto> listarTodos() {
        return repositorio.findAll().stream()
                .map(this::paraDto)
                .collect(Collectors.toList());
    }

    public UsuarioDto buscarPorId(Long id) {
        Usuario usuario = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        return paraDto(usuario);
    }

    public UsuarioDto cadastrar(Usuario usuario) {
        return paraDto(repositorio.save(usuario));
    }

    public UsuarioDto atualizar(Long id, Usuario dados) {
        Usuario usuario = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        usuario.setNome(dados.getNome());
        usuario.setNomeSocial(dados.getNomeSocial());
        usuario.setPerfis(dados.getPerfis());
        usuario.setTelefones(dados.getTelefones());
        usuario.setEndereco(dados.getEndereco());
        usuario.setDocumentos(dados.getDocumentos());
        usuario.setEmails(dados.getEmails());
        usuario.setCredenciais(dados.getCredenciais());
        return paraDto(repositorio.save(usuario));
    }

    public void deletar(Long id) {
        repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        repositorio.deleteById(id);
    }

    private UsuarioDto paraDto(Usuario u) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setNomeSocial(u.getNomeSocial());
        dto.setPerfis(u.getPerfis());
        if (u.getMercadorias() != null)
            dto.setMercadoriaIds(u.getMercadorias().stream().map(m -> m.getId()).collect(Collectors.toSet()));
        if (u.getVendas() != null)
            dto.setVendaIds(u.getVendas().stream().map(v -> v.getId()).collect(Collectors.toSet()));
        if (u.getVeiculos() != null)
            dto.setVeiculoIds(u.getVeiculos().stream().map(v -> v.getId()).collect(Collectors.toSet()));
        return dto;
    }
}