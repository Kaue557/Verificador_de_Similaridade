package version_joao.Arvore_AVL;

public class Arvore {

    private No raiz;
    private int totalRotacoes = 0;

    // ==========================================
    // 1. INSERÇÃO
    // ==========================================
    public void inserir(double similaridade, Resultado resultado) {
        this.raiz = inserirRecursivo(this.raiz, similaridade, resultado);
    }

    private No inserirRecursivo(No no, double similaridade, Resultado resultado) {
        
        // Passo 1: Inserção normal de BST
        if (no == null) {
            No novoNo = new No(similaridade);
            novoNo.getResultados().add(resultado); 
            return novoNo;
        }

        if (similaridade < no.getSimilaridade()) {
            no.setEsquerda(inserirRecursivo(no.getEsquerda(), similaridade, resultado));
        } else if (similaridade > no.getSimilaridade()) {
            no.setDireita(inserirRecursivo(no.getDireita(), similaridade, resultado));
        } else { 
            // Empate: Adiciona o documento na lista do nó existente
            no.getResultados().add(resultado); 
            return no;
        }

        // Passo 2: Atualiza a altura deste nó ancestral
        no.setAltura(1 + Math.max(altura(no.getEsquerda()), altura(no.getDireita())));

        // Passo 3: Calcula o fator de balanceamento para verificar se desbalanceou
        int balance = fatorBalanceamento(no);

        // Passo 4: Casos de Rotação
        
        // Rotação Simples à Direita
        if (balance > 1 && similaridade < no.getEsquerda().getSimilaridade()) {
            return rotacaoDireita(no);
        }

        // Rotação Simples à Esquerda
        if (balance < -1 && similaridade > no.getDireita().getSimilaridade()) {
            return rotacaoEsquerda(no);
        }

        // Rotação Dupla: Esquerda-Direita
        if (balance > 1 && similaridade > no.getEsquerda().getSimilaridade()) {
            no.setEsquerda(rotacaoEsquerda(no.getEsquerda()));
            return rotacaoDireita(no);
        }

        // Rotação Dupla: Direita-Esquerda
        if (balance < -1 && similaridade < no.getDireita().getSimilaridade()) {
            no.setDireita(rotacaoDireita(no.getDireita()));
            return rotacaoEsquerda(no);
        }

        return no;
    }

    // ==========================================
    // 2. ROTAÇÕES (Com registro de contagem)
    // ==========================================
    private No rotacaoDireita(No y) {
        this.totalRotacoes++; // Registrando a rotação para o relatório

        No x = y.getEsquerda();
        No T2 = x.getDireita();

        // Realiza a rotação
        x.setDireita(y);
        y.setEsquerda(T2);

        // Atualiza as alturas
        y.setAltura(Math.max(altura(y.getEsquerda()), altura(y.getDireita())) + 1);
        x.setAltura(Math.max(altura(x.getEsquerda()), altura(x.getDireita())) + 1);

        return x;
    }

    private No rotacaoEsquerda(No x) {
        this.totalRotacoes++; // Registrando a rotação para o relatório

        No y = x.getDireita();
        No T2 = y.getEsquerda();

        // Realiza a rotação
        y.setEsquerda(x);
        x.setDireita(T2);

        // Atualiza as alturas
        x.setAltura(Math.max(altura(x.getEsquerda()), altura(x.getDireita())) + 1);
        y.setAltura(Math.max(altura(y.getEsquerda()), altura(y.getDireita())) + 1);

        return y;
    }

    // ==========================================
    // 3. MÉTODOS AUXILIARES
    // ==========================================
    private int altura(No no) {
        if (no == null) return 0;
        return no.getAltura();
    }

    private int fatorBalanceamento(No no) {
        if (no == null) return 0;
        return altura(no.getEsquerda()) - altura(no.getDireita());
    }

    public No getRaiz() {
        return raiz;
    }


    public int getTotalRotacoes() {
        return totalRotacoes; 
    }

    // ==========================================
    // 4. REMOÇÃO 
    // ==========================================
    public void remover(double similaridade) {
        raiz = removerRecursivo(raiz, similaridade);
    }

    private No removerRecursivo(No no, double similaridade) {
        if (no == null) return null;

        if (similaridade < no.getSimilaridade()) {
            no.setEsquerda(removerRecursivo(no.getEsquerda(), similaridade));
        } else if (similaridade > no.getSimilaridade()) {
            no.setDireita(removerRecursivo(no.getDireita(), similaridade));
        } else {
            // Nó com apenas um filho ou sem filhos
            if (no.getEsquerda() == null || no.getDireita() == null) {
                No temp = null;
                if (temp == no.getEsquerda()) temp = no.getDireita();
                else temp = no.getEsquerda();

                if (temp == null) {
                    temp = no;
                    no = null;
                } else {
                    no = temp; 
                }
            } else {
                // Nó com dois filhos: pega o sucessor in-order (menor da direita)
                No sucessor = menorSimilaridade(no.getDireita());
                no.setSimilaridade(sucessor.getSimilaridade());
                no.setDireita(removerRecursivo(no.getDireita(), sucessor.getSimilaridade()));
            }
        }

        if (no == null) return no;

        // Atualiza a altura do nó corrente
        no.setAltura(Math.max(altura(no.getEsquerda()), altura(no.getDireita())) + 1);

        // Rebalanceia a árvore após a remoção
        int balance = fatorBalanceamento(no);

        if (balance > 1 && fatorBalanceamento(no.getEsquerda()) >= 0) {
            return rotacaoDireita(no);
        }
        if (balance > 1 && fatorBalanceamento(no.getEsquerda()) < 0) {
            no.setEsquerda(rotacaoEsquerda(no.getEsquerda()));
            return rotacaoDireita(no);
        }
        if (balance < -1 && fatorBalanceamento(no.getDireita()) <= 0) {
            return rotacaoEsquerda(no);
        }
        if (balance < -1 && fatorBalanceamento(no.getDireita()) > 0) {
            no.setDireita(rotacaoDireita(no.getDireita()));
            return rotacaoEsquerda(no);
        }

        return no;
    }

    private No menorSimilaridade(No no) {
        No atual = no;
        while (atual.getEsquerda() != null) {
            atual = atual.getEsquerda();
        }
        return atual;
    }

    // ==========================================
    // 5. PERCURSOS
    // ==========================================
    public void emOrdem(No atual) {
        if (atual != null) {
            emOrdem(atual.getEsquerda());
            System.out.println(atual.getSimilaridade());
            emOrdem(atual.getDireita());
        }
    }
}