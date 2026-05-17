import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

class Tokenizer {

    // Dentro da classe Tokenizer, crie a lista estática:
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "o", "a", "os", "as", "um", "uma", "de", "do", "da", "em", "no", "na",
            "para", "por", "com", "e", "ou", "mas", "que", "e", "sao"
    )); // estão sem acento, pois a checagem ocorre pós-normalização

    // Caminho do arquivo a ser processado.
    //
    // O tipo Path representa caminhos no sistema de arquivos.
    //
    // Exemplos válidos:
    //   Path.of("C:/Users/Joao/documento.txt")
    //   Path.of("./arquivos/teste.txt")
    //   Path.of("../documentos/doc1.txt")
    //
    // IMPORTANTE:
    // O caminho deve apontar para um arquivo existente.
    Path caminho;

    public Tokenizer(Path p) {
        this.caminho = p;
    }

    // Lê o arquivo, normaliza o texto e retorna um vetor contendo
    // cada palavra (token) separadamente.
    //
    // Exemplo:
    //   Arquivo:
    //       "Aula do Takuno!"
    //
    //   Após normalização:
    //       "aula do takuno"
    //
    //   Retorno:
    //       ["aula", "do", "takuno"]
    //
    // Etapas da normalização:
    //
    // Leitura completa do arquivo em uma String.
    // Conversão para letras minúsculas.
    // Remoção de acentos:
    //      "computação" -> "computacao"
    // Remoção de pontuação e caracteres especiais.
    // Separação das palavras usando espaços em branco.
    //
    // OBSERVAÇÃO:
    // Embora todo o conteudo seja carregado em uma String, essa
    // abordagem é totalmente adequada para o escopo do projeto e
    // simplifica bastante o pré-processamento do texto.
    public String[] tokens() throws IOException {

        // Le todo o conteúdo do arquivo usando UTF-8.
        String conteudo = Files.readString(this.caminho);

        // Converte para minúsculas como:
        // "Java" != "java"
        conteudo = conteudo.toLowerCase();

        // tira caracteres acentuados:
        // "ã" -> "a" + marcador de acento
        conteudo = Normalizer.normalize(conteudo, Normalizer.Form.NFD);

        // Remove os marcadores de acentuação.
        // Exemplo:
        // "computação" -> "computacao"
        conteudo = conteudo.replaceAll("\\p{M}", "");

        // Remove tudo que não for:
        // - letras de a a z
        // - dígitos de 0 a 9
        // - espaços em branco
        //
        // Pontuações são substituídas por espaço para evitar que
        // palavras sejam unidas acidentalmente.
        conteudo = conteudo.replaceAll("[^a-z0-9\\s]", " ");

        // Divide o texto em tokens usando um ou mais espaços.
        String[] keys = conteudo.split("\\s+");

        // FILTRO DE STOP WORDS:
        List<String> tokensFiltrados = new ArrayList<>();
        for (String palavra : keys) {
            if (!palavra.isEmpty() && !STOP_WORDS.contains(palavra)) {
                tokensFiltrados.add(palavra);
            }
        }

        // Retorna o vetor de palavras APÓS o filtro
        return tokensFiltrados.toArray(new String[tokensFiltrados.size()]);
    }
}