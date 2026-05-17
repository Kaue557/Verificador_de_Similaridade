package version_joao;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        
        Path files = Path.of("./arquivos/teste.txt");
        Tokenizer token = new Tokenizer(files);
        String[] keys = token.tokens();
        for(int i = 0; i < keys.length; i++) {
            System.out.print(keys[i] + "; ");   
        }

    }
}
