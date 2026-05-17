package version_joao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import version_joao.util.Util;
import version_joao.Arvore_AVL.Arvore;
import version_joao.Arvore_AVL.Resultado;
import version_joao.Arvore_AVL.No;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            return;
        }

        String diretorioPath = args[0];
        double limiar = Double.parseDouble(args[1]);
        String modo = args[2];
        
        File pasta = new File(diretorioPath);
        File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".txt"));

        if (arquivos == null || arquivos.length < 2) {
            System.out.println("Erro: O diretório precisa conter pelo menos 2 arquivos .txt");
            return;
        }
        int tipoHashEscolhido = 1; 

        Util util = new Util();
        List<Documento> documentos = new ArrayList<>();
        Arvore avl = new Arvore();

        try {
            // 1. Processa os documentos passando o tipo escolhido
            for (File arquivo : arquivos) {
                documentos.add(new Documento(arquivo.toPath(), util, tipoHashEscolhido));
            }

            // 2. Compara todos contra todos (Combinação Simples) e insere na AVL
            int totalPares = 0;
            for (int i = 0; i < documentos.size(); i++) {
                for (int j = i + 1; j < documentos.size(); j++) {
                    Resultado res = Cosseno.comparar(documentos.get(i), documentos.get(j));
                    avl.inserir(res.getSimilaridade(), res); // Ajuste feito para AVL suportar Similaridade+Resultado
                    totalPares++;
                }
            }

            // 3. Gera a saída e grava no Log
            gerarSaida(documentos.size(), totalPares, avl, modo, limiar, args, tipoHashEscolhido);

        } catch (Exception e) {
            System.out.println("Erro na execução: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void gerarSaida(int totalDocs, int totalPares, Arvore avl, String modo, double limiar, String[] args, int tipoHashEscolhido) {
        StringBuilder output = new StringBuilder();
        output.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n");
        output.append("Total de documentos processados: ").append(totalDocs).append("\n");
        output.append("Total de pares comparados: ").append(totalPares).append("\n");
        output.append("Função hash utilizada: Divisão (Dhash)\n");
        output.append("Métrica de similaridade: Cosseno\n\n");

        if (modo.equals("lista")) {
            output.append("Pares com similaridade >= ").append(limiar).append(":\n");
            coletarAcimaLimiar(avl.getRaiz(), limiar, output);
        } else if (modo.equals("busca") && args.length >= 5) {
            String target1 = args[3];
            String target2 = args[4];
            output.append("Comparando: ").append(target1).append(" <-> ").append(target2).append("\n");
            buscarParEspecifico(avl.getRaiz(), target1, target2, output);
        } else if (modo.equals("topk") && args.length >= 4) {
            int k = Integer.parseInt(args[3]);
            output.append("Top ").append(k).append(" pares mais semelhantes:\n");
            // A implementação do Top K exige percorrer a AVL do maior para o menor.
            // Aqui seria feita a chamada para um método em-ordem invertido na árvore.
        }

        System.out.println(output.toString());

        output.append("Total de pares comparados: ").append(totalPares).append("\n");
        
        // Verifica qual hash rodou
        String nomeHash = (args.length > 0) ? "Divisão (Dhash)" : "Multiplicação (Mhash)";
        output.append("Função hash utilizada: ").append(tipoHashEscolhido == 1 ? "Divisão" : "Multiplicação").append("\n");
        
        output.append("Métrica de similaridade: Cosseno\n\n");

        // Grava no arquivo
        try (PrintWriter out = new PrintWriter(new FileWriter("resultado.txt"))) {
            out.println(output.toString());
        } catch (IOException e) {
            System.out.println("Erro ao gravar resultado.txt");
        }
    }

    private static void coletarAcimaLimiar(No atual, double limiar, StringBuilder output) {
        if (atual != null) {
            coletarAcimaLimiar(atual.getEsquerda(), limiar, output);
            if (atual.getSimilaridade() >= limiar) {
                // A árvore do PDF deve guardar uma lista de Resultados em caso de empates.
                // Substitua getResultados() pelo método exato da sua classe No, caso divirja.
                output.append(atual.getResultados().get(0).toString()).append("\n");
            }
            coletarAcimaLimiar(atual.getDireita(), limiar, output);
        }
    }

    private static void buscarParEspecifico(No atual, String d1, String d2, StringBuilder output) {
         if (atual != null) {
            buscarParEspecifico(atual.getEsquerda(), d1, d2, output);
            Resultado res = atual.getResultados().get(0);
            if ((res.getDoc1().equals(d1) && res.getDoc2().equals(d2)) || 
                (res.getDoc1().equals(d2) && res.getDoc2().equals(d1))) {
                output.append("Similaridade calculada: ").append(String.format("%.2f", res.getSimilaridade())).append("\n");
            }
            buscarParEspecifico(atual.getDireita(), d1, d2, output);
        }
    }
}