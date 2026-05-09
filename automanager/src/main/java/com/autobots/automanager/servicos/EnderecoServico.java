package com.autobots.automanager.servicos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.EnderecoDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioEndereco;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class EnderecoServico {

    @Autowired
    private RepositorioEndereco repositorio;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public List<EnderecoDto> listarTodos() {
        return repositorio.findAll().stream().map(this::paraDto).collect(Collectors.toList());
    }

    public EnderecoDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço não encontrado: " + id)));
    }

    public EnderecoDto cadastrarParaUsuario(Long usuarioId, Endereco endereco) {
        Usuario usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + usuarioId));
        
        Endereco enderecoSalvo = repositorio.save(endereco);
        
        usuario.setEndereco(enderecoSalvo);
        repositorioUsuario.save(usuario);
        
        return paraDto(enderecoSalvo);
    }

    public EnderecoDto cadastrarParaEmpresa(Long empresaId, Endereco endereco) {
        Empresa empresa = repositorioEmpresa.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Empresa não encontrada: " + empresaId));
        
        Endereco enderecoSalvo = repositorio.save(endereco);
        
        empresa.setEndereco(enderecoSalvo);
        repositorioEmpresa.save(empresa);
        
        return paraDto(enderecoSalvo);
    }

    public EnderecoDto atualizar(Long id, Endereco dados) {
        Endereco endereco = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço não encontrado: " + id));
        endereco.setEstado(dados.getEstado());
        endereco.setCidade(dados.getCidade());
        endereco.setBairro(dados.getBairro());
        endereco.setRua(dados.getRua());
        endereco.setNumero(dados.getNumero());
        endereco.setCodigoPostal(dados.getCodigoPostal());
        endereco.setInformacoesAdicionais(dados.getInformacoesAdicionais());
        return paraDto(repositorio.save(endereco));
    }

    public void deletar(Long id) {
        repositorio.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço não encontrado: " + id));
        repositorio.deleteById(id);
    }

    private EnderecoDto paraDto(Endereco e) {
        EnderecoDto dto = new EnderecoDto();
        dto.setId(e.getId());
        dto.setEstado(e.getEstado());
        dto.setCidade(e.getCidade());
        dto.setBairro(e.getBairro());
        dto.setRua(e.getRua());
        dto.setNumero(e.getNumero());
        dto.setCodigoPostal(e.getCodigoPostal());
        dto.setInformacoesAdicionais(e.getInformacoesAdicionais());
        return dto;
    }
}