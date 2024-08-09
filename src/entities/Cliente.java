package entities;

import services.AVL;

import java.util.List;

public class Cliente {

    private AVL arvore;

    public Cliente(AVL arvore) {
        this.arvore = arvore;
    }

    public void cadastrarOS(int codigo, String nome, String descricao, String horaSolicitacao) {
        arvore.inserir(codigo, nome, descricao, horaSolicitacao);
    }

    public void listarOS() {
        List<No> ordensDeServico = arvore.listar();
        for (No no : ordensDeServico) {
            System.out.println("Código: " + no.getCodigo() + ", Nome: " + no.getNome() + 
                ", Descrição: " + no.getDescricao() + ", Hora da Solicitação: " + no.getHoraSolicitacao());
        }
    }

    public void alterarOS(int codigo, String novoNome, String novaDescricao, String novaHoraSolicitacao) {
        arvore.remover(codigo);
        arvore.inserir(codigo, novoNome, novaDescricao, novaHoraSolicitacao);
    }

    public void removerOS(int codigo) {
        arvore.remover(codigo);
    }

    public int acessarQuantidadeRegistros() {
        return arvore.contarNos();
    }
}
