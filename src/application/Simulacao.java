package application;

import java.time.LocalDateTime;

import services.AVL;
import services.Cliente;

public class Simulacao {
	
	private static final String AVL_FILE = "arvore.bin"; 
	private static final String CACHE_FILE = "cache.bin"; 

    public static void main(String[] args) {
        // Inicializa a árvore AVL
        AVL arvore = new AVL();
        Cliente cliente = new Cliente(arvore);

        // Inicia o programa adicionando 60 ordens de serviço
        for (int i = 1; i <= 60; i++) {
            cliente.cadastrarOS(i, "OS" + i, "Descrição da OS" + i, LocalDateTime.now(), AVL_FILE);
        }

        // Realiza cinco consultas
        cliente.buscarPorCodigo(10, CACHE_FILE);
        cliente.buscarPorCodigo(20, CACHE_FILE);
        cliente.buscarPorCodigo(20, CACHE_FILE);
        cliente.buscarPorCodigo(30, CACHE_FILE);
        cliente.buscarPorCodigo(10, CACHE_FILE);

        // Realiza uma listagem
        cliente.listarOS();

        // Realiza um cadastro
        cliente.cadastrarOS(61, "OS61", "Descrição da OS61", LocalDateTime.now(), AVL_FILE);

        // Realiza uma listagem
        cliente.listarOS();

        // Realiza um segundo cadastro
        cliente.cadastrarOS(62, "OS62", "Descrição da OS62", LocalDateTime.now(), AVL_FILE);

        // Realiza uma listagem
        cliente.listarOS();

        // Realiza uma alteração
        cliente.alterarOS(61, "OS61 Alterada", "Descrição da OS61 Alterada", LocalDateTime.now(), AVL_FILE, CACHE_FILE);

        // Realiza uma listagem
        cliente.listarOS();

        // Realiza uma remoção
        cliente.removerOS(60, AVL_FILE, CACHE_FILE);

        // Realiza uma listagem
        cliente.listarOS();

        // Realiza uma segunda remoção
        cliente.removerOS(59, AVL_FILE, CACHE_FILE);

        // Realiza uma listagem final
        cliente.listarOS();
    }
}
