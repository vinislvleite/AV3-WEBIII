package com.autobots.automanager.servicos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.MercadoriaDto;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@Service
public class MercadoriaServico {

    @Autowired
    private RepositorioMercadoria repositorio;

    public List<MercadoriaDto> listarTodas() {
        return repositorio.findAll().stream().map(this::paraDto).collect(Collectors.toList());
    }

    public MercadoriaDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Mercadoria não encontrada: " + id)));
    }

    public MercadoriaDto cadastrar(Mercadoria mercadoria) {
    mercadoria.setCadastro(new Date()); 
    return paraDto(repositorio.save(mercadoria));
}

    public MercadoriaDto atualizar(Long id, Mercadoria dados) {
        Mercadoria mercadoria = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Mercadoria não encontrada: " + id));
        mercadoria.setNome(dados.getNome());
        mercadoria.setDescricao(dados.getDescricao());
        mercadoria.setValor(dados.getValor());
        mercadoria.setQuantidade(dados.getQuantidade());
        mercadoria.setValidade(dados.getValidade());
        mercadoria.setFabricao(dados.getFabricao());
        return paraDto(repositorio.save(mercadoria));
    }

    public void deletar(Long id) {
        repositorio.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Mercadoria não encontrada: " + id));
        repositorio.deleteById(id);
    }

    private MercadoriaDto paraDto(Mercadoria m) {
        MercadoriaDto dto = new MercadoriaDto();
        dto.setId(m.getId());
        dto.setNome(m.getNome());
        dto.setDescricao(m.getDescricao());
        dto.setValor(m.getValor());
        dto.setQuantidade(m.getQuantidade());
        dto.setValidade(m.getValidade());
        dto.setFabricao(m.getFabricao());
        dto.setCadastro(m.getCadastro());
        return dto;
    }
}