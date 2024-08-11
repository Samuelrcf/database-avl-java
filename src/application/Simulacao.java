package application;

import java.time.LocalDateTime;

import services.AVL;
import services.Cliente;

public class Simulacao {
	
	private static final String AVL_FILE = "arvore.bin"; 
	private static final String CACHE_FILE = "cache.bin"; 

    public static void main(String[] args) {
        AVL arvore = new AVL();
        Cliente cliente = new Cliente(arvore);

        for (int i = 1; i <= 60; i++) {
            cliente.cadastrarOS(i, "OS" + i, "Descrição da OS" + i, LocalDateTime.now(), AVL_FILE);
        }
        
        System.out.println("Quantidade de registros: " + cliente.acessarQuantidadeRegistros());

        for (int i = 1; i <= 20; i++) {
            cliente.buscarPorCodigo(i, CACHE_FILE); 
        }
        
        cliente.buscarPorCodigo(1, CACHE_FILE);
        cliente.buscarPorCodigo(10, CACHE_FILE);
        cliente.buscarPorCodigo(30, CACHE_FILE);

        cliente.listarOS();

        cliente.cadastrarOS(61, "OS61", "Descrição da OS61", LocalDateTime.now(), AVL_FILE);

        cliente.listarOS();

        cliente.cadastrarOS(62, "OS62", "Descrição da OS62", LocalDateTime.now(), AVL_FILE);

        cliente.listarOS();

        cliente.alterarOS(61, "OS61 Alterada", "Descrição da OS61 Alterada", LocalDateTime.now(), AVL_FILE, CACHE_FILE);
        
        cliente.buscarPorCodigo(61, CACHE_FILE);

        cliente.listarOS();

        cliente.removerOS(60, AVL_FILE, CACHE_FILE);

        cliente.listarOS();

        cliente.removerOS(59, AVL_FILE, CACHE_FILE);

        cliente.listarOS();
        
        cliente.removerOS(3, AVL_FILE, CACHE_FILE);
        
        cliente.buscarPorCodigo(30, CACHE_FILE);
        
        System.out.println("Quantidade de registros: " + cliente.acessarQuantidadeRegistros());
        
        cliente.cadastrarOS(61, "OS61", "Descrição da OS61", LocalDateTime.now(), AVL_FILE); //61 já existe, exception ResourceAlreadyExistsException
        
        cliente.alterarOS(70, "OS70 Alterada", "Descrição da OS70 Alterada", LocalDateTime.now(), AVL_FILE, CACHE_FILE); //70 não existe, exception ResourceNotFoundException

        cliente.removerOS(70, AVL_FILE, CACHE_FILE); //70 não existe, exception ResourceNotFoundException


    }
}
