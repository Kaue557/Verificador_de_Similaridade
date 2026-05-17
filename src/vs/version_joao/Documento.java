package version_joao;

import java.nio.file.Path;
import version_joao.util.HashTable;
import version_joao.util.Util;

public class Documento {
    private String nome;
    private HashTable tabelaFrequencias;

public Documento(Path caminho, Util util, int tipoHash) throws Exception {
        this.nome = caminho.getFileName().toString();
        
        // Repasse o tipoHash na hora de dar o 'new' na tabela
        this.tabelaFrequencias = new HashTable(1021, tipoHash); 
        
        processar(caminho, util);
    }

    private void processar(Path caminho, Util util) throws Exception {
        String[] tokens = util.tokens(caminho);
        tokens = util.remover_stopwords(tokens);
        
        for (String token : tokens) {
            if (token != null && !token.trim().isEmpty()) {
                tabelaFrequencias.put(token);
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public HashTable getTabela() {
        return tabelaFrequencias;
    }
}