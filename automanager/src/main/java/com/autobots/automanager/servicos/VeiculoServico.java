package com.autobots.automanager.servicos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.VeiculoDto;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@Service
public class VeiculoServico {

    @Autowired
    private RepositorioVeiculo repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<VeiculoDto> listarTodos() {
        return repositorio.findAll().stream()
                .map(this::paraDto)
                .collect(Collectors.toList());
    }

    public VeiculoDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado: " + id)));
    }

    public VeiculoDto cadastrar(Veiculo veiculo) {
        return paraDto(repositorio.save(veiculo));
    }

    public VeiculoDto atualizar(Long id, Veiculo dados) {
        Veiculo veiculo = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado: " + id));
        veiculo.setTipo(dados.getTipo());
        veiculo.setModelo(dados.getModelo());
        veiculo.setPlaca(dados.getPlaca());
        if (dados.getProprietario() != null) {
            Usuario proprietario = repositorioUsuario.findById(dados.getProprietario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            veiculo.setProprietario(proprietario);
        }
        return paraDto(repositorio.save(veiculo));
    }

    public void deletar(Long id) {
        repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado: " + id));
        repositorio.deleteById(id);
    }

    private VeiculoDto paraDto(Veiculo v) {
        VeiculoDto dto = new VeiculoDto();
        dto.setId(v.getId());
        dto.setTipo(v.getTipo());
        dto.setModelo(v.getModelo());
        dto.setPlaca(v.getPlaca());
        if (v.getProprietario() != null)
            dto.setProprietarioId(v.getProprietario().getId());
        return dto;
    }
}