package com.autobots.automanager.servicos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.DocumentoDto;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.excecoes.EntidadeNaoEncontradaException;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class DocumentoServico {

    @Autowired
    private RepositorioDocumento repositorio;
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<DocumentoDto> listarTodos() {
        return repositorio.findAll().stream().map(this::paraDto).collect(Collectors.toList());
    }

    public DocumentoDto buscarPorId(Long id) {
        return paraDto(repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Documento não encontrado: " + id)));
    }

    public DocumentoDto cadastrar(Long usuarioId, Documento documento) {
    Usuario usuario = repositorioUsuario.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
    usuario.getDocumentos().add(documento);
    Documento documentoGuardado = repositorio.save(documento);
    
    return paraDto(documentoGuardado);
    }

    public DocumentoDto atualizar(Long id, Documento dados) {
        Documento documento = repositorio.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Documento não encontrado: " + id));
        documento.setTipo(dados.getTipo());
        documento.setNumero(dados.getNumero());
        documento.setDataEmissao(dados.getDataEmissao());
        return paraDto(repositorio.save(documento));
    }

    public void deletar(Long id) {
        repositorio.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Documento não encontrado: " + id));
        repositorio.deleteById(id);
    }

    private DocumentoDto paraDto(Documento d) {
        DocumentoDto dto = new DocumentoDto();
        dto.setId(d.getId());
        dto.setTipo(d.getTipo());
        dto.setNumero(d.getNumero());
        dto.setDataEmissao(d.getDataEmissao());
        return dto;
    }
}