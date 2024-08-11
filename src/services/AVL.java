package services;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entities.No;
import exceptions.ResourceAlreadyExistsException;
import exceptions.ResourceNotFoundException;

public class AVL implements Serializable {
	private static final long serialVersionUID = 1L;
	private No raiz = null;
	private final Cache cache = new Cache(20);
	private static final String LOG_FILE = "log_avl.txt";

	public void inserir(int codigo, String nome, String descricao, LocalDateTime horaSolicitacao, String caminhoArquivo) {
		carregarArvoreDeArquivo(caminhoArquivo);
		if (buscarNaArvore(raiz, codigo) != null) {
			System.out.println("Código " + codigo + " já existe na árvore. Inserção recusada.");
			registrarLog("Inserção recusada", false);
			throw new ResourceAlreadyExistsException("Código " + codigo + " já existe na árvore. Inserção recusada.");
		}
		boolean[] rotacao = new boolean[1];
		raiz = inserir(raiz, codigo, nome, descricao, horaSolicitacao, rotacao);
		salvarArvoreEmArquivo(caminhoArquivo);
		registrarLog("Inserção", rotacao[0]);
	}

	private No inserir(No arv, int codigo, String nome, String descricao, LocalDateTime horaSolicitacao, boolean[] rotacao) {
		if (arv == null)
			return new No(codigo, nome, descricao, horaSolicitacao);

		if (codigo < arv.getCodigo())
			arv.setEsq(inserir(arv.getEsq(), codigo, nome, descricao, horaSolicitacao, rotacao));
		else if (codigo > arv.getCodigo())
			arv.setDir(inserir(arv.getDir(), codigo, nome, descricao, horaSolicitacao, rotacao));
		else
			return arv;

		return rebalancear(arv, rotacao);
	}

	public void remover(int codigo, String caminhoArquivo, String nomeArquivoCache) {
		carregarArvoreDeArquivo(caminhoArquivo);
		cache.carregarCache(nomeArquivoCache); 

		No noExistente = buscarNaArvore(raiz, codigo);
		if (noExistente == null) {
			System.out.println("Código " + codigo + " não encontrado na árvore. Remoção ignorada.");
			registrarLog("Remoção Ignorada", false);
			throw new ResourceNotFoundException("Código " + codigo + " não encontrado na árvore. Remoção ignorada.");
		}

		boolean[] rotacao = new boolean[1];
		raiz = remover(raiz, codigo, rotacao);

		cache.removerDaCache(codigo);
		cache.salvarCache(nomeArquivoCache); 

		salvarArvoreEmArquivo(caminhoArquivo);
		registrarLog("Remoção", rotacao[0]);
	}

	private No remover(No arv, int codigo, boolean[] rotacao) {
		if (arv == null)
			return arv;

		if (codigo < arv.getCodigo()) {
			arv.setEsq(remover(arv.getEsq(), codigo, rotacao));
		} else if (codigo > arv.getCodigo()) {
			arv.setDir(remover(arv.getDir(), codigo, rotacao));
		} else {
			if (arv.getEsq() == null && arv.getDir() == null) {
				arv = null;
			} else if (arv.getEsq() == null) {
				arv = arv.getDir();
			} else if (arv.getDir() == null) {
				arv = arv.getEsq();
			} else {
				No temp = menorChave(arv.getDir());
				arv.setCodigo(temp.getCodigo());
				arv.setNome(temp.getNome());
				arv.setDescricao(temp.getDescricao());
				arv.setHoraSolicitacao(temp.getHoraSolicitacao());
				arv.setDir(remover(arv.getDir(), temp.getCodigo(), rotacao));
			}
		}

		if (arv == null)
			return arv;

		return rebalancear(arv, rotacao);
	}

	public void alterar(int codigo, String novoNome, String novaDescricao, LocalDateTime novaHoraSolicitacao,
			String caminhoArquivo, String nomeArquivoCache) {
		carregarArvoreDeArquivo(caminhoArquivo);
		cache.carregarCache(nomeArquivoCache);

		No noExistente = buscarNaArvore(raiz, codigo);
		if (noExistente == null) {
			System.out.println("Código " + codigo + " não encontrado na árvore. Alteração ignorada.");
			registrarLog("Alteração Ignorada", false);
			throw new ResourceNotFoundException("Código " + codigo + " não encontrado na árvore. Alteração ignorada.");
		}

		noExistente.setNome(novoNome);
		noExistente.setDescricao(novaDescricao);
		noExistente.setHoraSolicitacao(novaHoraSolicitacao);
		
		cache.alterarNaCache(codigo, novoNome, novaDescricao, novaHoraSolicitacao);

		salvarArvoreEmArquivo(caminhoArquivo);
		cache.salvarCache(nomeArquivoCache);

		registrarLog("Alteração", false);
	}

	private No rebalancear(No arv, boolean[] rotacao) {
	    if (arv == null)
	        return arv;

	    arv.setAlturaNo(1 + maior(altura(arv.getEsq()), altura(arv.getDir())));

	    int fb = obterFB(arv);

	    if (fb > 1 && obterFB(arv.getEsq()) >= 0) {
	        rotacao[0] = true;
	        arv = rotacaoDireitaSimples(arv);
	    } else if (fb < -1 && obterFB(arv.getDir()) <= 0) {
	        rotacao[0] = true;
	        arv = rotacaoEsquerdaSimples(arv);
	    } else if (fb > 1 && obterFB(arv.getEsq()) < 0) {
	        arv.setEsq(rotacaoEsquerdaSimples(arv.getEsq()));
	        rotacao[0] = true;
	        arv = rotacaoDireitaSimples(arv);
	    } else if (fb < -1 && obterFB(arv.getDir()) > 0) {
	        arv.setDir(rotacaoDireitaSimples(arv.getDir()));
	        rotacao[0] = true;
	        arv = rotacaoEsquerdaSimples(arv);
	    }

	    return arv;
	}


	private int altura(No arv) {
		if (arv == null)
			return -1;
		return arv.getAlturaNo();
	}

	private int maior(int a, int b) {
		return (a > b) ? a : b;
	}

	private int obterFB(No arv) {
		if (arv == null)
			return 0;
		return altura(arv.getEsq()) - altura(arv.getDir());
	}

	private No menorChave(No arv) {
		No temp = arv;
		while (temp.getEsq() != null)
			temp = temp.getEsq();
		return temp;
	}

	private No rotacaoDireitaSimples(No y) {
		No x = y.getEsq();
		No z = x.getDir();

		x.setDir(y);
		y.setEsq(z);

		y.setAlturaNo(maior(altura(y.getEsq()), altura(y.getDir())) + 1);
		x.setAlturaNo(maior(altura(x.getEsq()), altura(x.getDir())) + 1);

		return x;
	}

	private No rotacaoEsquerdaSimples(No x) {
		No y = x.getDir();
		No z = y.getEsq();

		y.setEsq(x);
		x.setDir(z);

		x.setAlturaNo(maior(altura(x.getEsq()), altura(x.getDir())) + 1);
		y.setAlturaNo(maior(altura(y.getEsq()), altura(y.getDir())) + 1);

		return y;
	}

	public int contarNos() {
		return contarNos(raiz);
	}

	private int contarNos(No arv) {
		if (arv == null)
			return 0;
		return 1 + contarNos(arv.getEsq()) + contarNos(arv.getDir());
	}

	public List<No> listar() {
		List<No> lista = new ArrayList<>();
		listar(raiz, lista);
		return lista;
	}

	private void listar(No arv, List<No> lista) {
		if (arv != null) {
			listar(arv.getEsq(), lista);
			lista.add(arv);
			listar(arv.getDir(), lista);
		}
	}

	private void registrarLog(String operacao, boolean rotacao) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
	        String logMessage = String.format(
	            "%s [Operação: %s] [Altura da árvore: %d] [%s] [Cache: %s]",
	            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
	            operacao,
	            altura(raiz),
	            rotacao ? "Houve rotação" : "Não houve rotação",
	            cache.toString()
	        );
	        writer.write(logMessage);
	        writer.newLine();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public No buscarNaCache(int codigo, String nomeArquivoCache) {
		carregarCache(nomeArquivoCache);
		Optional<No> noCache = cache.buscar(codigo);
		if (noCache.isPresent()) {
			System.out.println("HIT!");
			System.out.println(cache.toString());
			return noCache.get();
		} else {
			System.out.println("MISS!\nBuscando na base de dados...");
			No noArvore = buscarNaArvore(raiz, codigo);
			if (noArvore != null) {
				cache.adicionar(noArvore);
				salvarCache(nomeArquivoCache);
			} else {
				System.out.println("Nó não encontrado na base de dados.");
			}
			System.out.println(cache.toString());
			return noArvore;
		}
	}

	private No buscarNaArvore(No arv, int codigo) {
		if (arv == null || arv.getCodigo() == codigo) {
			return arv;
		}
		if (codigo < arv.getCodigo()) {
			return buscarNaArvore(arv.getEsq(), codigo);
		}
		return buscarNaArvore(arv.getDir(), codigo);
	}

	public void salvarCache(String nomeArquivo) {
		cache.salvarCache(nomeArquivo);
	}

	public void carregarCache(String nomeArquivo) {
		cache.carregarCache(nomeArquivo);
	}

	public void salvarArvoreEmArquivo(String caminhoArquivo) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoArquivo))) {
			oos.writeObject(raiz);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void carregarArvoreDeArquivo(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        
        if (!arquivo.exists()) {
            try {
                if (arquivo.createNewFile()) {
                } else {
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            return;
        }

        try (FileInputStream fis = new FileInputStream(arquivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            if (fis.available() > 0) {
                raiz = (No) ois.readObject();
            } else {
                raiz = null; 
            }
        } catch (EOFException e) {
            raiz = null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            raiz = null;
        }
    }

}
