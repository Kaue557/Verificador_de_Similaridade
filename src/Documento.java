import java.io.IOException;
import java.nio.file.Path;

public class Documento {

    private String nomeArquivo;
    private HashTable tabelaHash;

    // Construtor: recebe o Path (caminho) do arquivo que será lido
    public Documento(Path caminhoArquivo) {
        // Pega apenas o nome do arquivo (ex: "doc1.txt") para exibir nos resultados
        this.nomeArquivo = caminhoArquivo.getFileName().toString();

        // Inicializa a tabela hash com um tamanho primo bom (ex: 1009) para diminuir colisões
        this.tabelaHash = new HashTable(1009);

        // Inicia o processamento automaticamente na criação do objeto
        processarArquivo(caminhoArquivo);
    }

    // Metodo privado que orquestra a leitura e armazenamento
    private void processarArquivo(Path caminhoArquivo) {
        try {
            // 1. Instancia o seu Tokenizer recém-corrigido
            Tokenizer tokenizer = new Tokenizer(caminhoArquivo);

            // 2. Pega o array de palavras já limpas e sem stop-words
            String[] palavrasLimpas = tokenizer.tokens();

            // 3. Joga todas as palavras na sua HashTable
            for (String palavra : palavrasLimpas) {
                // Se a palavra for repetida, sua HashTable já sabe que deve somar +1 na frequência
                tabelaHash.inserir(palavra);
            }

        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo: " + nomeArquivo);
            e.printStackTrace();
        }
    }

    // *******************************************************************
    // GETTERS PARA O COMPARADOR DE DOCUMENTOS
    // *******************************************************************

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    // Retorna o vetor com todas as chaves (palavras únicas) desse documento
    public String[] getPalavras() {
        return tabelaHash.obterTodasAsChaves();
    }

    // Busca a frequência de uma palavra específica
    public int getFrequencia(String palavra) {
        return tabelaHash.buscarFrequencia(palavra);
    }

    // Retorna o tamanho da tabela (total de palavras únicas)
    public int getQuantidadePalavrasUnicas() {
        return tabelaHash.getTamanho();
    }
}