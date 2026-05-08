package com.autobots.automanager.servicos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.ServicoDto;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

@Service
public class ServicoService {

    @Autowired
    private RepositorioServico repositorio;

    public List<ServicoDto> listarTodos() {
        return repositorio.findAll().stream()
                .map(this::paraDto)
                .collect(Collectors.toList());
    }

    public ServicoDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado: " + id)));
    }

    public ServicoDto cadastrar(Servico servico) {
        return paraDto(repositorio.save(servico));
    }

    public ServicoDto atualizar(Long id, Servico dados) {
        Servico servico = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado: " + id));
        servico.setNome(dados.getNome());
        servico.setDescricao(dados.getDescricao());
        servico.setValor(dados.getValor());
        return paraDto(repositorio.save(servico));
    }

    public void deletar(Long id) {
        repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado: " + id));
        repositorio.deleteById(id);
    }

    private ServicoDto paraDto(Servico s) {
        ServicoDto dto = new ServicoDto();
        dto.setId(s.getId());
        dto.setNome(s.getNome());
        dto.setDescricao(s.getDescricao());
        dto.setValor(s.getValor());
        return dto;
    }
}