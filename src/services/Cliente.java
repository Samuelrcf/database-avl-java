package services;

import java.time.LocalDateTime;
import java.util.List;

import entities.No;

public class Cliente {

	private AVL arvore;

	public Cliente(AVL arvore) {
		this.arvore = arvore;
	}

	public void cadastrarOS(int codigo, String nome, String descricao, LocalDateTime horaSolicitacao,
			String caminhoDoArquivo) {
		System.out.println("Cadastrando codigo " + codigo + "...");
		arvore.inserir(codigo, nome, descricao, horaSolicitacao, caminhoDoArquivo);
	}

	public void listarOS() {
		System.out.println("Listando todos os códigos da base de dados...");
		List<No> ordensDeServico = arvore.listar();
		for (No no : ordensDeServico) {
			System.out.println("Código: " + no.getCodigo() + ", Nome: " + no.getNome() + ", Descrição: "
					+ no.getDescricao() + ", Hora da Solicitação: " + no.getHoraSolicitacao());
		}
	}

	public void alterarOS(int codigo, String novoNome, String novaDescricao, LocalDateTime novaHoraSolicitacao,
			String caminhoDoArquivo, String nomeArquivoCache) {
		System.out.println("Alterando codigo " + codigo + "...");
		arvore.alterar(codigo, novoNome, novaDescricao, novaHoraSolicitacao, caminhoDoArquivo, nomeArquivoCache);
	}

	public void removerOS(int codigo, String caminhoDoArquivo, String nomeArquivoCache) {
		System.out.println("Removendo codigo " + codigo + " ...");
		arvore.remover(codigo, caminhoDoArquivo, nomeArquivoCache);
	}

	public int acessarQuantidadeRegistros() {
		return arvore.contarNos();
	}

	public No buscarPorCodigo(int codigo, String nomeArquivoCache) {
		System.out.println("Buscando " + codigo + " na cache...");
		No no = arvore.buscarNaCache(codigo, nomeArquivoCache);
		if (no != null) {
			System.out.println("Código: " + no.getCodigo() + ", Nome: " + no.getNome() + ", Descrição: "
					+ no.getDescricao() + ", Hora da Solicitação: " + no.getHoraSolicitacao());
		}
		return no;
	}
}
