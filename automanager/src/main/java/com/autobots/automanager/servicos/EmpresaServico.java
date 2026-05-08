package com.autobots.automanager.servicos;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.dtos.EmpresaDto;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@Service
public class EmpresaServico {

    @Autowired
    private RepositorioEmpresa repositorio;

    public List<EmpresaDto> listarTodas() {
        return repositorio.findAll().stream()
                .map(this::paraDto)
                .collect(Collectors.toList());
    }

    public EmpresaDto buscarPorId(Long id) {
        Empresa empresa = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + id));
        return paraDto(empresa);
    }

    public EmpresaDto cadastrar(Empresa empresa) {
        return paraDto(repositorio.save(empresa));
    }

    public EmpresaDto atualizar(Long id, Empresa dados) {
        Empresa empresa = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + id));
        empresa.setRazaoSocial(dados.getRazaoSocial());
        empresa.setNomeFantasia(dados.getNomeFantasia());
        empresa.setTelefones(dados.getTelefones());
        empresa.setEndereco(dados.getEndereco());
        return paraDto(repositorio.save(empresa));
    }

    public void deletar(Long id) {
        repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + id));
        repositorio.deleteById(id);
    }

    private EmpresaDto paraDto(Empresa e) {
        EmpresaDto dto = new EmpresaDto();
        dto.setId(e.getId());
        dto.setRazaoSocial(e.getRazaoSocial());
        dto.setNomeFantasia(e.getNomeFantasia());
        dto.setCadastro(e.getCadastro());
        if (e.getUsuarios() != null)
            dto.setUsuarioIds(e.getUsuarios().stream().map(u -> u.getId()).collect(Collectors.toSet()));
        if (e.getMercadorias() != null)
            dto.setMercadoriaIds(e.getMercadorias().stream().map(m -> m.getId()).collect(Collectors.toSet()));
        if (e.getServicos() != null)
            dto.setServicoIds(e.getServicos().stream().map(s -> s.getId()).collect(Collectors.toSet()));
        if (e.getVendas() != null)
            dto.setVendaIds(e.getVendas().stream().map(v -> v.getId()).collect(Collectors.toSet()));
        return dto;
    }
}