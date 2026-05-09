package com.autobots.automanager.servicos;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dtos.EmpresaDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioEndereco;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class EmpresaServico {

    @Autowired
    private RepositorioEmpresa repositorio;

    @Autowired
    private RepositorioTelefone repositorioTelefone;

    @Autowired
    private RepositorioEndereco repositorioEndereco;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<EmpresaDto> listarTodas() {
        return repositorio.findAll()
                .stream()
                .map(this::paraDto)
                .collect(Collectors.toList());
    }

    public EmpresaDto buscarPorId(Long id) {

        Empresa empresa = repositorio.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Empresa não encontrada: " + id));

        return paraDto(empresa);
    }

    public EmpresaDto cadastrar(Empresa empresa) {
        return paraDto(repositorio.save(empresa));
    }

    public EmpresaDto atualizar(Long id, Empresa dados) {

        Empresa empresa = repositorio.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Empresa não encontrada: " + id));

        empresa.setRazaoSocial(dados.getRazaoSocial());
        empresa.setNomeFantasia(dados.getNomeFantasia());

        if (dados.getEndereco() != null
                && dados.getEndereco().getId() != null) {

            Endereco endereco = repositorioEndereco
                    .findById(dados.getEndereco().getId())
                    .orElseThrow(() ->
                            new EntidadeNaoEncontradaException(
                                    "Endereço não encontrado"));

            empresa.setEndereco(endereco);
        }

        if (dados.getTelefones() != null
                && !dados.getTelefones().isEmpty()) {

            Set<Telefone> telefones = dados.getTelefones()
                    .stream()
                    .map(t -> repositorioTelefone.findById(t.getId())
                            .orElseThrow(() ->
                                    new EntidadeNaoEncontradaException(
                                            "Telefone não encontrado: " + t.getId())))
                    .collect(Collectors.toSet());

            empresa.setTelefones(telefones);
        }

        if (dados.getUsuarios() != null
                && !dados.getUsuarios().isEmpty()) {

            Set<Usuario> usuarios = dados.getUsuarios()
                    .stream()
                    .map(u -> repositorioUsuario.findById(u.getId())
                            .orElseThrow(() ->
                                    new EntidadeNaoEncontradaException(
                                            "Usuário não encontrado: " + u.getId())))
                    .collect(Collectors.toSet());

            empresa.setUsuarios(usuarios);
        }

        return paraDto(repositorio.save(empresa));
    }

    public EmpresaDto associarUsuario(Long empresaId, Long usuarioId) {

        Empresa empresa = repositorio.findById(empresaId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Empresa não encontrada: " + empresaId));

        Usuario usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Usuário não encontrado: " + usuarioId));

        empresa.getUsuarios().add(usuario);

        return paraDto(repositorio.save(empresa));
    }

    public EmpresaDto desassociarUsuario(Long empresaId, Long usuarioId) {

        Empresa empresa = repositorio.findById(empresaId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Empresa não encontrada: " + empresaId));

        Usuario usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Usuário não encontrado: " + usuarioId));

        empresa.getUsuarios().remove(usuario);

        return paraDto(repositorio.save(empresa));
    }

    public void deletar(Long id) {

        repositorio.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Empresa não encontrada: " + id));

        repositorio.deleteById(id);
    }

    private EmpresaDto paraDto(Empresa e) {

        EmpresaDto dto = new EmpresaDto();

        dto.setId(e.getId());
        dto.setRazaoSocial(e.getRazaoSocial());
        dto.setNomeFantasia(e.getNomeFantasia());
        dto.setCadastro(e.getCadastro());

        if (e.getUsuarios() != null) {
            dto.setUsuarioIds(
                    e.getUsuarios()
                            .stream()
                            .map(Usuario::getId)
                            .collect(Collectors.toSet()));
        }

        if (e.getMercadorias() != null) {
            dto.setMercadoriaIds(
                    e.getMercadorias()
                            .stream()
                            .map(Mercadoria::getId)
                            .collect(Collectors.toSet()));
        }

        if (e.getServicos() != null) {
            dto.setServicoIds(
                    e.getServicos()
                            .stream()
                            .map(Servico::getId)
                            .collect(Collectors.toSet()));
        }

        if (e.getVendas() != null) {
            dto.setVendaIds(
                    e.getVendas()
                            .stream()
                            .map(Venda::getId)
                            .collect(Collectors.toSet()));
        }

        return dto;
    }
}