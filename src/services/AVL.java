package services;

import entities.No;
import java.util.ArrayList;
import java.util.List;

public class AVL {

    private No raiz = null;

    public void inserir(int codigo, String nome, String descricao, String horaSolicitacao) {
        raiz = inserir(raiz, codigo, nome, descricao, horaSolicitacao);
    }

    private No inserir(No arv, int codigo, String nome, String descricao, String horaSolicitacao) {
        if (arv == null)
            return new No(codigo, nome, descricao, horaSolicitacao);

        if (codigo < arv.getCodigo())
            arv.setEsq(inserir(arv.getEsq(), codigo, nome, descricao, horaSolicitacao));
        else if (codigo > arv.getCodigo())
            arv.setDir(inserir(arv.getDir(), codigo, nome, descricao, horaSolicitacao));
        else
            return arv;

        arv.setAlturaNo(1 + maior(altura(arv.getEsq()), altura(arv.getDir())));

        int fb = obterFB(arv);

        if (fb > 1 && codigo < arv.getEsq().getCodigo())
            return rotacaoDireitaSimples(arv);

        if (fb < -1 && codigo > arv.getDir().getCodigo())
            return rotacaoEsquerdaSimples(arv);

        if (fb > 1 && codigo > arv.getEsq().getCodigo()) {
            arv.setEsq(rotacaoEsquerdaSimples(arv.getEsq()));
            return rotacaoDireitaSimples(arv);
        }

        if (fb < -1 && codigo < arv.getDir().getCodigo()) {
            arv.setDir(rotacaoDireitaSimples(arv.getDir()));
            return rotacaoEsquerdaSimples(arv);
        }

        return arv;
    }

    public void remover(int codigo) {
        raiz = remover(raiz, codigo);
    }

    private No remover(No arv, int codigo) {
        if (arv == null)
            return arv;

        if (codigo < arv.getCodigo())
            arv.setEsq(remover(arv.getEsq(), codigo));
        else if (codigo > arv.getCodigo())
            arv.setDir(remover(arv.getDir(), codigo));
        else {
            if (arv.getEsq() == null && arv.getDir() == null) {
                arv = null;
            } else if (arv.getEsq() == null) {
                No temp = arv;
                arv = temp.getDir();
                temp = null;
            } else if (arv.getDir() == null) {
                No temp = arv;
                arv = temp.getEsq();
                temp = null;
            } else {
                No temp = menorChave(arv.getDir());
                arv.setCodigo(temp.getCodigo());
                arv.setNome(temp.getNome());
                arv.setDescricao(temp.getDescricao());
                arv.setHoraSolicitacao(temp.getHoraSolicitacao());
                arv.setDir(remover(arv.getDir(), temp.getCodigo()));
            }
        }

        if (arv == null)
            return arv;

        arv.setAlturaNo(1 + maior(altura(arv.getEsq()), altura(arv.getDir())));

        int fb = obterFB(arv);

        if (fb > 1 && obterFB(arv.getEsq()) >= 0)
            return rotacaoDireitaSimples(arv);

        if (fb < -1 && obterFB(arv.getDir()) <= 0)
            return rotacaoEsquerdaSimples(arv);

        if (fb > 1 && obterFB(arv.getEsq()) < 0) {
            arv.setEsq(rotacaoEsquerdaSimples(arv.getEsq()));
            return rotacaoDireitaSimples(arv);
        }

        if (fb < -1 && obterFB(arv.getDir()) > 0) {
            arv.setDir(rotacaoDireitaSimples(arv.getDir()));
            return rotacaoEsquerdaSimples(arv);
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
}
