package version_joao.util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;

public class Util {

    public String normalizar(String palavra){

                // Converte para minúsculas como:
        // "Java" != "java"
        palavra = palavra.toLowerCase();

        // tira caracteres acentuados:
        // "ã" -> "a" + marcador de acento
        palavra = Normalizer.normalize(palavra, Normalizer.Form.NFD);

        // Remove os marcadores de acentuação.
        // Exemplo:
        // "computação" -> "computacao"
        palavra = palavra.replaceAll("\\p{M}", "");

        // Remove tudo que não for:
        // - letras de a a z
        // - dígitos de 0 a 9
        // - espaços em branco
        //
        // Pontuações são substituídas por espaço para evitar que
        // palavras sejam unidas acidentalmente.
        palavra = palavra.replaceAll("[^a-z0-9\\s]", " ");



        return palavra;
    }


        public String[] tokens(Path caminho) throws IOException {

        // Lê todo o conteúdo do arquivo usando UTF-8.
        String conteudo = Files.readString(caminho);
        
        // Divide o texto em tokens usando um ou mais espaços.
        //
        // Exemplos de separadores:
        // " "
        // "   "
        // "\\n"
        // "\\t"
        conteudo = normalizar(conteudo);

        String[] keys = conteudo.split("\\s+");
        

        // Retorna o vetor de palavras normalizadas.
        return keys;
    }

    public String[] remover_stopwords(String[] palavras){
        
    String[] stopWordsPadrao = {
        "a", "o", "e", "é", "de", "do", "da", "para", "com", "que", "em", 
        "no", "na", "um", "uma", "os", "as", "dos", "das", "por", "se", 
        "como", "mais", "mas", "ao", "aos", "ou", "sua", "seu", "suas", 
        "seus", "nao", "não", "como", "pelo", "pela", "nos", "nas", "qual"
    };

    HashTable hashStopWords = new HashTable(71);
        
        for(int i = 0; i < stopWordsPadrao.length; i++){
            hashStopWords.put(stopWordsPadrao[i]);
        }

        int validas = 0;
        for(int i = 0; i < palavras.length; i++){

            if (palavras[i] != null && !palavras[i].isBlank()){
                if(hashStopWords.search(palavras[i]) == null){
                    validas++;
                }
            }
        }

        if(validas == 0){
            return palavras;
        }

        int indice = 0;

        String[] filtradas = new String[validas];
        for(int i = 0; i < palavras.length; i++){
            if (palavras[i] != null && !palavras[i].isBlank()){
                if(hashStopWords.search(palavras[i]) == null){
                   filtradas[indice] = palavras[i];
                    indice++;
                }

        }
    }

        return filtradas;
    }

}
