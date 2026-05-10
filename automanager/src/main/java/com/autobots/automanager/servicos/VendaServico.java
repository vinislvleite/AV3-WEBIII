package com.autobots.automanager.servicos;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dtos.VendaDto;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.excecoes.VendaException;
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
        return repositorio.findAll()
                .stream()
                .map(this::paraDto)
                .collect(Collectors.toList());
    }

    public VendaDto buscarPorId(Long id) {
        Venda venda = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada: " + id));
        return paraDto(venda);
    }

    public VendaDto cadastrar(Venda venda) {

        if (venda.getIdentificacao() != null) {
            boolean existe = repositorio.findAll()
                    .stream()
                    .anyMatch(v -> v.getIdentificacao() != null &&
                            v.getIdentificacao().equalsIgnoreCase(venda.getIdentificacao()));

            if (existe) {
                throw new VendaException(venda.getIdentificacao());
            }
        }

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

        if (veiculo.getProprietario() == null ||
                !veiculo.getProprietario().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("O veículo não pertence ao cliente.");
        }

        if (venda.getFuncionario() != null && venda.getFuncionario().getId() != null) {
            Usuario funcionario = repositorioUsuario.findById(venda.getFuncionario().getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Funcionário não encontrado"));
            venda.setFuncionario(funcionario);
        }

        Set<Mercadoria> mercadorias = new HashSet<>();

        if (venda.getMercadorias() != null) {
            for (Mercadoria m : venda.getMercadorias()) {

                mercadorias.add(
                        repositorioMercadoria.findById(m.getId())
                                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                                        "Mercadoria não encontrada: " + m.getId()))
                );
            }
        }

        venda.setMercadorias(mercadorias);

        Set<Servico> servicos = new HashSet<>();

        if (venda.getServicos() != null) {
            for (Servico s : venda.getServicos()) {

                servicos.add(
                        repositorioServico.findById(s.getId())
                                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                                        "Serviço não encontrado: " + s.getId()))
                );
            }
        }

        venda.setServicos(servicos);

        venda.setCliente(cliente);
        venda.setVeiculo(veiculo);

        if (venda.getCadastro() == null) {
            venda.setCadastro(new Date());
        }

        try {

            return paraDto(repositorio.save(venda));

        } catch (DataIntegrityViolationException e) {

            throw new VendaException(
                    "Uma ou mais mercadorias/serviços já estão vinculados a outra venda.",
                    e
            );
        }
    }

    public VendaDto atualizar(Long id, Venda dados) {

        Venda venda = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada: " + id));

        if (dados.getIdentificacao() != null) {
            venda.setIdentificacao(dados.getIdentificacao());
        }

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
            if (!venda.getVeiculo().getProprietario().getId()
                    .equals(venda.getCliente().getId())) {
                throw new IllegalArgumentException("Veículo não pertence ao cliente.");
            }
        }

        Set<Mercadoria> mercadorias = new HashSet<>();
        if (dados.getMercadorias() != null) {
            for (Mercadoria m : dados.getMercadorias()) {
                mercadorias.add(repositorioMercadoria.findById(m.getId())
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Mercadoria não encontrada: " + m.getId())));
            }
        }
        venda.setMercadorias(mercadorias);

        Set<Servico> servicos = new HashSet<>();
        if (dados.getServicos() != null) {
            for (Servico s : dados.getServicos()) {
                servicos.add(repositorioServico.findById(s.getId())
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço não encontrado: " + s.getId())));
            }
        }
        venda.setServicos(servicos);

        try {

        return paraDto(repositorio.save(venda));

    } catch (DataIntegrityViolationException e) {

        throw new VendaException(
                "Uma ou mais mercadorias/serviços já estão vinculados a outra venda.",
                e
        );
    }
    }

    public void deletar(Long id) {
        repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada: " + id));
        repositorio.deleteById(id);
    }

    private VendaDto paraDto(Venda v) {

        VendaDto dto = new VendaDto();

        dto.setId(v.getId());
        dto.setIdentificacao(v.getIdentificacao());
        dto.setCadastro(v.getCadastro());

        if (v.getCliente() != null) dto.setClienteId(v.getCliente().getId());
        if (v.getFuncionario() != null) dto.setFuncionarioId(v.getFuncionario().getId());
        if (v.getVeiculo() != null) dto.setVeiculoId(v.getVeiculo().getId());

        if (v.getMercadorias() != null) {
            dto.setMercadoriaIds(v.getMercadorias().stream()
                    .map(Mercadoria::getId)
                    .collect(Collectors.toSet()));
        }

        if (v.getServicos() != null) {
            dto.setServicoIds(v.getServicos().stream()
                    .map(Servico::getId)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }
}