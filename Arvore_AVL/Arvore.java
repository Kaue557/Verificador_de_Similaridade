package arvore_AVL;

public class Arvore {

    private No raiz;
    private int rotacoesSimples;
    private int rotacoesDuplas;

    public int getRotacoesSimples() {
        return rotacoesSimples;
    }

    public int getRotacoesDuplas() {
        return rotacoesDuplas;
    }

    // INSERÇÃO V.2
    public No inserir(No no, double similaridade, Resultado resultado){
        // CASO BASE
        if (no == null) {
            No novoNo = new No(similaridade);
            novoNo.getResultados().add(resultado);
            return novoNo;
        }

        if (similaridade < no.getSimilaridade()) { // similaridade menor, desce pra esquerda
            no.setEsquerda(inserir(no.getEsquerda(), similaridade, resultado));
        } else if (similaridade > no.getSimilaridade()) { // similaridade maior, desce pra direita
            no.setDireita(inserir(no.getDireita(), similaridade, resultado));
        } else { // agora permite duplicados
            no.getResultados().add(resultado); // adiciona na lista
            return no;
        }

        // Atualiza altura
        no.setAltura(
                1 + Math.max(
                        altura(no.getEsquerda()),
                        altura(no.getDireita())
                )
        );

        // Atualiza balanceamento
        int balance = fatorBalanceamento(no);


        // CASOS DE ROTAÇÃO

        // rotacao direita
        if (balance > 1 && similaridade < no.getEsquerda().getSimilaridade()) {
            rotacoesSimples++;
            return rotacaoDireita(no);
        }

        // rotacao esquerda
        if (balance < -1 && similaridade > no.getDireita().getSimilaridade()) {
            rotacoesSimples++;
            return rotacaoEsquerda(no);
        }

        // esquerda + direita
        if (balance > 1 && similaridade > no.getEsquerda().getSimilaridade()) {
            rotacoesDuplas++;
            no.setEsquerda(rotacaoEsquerda(no.getEsquerda()));
            return rotacaoDireita(no);
        }

        // direita + esquerda
        if (balance < -1 && similaridade < no.getDireita().getSimilaridade()) {
            rotacoesDuplas++;
            no.setDireita(rotacaoDireita(no.getDireita()));
            return rotacaoEsquerda(no);
        }

        return no;
    }

    public No remover(No no, double similaridade) {
        if (no == null) return null;

        // BUSCA
        if (similaridade < no.getSimilaridade()) {
            no.setEsquerda(remover(no.getEsquerda(), similaridade));
        } else if (similaridade > no.getSimilaridade()) {
            no.setDireita(remover(no.getDireita(), similaridade));
        } else {

            // NÓ ENCONTRADO

            // Caso 1: sem filhos ou 1 filho
            if (no.getEsquerda() == null || no.getDireita() == null) {
                No temp;

                if (no.getEsquerda() != null)
                    temp = no.getEsquerda();
                else
                    temp = no.getDireita();

                // sem filhos
                if (temp == null) {
                    return null;
                } else {
                    return temp; // substitui pelo filho
                }
            }

            // Caso 2: dois filhos
            No sucessor = menorsimilaridade(no.getDireita());
            no.setSimilaridade(sucessor.getSimilaridade());
            no.setDireita(remover(no.getDireita(), sucessor.getSimilaridade()));
        }

        no.setAltura(
                1 + Math.max(
                        altura(no.getEsquerda()),
                        altura(no.getDireita())
                )
        );

        // REBALANCEAMENTO
        int balance = fatorBalanceamento(no);

        // rotacao direita
        if (balance > 1 && fatorBalanceamento(no.getEsquerda()) >= 0) {
            return rotacaoDireita(no);
        }

        // esquerda + direita
        if (balance > 1 && fatorBalanceamento(no.getEsquerda()) < 0) {
            no.setEsquerda(rotacaoEsquerda(no.getEsquerda()));
            return rotacaoDireita(no);
        }

        // rotacao esquerda
        if (balance < -1 && fatorBalanceamento(no.getDireita()) <= 0) {
            return rotacaoEsquerda(no);
        }

        // direita + esquerda
        if (balance < -1 && fatorBalanceamento(no.getDireita()) > 0) {
            no.setDireita(rotacaoDireita(no.getDireita()));
            return rotacaoEsquerda(no);
        }

        return no;
    }

    private No menorsimilaridade(No no) {
        No atual = no;
        while (atual.getEsquerda() != null) {
            atual = atual.getEsquerda();
        }
        return atual;
    }

    public void remover(double similaridade) {
        raiz = remover(raiz, similaridade);
    }


    // ALTURA V.2
    private int altura(No no) {
        if (no == null)
            return 0;

        return no.getAltura();
    }


    // FATOR DE BALANCEAMENTO
    private int fatorBalanceamento(No no) {
        if (no == null) return 0;
        return altura(no.getEsquerda()) - altura(no.getDireita());
    }


    // ROTACAO À DIREITA
    private No rotacaoDireita(No y) {
        No x = y.getEsquerda();
        No T2 = x.getDireita();

        x.setDireita(y);
        y.setEsquerda(T2);

        y.setAltura(
                1 + Math.max(
                        altura(y.getEsquerda()),
                        altura(y.getDireita())
                )
        );

        x.setAltura(
                1 + Math.max(
                        altura(x.getEsquerda()),
                        altura(x.getDireita())
                )
        );

        return x;
    }


    // ROTACAO À ESQUERDA
    private No rotacaoEsquerda(No x) {
        No y = x.getDireita();
        No T2 = y.getEsquerda();

        y.setEsquerda(x);
        x.setDireita(T2);

        x.setAltura(
                1 + Math.max(
                        altura(x.getEsquerda()),
                        altura(x.getDireita())
                )
        );

        y.setAltura(
                1 + Math.max(
                        altura(y.getEsquerda()),
                        altura(y.getDireita())
                )
        );

        return y;
    }


    // METODO PÚBLICO
    public void inserir(double similaridade, Resultado resultado) {
        raiz = inserir(raiz, similaridade, resultado);
    }

    public No getRaiz() {
        return raiz;
    }


    // PERCURSOS -------------------------------------

    public void emOrdem(No atual) { // esquerda - raiz - direita
        if (atual != null) {
            emOrdem(atual.getEsquerda());

            System.out.println(atual.getSimilaridade());

            for (Resultado r : atual.getResultados()) {
                System.out.println(r);
            }

            emOrdem(atual.getDireita());
        }
    }

    public void preOrdem(No atual) { // raiz - esquerda - direita
        if (atual != null) {

            System.out.println(atual.getSimilaridade());

            for (Resultado r : atual.getResultados()) {
                System.out.println(r);
            }

            preOrdem(atual.getEsquerda());
            preOrdem(atual.getDireita());
        }
    }

    public void posOrdem(No atual) { // esquerda - direita - raiz
        if (atual != null) {
            posOrdem(atual.getEsquerda());
            posOrdem(atual.getDireita());

            System.out.println(atual.getSimilaridade());

            for (Resultado r : atual.getResultados()) {
                System.out.println(r);
            }
        }
    }
}