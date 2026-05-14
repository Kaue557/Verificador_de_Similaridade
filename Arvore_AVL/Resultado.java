package arvore_AVL;

public class Resultado {
    private String doc1;
    private String doc2;
    private double similaridade;

    // CONSTRUTOR
    public Resultado(String doc1, String doc2, double similaridade) {
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.similaridade = similaridade;
    }

    // GETTERS E SETTERS
    public String getDoc1() {
        return doc1;
    }

    public void setDoc1(String doc1) {
        this.doc1 = doc1;
    }

    public String getDoc2() {
        return doc2;
    }

    public void setDoc2(String doc2) {
        this.doc2 = doc2;
    }

    public double getSimilaridade() {
        return similaridade;
    }

    public void setSimilaridade(double similaridade) {
        this.similaridade = similaridade;
    }

    // toString para FORMATAÇÃO DA SAÍDA
    @Override
    public String toString() {
        // Formata a string para o padrão: doc1.txt <-> doc2.txt = 0.82
        return String.format("%s <-> %s = %.2f", doc1, doc2, similaridade);
    }
}