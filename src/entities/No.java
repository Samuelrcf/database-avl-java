package entities;

public class No {

    private int codigo;          
    private String nome;        
    private String descricao;    
    private String horaSolicitacao; 
    private int alturaNo;
    private No esq;
    private No dir;

    public No(int codigo, String nome, String descricao, String horaSolicitacao) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.horaSolicitacao = horaSolicitacao;
        this.alturaNo = 0;
        this.esq = null;
        this.dir = null;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHoraSolicitacao() {
        return horaSolicitacao;
    }

    public void setHoraSolicitacao(String horaSolicitacao) {
        this.horaSolicitacao = horaSolicitacao;
    }

    public int getAlturaNo() {
        return alturaNo;
    }

    public void setAlturaNo(int alturaNo) {
        this.alturaNo = alturaNo;
    }

    public No getEsq() {
        return esq;
    }

    public void setEsq(No esq) {
        this.esq = esq;
    }

    public No getDir() {
        return dir;
    }

    public void setDir(No dir) {
        this.dir = dir;
    }
}
