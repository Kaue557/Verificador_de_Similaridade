package arvore_AVL;

public class No {

    private int valor;
    private No esquerda;
    private No direita;

    public No(int valor) {
        this.valor = valor;
        this.esquerda = null;
        this.direita = null;
    }

    // GETTER E SETTER PARA VALOR --------------------
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
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