package entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrdemDeServico implements Serializable {
	private static final long serialVersionUID = 1L;
	private int codigo;
    private String nome;
    private String descricao;
    private LocalDateTime horaSolicitacao;
    
    public OrdemDeServico() {
    	
    }

    public OrdemDeServico(int codigo, String nome, String descricao, LocalDateTime horaSolicitacao) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.horaSolicitacao = horaSolicitacao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getHoraSolicitacao() {
        return horaSolicitacao;
    }

    @Override
    public String toString() {
        return "OrdemDeServico{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", horaSolicitacao=" + horaSolicitacao +
                '}';
    }
}
