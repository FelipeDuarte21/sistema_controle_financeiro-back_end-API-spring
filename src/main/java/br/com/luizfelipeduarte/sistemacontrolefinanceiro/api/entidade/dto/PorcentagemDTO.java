package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

public class PorcentagemDTO {

    @NotNull(message = "rendaMensal é obrigatorio")
    private BigDecimal rendaMensal;

    @NotNull(message = "categorias são obrigatórias")
    private List<CategoriaPorcentagemDTO> categorias;

    @NotNull(message = "IdConta é obrigatório")
    private Long idConta;

    public PorcentagemDTO(BigDecimal rendaMensal, List<CategoriaPorcentagemDTO> categorias, Long idConta){
        this.rendaMensal = rendaMensal;
        this.categorias = categorias;
        this.idConta = idConta;
    }

    public List<CategoriaPorcentagemDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaPorcentagemDTO> categorias) {
        this.categorias = categorias;
    }

    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(BigDecimal rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

}
