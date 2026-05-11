package arvore_AVL;

import java.util.ArrayList;

public class No {

    // cada nó de similaridade pode carregar uma lista dos docs com tal similaridade
    private ArrayList<Resultado> resultados;
    private int altura;

    private double similaridade;
    private No esquerda;
    private No direita;

    public No(double similaridade) {
        this.similaridade = similaridade;
        this.resultados = new ArrayList<>();

        this.esquerda = null;
        this.direita = null;

        this.altura = 1;
    }

    // GETTER E SETTER PARA RESULTADO --------------------
    public ArrayList<> getResultados(){
        return resultados;
    }


    // GETTER E SETTER PARA SIMILARIDADE --------------------
    public double getSimilaridade() {
        return similaridade;
    }

    public void setSimilaridade(double similaridade) {
        this.similaridade = similaridade;
    }

    // GETTER E SETTER PARA ALTURA --------------------
    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    // GETTER E SETTER PARA FILHO ESQUERDO ------------
    public No getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(No esquerda) {
        this.esquerda = esquerda;
    }

    // GETTER E SETTER PARA FILHO DIREITO ------------
    public No getDireita() {
        return direita;
    }

    public void setDireita(No direita) {
        this.direita = direita;
    }
}