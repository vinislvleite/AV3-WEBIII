package com.autobots.automanager.servicos;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dtos.VendaDto;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@Service
public class VendaServico {

    @Autowired
    private RepositorioVenda repositorio;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioVeiculo repositorioVeiculo;
    @Autowired
    private RepositorioMercadoria repositorioMercadoria;
    @Autowired
    private RepositorioServico repositorioServico;

    public List<VendaDto> listarTodas() {
        return repositorio.findAll().stream().map(this::paraDto).collect(Collectors.toList());
    }

    public VendaDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada: " + id)));
    }

    public VendaDto cadastrar(Venda venda) {
    if (venda.getCliente() == null || venda.getCliente().getId() == null) {
        throw new IllegalArgumentException("ID do Cliente é obrigatório.");
    }
    if (venda.getVeiculo() == null || venda.getVeiculo().getId() == null) {
        throw new IllegalArgumentException("ID do Veículo é obrigatório.");
    }

    Usuario cliente = repositorioUsuario.findById(venda.getCliente().getId())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));
            
    Veiculo veiculo = repositorioVeiculo.findById(venda.getVeiculo().getId())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));

    if (veiculo.getProprietario() == null || !veiculo.getProprietario().getId().equals(cliente.getId())) {
        throw new IllegalArgumentException("O veículo informado não pertence a este cliente.");
    }

    if (venda.getFuncionario() != null && venda.getFuncionario().getId() != null) {
        Usuario func = repositorioUsuario.findById(venda.getFuncionario().getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Funcionário não encontrado"));
        venda.setFuncionario(func);
    }
    if (venda.getMercadorias() != null) {
        Set<Mercadoria> mercadoriasGerenciadas = venda.getMercadorias().stream()
            .map(m -> repositorioMercadoria.findById(m.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Mercadoria não encontrada: " + m.getId())))
            .collect(Collectors.toSet());
        venda.getMercadorias().clear();
        venda.getMercadorias().addAll(mercadoriasGerenciadas);
    }

    if (venda.getServicos() != null) {
        Set<Servico> servicosGerenciados = venda.getServicos().stream()
            .map(s -> repositorioServico.findById(s.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço não encontrado: " + s.getId())))
            .collect(Collectors.toSet());
        venda.getServicos().clear();
        venda.getServicos().addAll(servicosGerenciados);
    }

    venda.setCliente(cliente);
    venda.setVeiculo(veiculo);

    return paraDto(repositorio.save(venda));
}

    public VendaDto atualizar(Long id, Venda dados) {
        Venda venda = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada: " + id));
        
        venda.setIdentificacao(dados.getIdentificacao());
        venda.setCadastro(dados.getCadastro());

        if (dados.getCliente() != null && dados.getCliente().getId() != null) {
            Usuario cliente = repositorioUsuario.findById(dados.getCliente().getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));
            venda.setCliente(cliente);
        }

        if (dados.getFuncionario() != null && dados.getFuncionario().getId() != null) {
            Usuario funcionario = repositorioUsuario.findById(dados.getFuncionario().getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Funcionário não encontrado"));
            venda.setFuncionario(funcionario);
        }
        
        if (dados.getVeiculo() != null && dados.getVeiculo().getId() != null) {
            Veiculo veiculo = repositorioVeiculo.findById(dados.getVeiculo().getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));
            venda.setVeiculo(veiculo);
        }

        if (venda.getVeiculo() != null && venda.getCliente() != null) {
            if (!venda.getVeiculo().getProprietario().getId().equals(venda.getCliente().getId())) {
                throw new IllegalArgumentException("O veículo atualizado não pertence ao cliente da venda.");
            }
        }

        if (dados.getMercadorias() != null) {
            Set<Mercadoria> mercadorias = dados.getMercadorias().stream()
                .map(m -> repositorioMercadoria.findById(m.getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Mercadoria não encontrada: " + m.getId())))
                .collect(Collectors.toSet());
            venda.setMercadorias(mercadorias);
        }

        if (dados.getServicos() != null) {
            Set<Servico> servicos = dados.getServicos().stream()
                .map(s -> repositorioServico.findById(s.getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço não encontrado: " + s.getId())))
                .collect(Collectors.toSet());
            venda.setServicos(servicos);
        }

        return paraDto(repositorio.save(venda));
    }

    public void deletar(Long id) {
        repositorio.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada: " + id));
        repositorio.deleteById(id);
    }

    private VendaDto paraDto(Venda v) {
        VendaDto dto = new VendaDto();
        dto.setId(v.getId());
        dto.setIdentificacao(v.getIdentificacao());
        dto.setCadastro(v.getCadastro());
        
        if (v.getCliente() != null) dto.setClienteId(v.getCliente().getId());
        if (v.getVeiculo() != null) dto.setVeiculoId(v.getVeiculo().getId());
        
        if (v.getFuncionario() != null) dto.setFuncionarioId(v.getFuncionario().getId());
        
        if (v.getMercadorias() != null) {
            dto.setMercadoriaIds(v.getMercadorias().stream().map(Mercadoria::getId).collect(Collectors.toSet()));
        }
        if (v.getServicos() != null) {
            dto.setServicoIds(v.getServicos().stream().map(Servico::getId).collect(Collectors.toSet()));
        }
        
        return dto;
    }
}