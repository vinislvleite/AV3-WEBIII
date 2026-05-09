package com.autobots.automanager.servicos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.TelefoneDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class TelefoneServico {

    @Autowired
    private RepositorioTelefone repositorio;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioEmpresa repositorioEmpresa;
    @Autowired
    private RepositorioTelefone repositorioTelefone;

    public List<TelefoneDto> listarTodos() {
        return repositorio.findAll().stream().map(this::paraDto).collect(Collectors.toList());
    }

    public TelefoneDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Telefone não encontrado: " + id)));
    }

    public TelefoneDto cadastrarParaUsuario(Long usuarioId, Telefone telefone) {
    Usuario usuario = repositorioUsuario.findById(usuarioId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado"));
    Telefone telefoneSalvo = repositorioTelefone.save(telefone);
    usuario.getTelefones().add(telefoneSalvo);
    repositorioUsuario.save(usuario);
    return paraDto(telefoneSalvo);
}
    public TelefoneDto cadastrarParaEmpresa(Long empresaId, Telefone telefone) {
        Empresa empresa = repositorioEmpresa.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Empresa não encontrada: " + empresaId));
        empresa.getTelefones().add(telefone);
        repositorioEmpresa.save(empresa);
        return paraDto(telefone);
    }

    public TelefoneDto atualizar(Long id, Telefone dados) {
        Telefone telefone = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Telefone não encontrado: " + id));
        telefone.setDdd(dados.getDdd());
        telefone.setNumero(dados.getNumero());
        return paraDto(repositorio.save(telefone));
    }

    public void deletar(Long id) {
        repositorio.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Telefone não encontrado: " + id));
        repositorio.deleteById(id);
    }

    private TelefoneDto paraDto(Telefone t) {
        TelefoneDto dto = new TelefoneDto();
        dto.setId(t.getId());
        dto.setDdd(t.getDdd());
        dto.setNumero(t.getNumero());
        return dto;
    }
}