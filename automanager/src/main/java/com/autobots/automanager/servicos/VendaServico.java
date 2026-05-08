package com.autobots.automanager.servicos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.VendaDto;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
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

    public List<VendaDto> listarTodas() {
        return repositorio.findAll().stream()
                .map(this::paraDto)
                .collect(Collectors.toList());
    }

    public VendaDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada: " + id)));
    }

    public VendaDto cadastrar(Venda venda) {
        return paraDto(repositorio.save(venda));
    }

    public VendaDto atualizar(Long id, Venda dados) {
        Venda venda = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada: " + id));
        venda.setIdentificacao(dados.getIdentificacao());
        venda.setCadastro(dados.getCadastro());

        if (dados.getCliente() != null) {
            Usuario cliente = repositorioUsuario.findById(dados.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            venda.setCliente(cliente);
        }
        if (dados.getFuncionario() != null) {
            Usuario funcionario = repositorioUsuario.findById(dados.getFuncionario().getId())
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
            venda.setFuncionario(funcionario);
        }
        if (dados.getVeiculo() != null) {
            Veiculo veiculo = repositorioVeiculo.findById(dados.getVeiculo().getId())
                    .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            venda.setVeiculo(veiculo);
        }
        if (dados.getMercadorias() != null)
            venda.setMercadorias(dados.getMercadorias());
        if (dados.getServicos() != null)
            venda.setServicos(dados.getServicos());

        return paraDto(repositorio.save(venda));
    }

    public void deletar(Long id) {
        repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada: " + id));
        repositorio.deleteById(id);
    }

    private VendaDto paraDto(Venda v) {
        VendaDto dto = new VendaDto();
        dto.setId(v.getId());
        dto.setIdentificacao(v.getIdentificacao());
        dto.setCadastro(v.getCadastro());
        if (v.getCliente() != null)
            dto.setClienteId(v.getCliente().getId());
        if (v.getFuncionario() != null)
            dto.setFuncionarioId(v.getFuncionario().getId());
        if (v.getVeiculo() != null)
            dto.setVeiculoId(v.getVeiculo().getId());
        if (v.getMercadorias() != null)
            dto.setMercadoriaIds(v.getMercadorias().stream().map(Mercadoria::getId).collect(Collectors.toSet()));
        if (v.getServicos() != null)
            dto.setServicoIds(v.getServicos().stream().map(Servico::getId).collect(Collectors.toSet()));
        return dto;
    }
}