package com.autobots.automanager.dtos;

import java.util.Date;
import com.autobots.automanager.enumeracoes.TipoDocumento;
import lombok.Data;

@Data
public class DocumentoDto {
    private Long id;
    private TipoDocumento tipo;
    private Date dataEmissao;
    private String numero;
}