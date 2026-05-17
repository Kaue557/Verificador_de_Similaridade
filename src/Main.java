/*
========================================
NOME                            RA

Aline Vidal                     10721348
João Vitor Fernandes Messias    10723552
Kauê Lima Rodrigues Meneses     10410594
Rayana Pimentel Marques Lopes   10435370
========================================
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Validação de argumentos da linha de comando
        if (args.length < 3) {
            System.out.println("Uso correto: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            return;
        }

        String diretorioStr = args[0];
        double limiar = Double.parseDouble(args[1]);
        String modo = args[2].toLowerCase();

        File diretorio = new File(diretorioStr);
        File[] arquivosTxt = diretorio.listFiles((dir, name) -> name.endsWith(".txt"));

        if (arquivosTxt == null || arquivosTxt.length < 2) {
            System.out.println("Erro: O diretório precisa conter pelo menos 2 arquivos .txt para comparação.");
            return;
        }

        List<Documento> documentos = new ArrayList<>();
        AVLTree arvore = new AVLTree();
        int paresComparados = 0;

        try {
            // 1. Processa todos os arquivos e cria os Documentos (HashTables preenchidas)
            for (File arquivo : arquivosTxt) {
                documentos.add(new Documento(arquivo.toPath()));
            }

            // 2. Compara todos os pares possíveis (Combinação 2 a 2)
            for (int i = 0; i < documentos.size(); i++) {
                for (int j = i + 1; j < documentos.size(); j++) {
                    Documento doc1 = documentos.get(i);
                    Documento doc2 = documentos.get(j);

                    // Calcula similaridade (substituindo a antiga classe Cosseno)
                    double similaridade = ComparadorDeDocumentos.calcularCosseno(doc1, doc2);

                    // Cria o objeto de resultado e insere na Árvore AVL
                    Resultado resultado = new Resultado(doc1.getNomeArquivo(), doc2.getNomeArquivo(), similaridade);
                    arvore.inserir(similaridade, resultado);
                    paresComparados++;
                }
            }

            // 3. Gera a saída de acordo com o modo escolhido
            gerarSaida(documentos.size(), paresComparados, arvore, modo, limiar, args);

        } catch (Exception e) {
            System.out.println("Erro na execução do processamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void gerarSaida(int totalDocs, int paresComparados, AVLTree arvore, String modo, double limiar, String[] args) {
        StringBuilder sb = new StringBuilder();

        // Cabeçalho padronizado exigido pelo PDF
        sb.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n");
        sb.append("Total de documentos processados: ").append(totalDocs).append("\n");
        sb.append("Total de pares comparados: ").append(paresComparados).append("\n");
        sb.append("Função hash utilizada: Divisão (Dhash)\n"); // Pode alterar se usar a Mhash
        sb.append("Métrica de similaridade: Cosseno\n\n");

        // Delega a busca baseada no Modo
        if (modo.equals("lista")) {
            sb.append("Pares com similaridade >= ").append(limiar).append(":\n");
            for (Resultado r : arvore.obterAcimaLimiar(limiar)) {
                sb.append(r.toString()).append("\n");
            }
        }
        else if (modo.equals("busca") && args.length >= 5) {
            String docA = args[3];
            String docB = args[4];
            sb.append("Comparando: ").append(docA).append(" <-> ").append(docB).append("\n");

            String similaridadeAchada = arvore.buscarPar(docA, docB);
            if(similaridadeAchada != null) {
                sb.append("Similaridade calculada: ").append(similaridadeAchada).append("\n");
            } else {
                sb.append("Par não encontrado.\n");
            }
        }
        else if (modo.equals("topk") && args.length >= 4) {
            int k = Integer.parseInt(args[3]);
            sb.append("Top ").append(k).append(" pares mais semelhantes:\n");
            for (Resultado r : arvore.obterTopK(k)) {
                sb.append(r.toString()).append("\n");
            }
        } else {
            sb.append("Modo ou argumentos inválidos.\n");
        }

        String saidaFinal = sb.toString();

        // Imprime no Console
        System.out.println(saidaFinal);

        // Grava no arquivo resultado.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("resultado.txt"))) {
            writer.print(saidaFinal);
        } catch (IOException e) {
            System.out.println("Erro ao gravar o arquivo resultado.txt");
        }
    }

    // ************************************************************************
    // PERCURSOS CUSTOMIZADOS PARA A ÁRVORE (DIREITA -> RAIZ -> ESQUERDA)
    // Usamos o sentido reverso para pegar sempre os maiores valores de similaridade primeiro.
    // ************************************************************************

    private static void coletarAcimaLimiar(No no, double limiar, StringBuilder sb) {
        if (no != null) {
            coletarAcimaLimiar(no.getDireita(), limiar, sb); // Visita direita (maiores)

            if (no.getSimilaridade() >= limiar) {
                // Itera sobre TODOS os resultados daquele nó (tratamento de colisão)
                for (Resultado res : no.getResultados()) {
                    sb.append(res.toString()).append("\n");
                }
            }

            coletarAcimaLimiar(no.getEsquerda(), limiar, sb); // Visita esquerda (menores)
        }
    }

    private static void coletarTopK(No no, int[] contador, int k, StringBuilder sb) {
        if (no != null && contador[0] < k) {
            coletarTopK(no.getDireita(), contador, k, sb);

            // Verifica os resultados do nó atual
            for (Resultado res : no.getResultados()) {
                if (contador[0] < k) {
                    sb.append(res.toString()).append("\n");
                    contador[0]++;
                } else {
                    break;
                }
            }

            coletarTopK(no.getEsquerda(), contador, k, sb);
        }
    }

    private static void buscarParEspecifico(No no, String doc1, String doc2, StringBuilder sb) {
        if (no != null) {
            buscarParEspecifico(no.getDireita(), doc1, doc2, sb);

            for (Resultado res : no.getResultados()) {
                // Checa as duas combinações possíveis (doc1-doc2 ou doc2-doc1)
                if ((res.getDoc1().equals(doc1) && res.getDoc2().equals(doc2)) ||
                        (res.getDoc1().equals(doc2) && res.getDoc2().equals(doc1))) {
                    sb.append("Similaridade calculada: ").append(String.format("%.2f", res.getSimilaridade())).append("\n");
                }
            }

            buscarParEspecifico(no.getEsquerda(), doc1, doc2, sb);
        }
    }
}