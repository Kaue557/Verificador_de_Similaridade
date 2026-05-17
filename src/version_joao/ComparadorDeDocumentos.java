package version_joao; // ou o pacote adequado do seu projeto

public class ComparadorDeDocumentos {

    /**
     * Calcula a Similaridade de Cosseno entre dois documentos.
     * Leva em consideração a frequência das palavras (TF).
     */
    public static double calcularCosseno(Documento doc1, Documento doc2) {
        double produtoEscalar = 0.0;
        double normaDoc1 = 0.0;
        double normaDoc2 = 0.0;

        // 1. Calcula o Produto Escalar e a Norma do Documento 1
        // Assume que doc1.getPalavras() retorna um array/lista das chaves da sua HashTable
        for (String palavra : doc1.getPalavras()) {
            int freq1 = doc1.getFrequencia(palavra);
            int freq2 = doc2.getFrequencia(palavra); // Deve retornar 0 se a palavra não existir no doc2

            produtoEscalar += (freq1 * freq2);
            normaDoc1 += Math.pow(freq1, 2);
        }

        // 2. Calcula a Norma do Documento 2
        for (String palavra : doc2.getPalavras()) {
            int freq2 = doc2.getFrequencia(palavra);
            normaDoc2 += Math.pow(freq2, 2);
        }

        // Evita divisão por zero caso um documento esteja vazio
        if (normaDoc1 == 0.0 || normaDoc2 == 0.0) {
            return 0.0;
        }

        // Fórmula do Cosseno: (A . B) / (||A|| * ||B||)
        return produtoEscalar / (Math.sqrt(normaDoc1) * Math.sqrt(normaDoc2));
    }

    /**
     * Calcula o Índice de Jaccard entre dois documentos.
     * Mede a sobreposição de conjuntos: (Interseção / União)
     */
    public static double calcularJaccard(Documento doc1, Documento doc2) {
        int intersecao = 0;
        int uniao = 0;

        // Conta a interseção (palavras que aparecem em ambos)
        for (String palavra : doc1.getPalavras()) {
            if (doc2.getFrequencia(palavra) > 0) {
                intersecao++;
            }
        }

        // A união é o total de palavras únicas no doc1 + total no doc2 - a interseção
        int totalPalavrasDoc1 = doc1.getQuantidadePalavrasUnicas();
        int totalPalavrasDoc2 = doc2.getQuantidadePalavrasUnicas();
        uniao = (totalPalavrasDoc1 + totalPalavrasDoc2) - intersecao;

        if (uniao == 0) return 0.0;

        return (double) intersecao / uniao;
    }
}