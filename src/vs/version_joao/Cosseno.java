package version_joao;
import version_joao.Arvore_AVL.Resultado;
import version_joao.util.HashTable;
public class Cosseno {

    public static Resultado comparar(Documento d1, Documento d2) {
        HashTable t1 = d1.getTabela();
        HashTable t2 = d2.getTabela();

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        HashTable.Data[] table1 = t1.getTable();
        for (HashTable.Data head : table1) {
            HashTable.Data atual = head;
            while (atual != null) {
                double freqA = atual.valor;
                normA += freqA * freqA;

                HashTable.Data noB = t2.search(atual.key);
                if (noB != null) {
                    double freqB = noB.valor;
                    dotProduct += freqA * freqB;
                }
                atual = atual.prox;
            }
        }

        HashTable.Data[] table2 = t2.getTable();
        for (HashTable.Data head : table2) {
            HashTable.Data atual = head;
            while (atual != null) {
                double freqB = atual.valor;
                normB += freqB * freqB;
                atual = atual.prox;
            }
        }

        double similaridade = 0.0;
        if (normA != 0 && normB != 0) {
            similaridade = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        }

        return new Resultado(d1.getNome(), d2.getNome(), similaridade);
    }
}

