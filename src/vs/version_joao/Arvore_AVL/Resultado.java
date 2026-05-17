package version_joao.Arvore_AVL;

public class Resultado {
    private String doc1;
    private String doc2;
    private double similaridade;

    public Resultado(String doc1, String doc2, double similaridade) {
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.similaridade = similaridade;
    }

    public String getDoc1() { return doc1; }
    public String getDoc2() { return doc2; }
    public double getSimilaridade() { return similaridade; }

    @Override
    public String toString() {
        return doc1 + " <-> " + doc2 + " = " + String.format("%.2f", similaridade);
    }
}