package version_joao;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import version_joao.util.Util;

class Main{
    public static void main(String[] args) {

    Util doc = new Util();
    Path p = Path.of("./arquivos/teste.txt");

    try{
        String conteudo = Files.readString(p);
        String[] s = doc.tokens(p);
        System.out.println("Arquivo original:\n");

        System.out.println(conteudo);

        for(int i = 0; i < s.length; i++){
            s[i] = doc.normalizar(s[i]);

        } 
        s = doc.remover_stopwords(s);
        
        System.out.println("Arquivo modificado:\n");

        for (int i = 0; i < s.length; i++) {
            System.out.print(s[i] + " ");
        }

    }catch(IOException e){
        System.out.println("Erro");
        e.printStackTrace();
    }
    }


}