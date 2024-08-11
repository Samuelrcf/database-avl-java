package services;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import entities.No;

public class Cache implements Serializable {
	private static final long serialVersionUID = 1L;
	private No[] cache;
	private int currentIndex;

	public Cache(int tamanho) {
		this.cache = new No[tamanho];
		this.currentIndex = 0;
	}

	public void adicionar(No no) {
	    if (currentIndex < cache.length) {
	        cache[currentIndex] = no;
	        currentIndex++;
	    } else {
	        System.arraycopy(cache, 1, cache, 0, cache.length - 1);
	        cache[cache.length - 1] = no;
	    }
	}

	public void alterarNaCache(int codigo, String novoNome, String novaDescricao, LocalDateTime novaHoraSolicitacao) {
		for (int i = 0; i < currentIndex; i++) {
			if (cache[i] != null && cache[i].getCodigo() == codigo) {
				cache[i].setNome(novoNome);
				cache[i].setDescricao(novaDescricao);
				cache[i].setHoraSolicitacao(novaHoraSolicitacao);
				return;
			}
		}
	}

	public void removerDaCache(int codigo) {
		for (int i = 0; i < currentIndex; i++) {
			if (cache[i] != null && cache[i].getCodigo() == codigo) {
				System.arraycopy(cache, i + 1, cache, i, currentIndex - i - 1);
				currentIndex--;
				cache[currentIndex] = null; // limpar último elemento
				return;
			}
		}
	}

	public Optional<No> buscar(int codigo) {
		for (int i = 0; i < currentIndex; i++) {
			if (cache[i] != null && cache[i].getCodigo() == codigo) {
				return Optional.of(cache[i]);
			}
		}
		return Optional.empty();
	}

	public void salvarCache(String nomeArquivo) {
		try (FileOutputStream fos = new FileOutputStream(nomeArquivo);
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(cache);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void carregarCache(String nomeArquivo) {
	    File arquivoCache = new File(nomeArquivo);

	    if (!arquivoCache.exists()) {
	        try {
	            if (arquivoCache.createNewFile()) {
	            } else {
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return;
	        }
	        cache = new No[20];
	        currentIndex = 0;
	    } else {
	        try (FileInputStream fis = new FileInputStream(arquivoCache);
	             ObjectInputStream ois = new ObjectInputStream(fis)) {
	            if (fis.available() > 0) {
	                No[] loadedCache = (No[]) ois.readObject();
	                cache = new No[loadedCache.length];
	                System.arraycopy(loadedCache, 0, cache, 0, loadedCache.length);
	                currentIndex = 0;
	                for (No no : cache) {
	                    if (no != null) {
	                        currentIndex++;
	                    } else {
	                        break;
	                    }
	                }
	            } else {
	                cache = new No[20];
	                currentIndex = 0;
	            }
	        } catch (EOFException e) {
	            cache = new No[20];
	            currentIndex = 0;
	        } catch (IOException | ClassNotFoundException e) {
	            e.printStackTrace();
	            cache = new No[20];
	            currentIndex = 0;
	        }
	    }
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		for (int i = 0; i < currentIndex; i++) {
			if (cache[i] != null) {
				sb.append(cache[i].getCodigo());
				if (i < currentIndex - 1) {
					sb.append(", ");
				}
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
