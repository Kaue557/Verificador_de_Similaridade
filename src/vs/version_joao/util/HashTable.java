package version_joao.util;

public class HashTable {

    int size;

    // Classe Data para agrupar as informações armazenadas em cada
    // posição da tabela hash.
    //
    // Cada objeto Data representa um par:
    //      key   -> palavra (chave)
    //      valor -> frequência da palavra no documento
    //
    // O atributo 'prox' é utilizado para encadear elementos que
    // caíram no mesmo índice (tratamento de colisões por lista encadeada).
    //
    // Exemplo:
    // tabela[42] -> ("java", 3) -> ("dados", 5) -> null
    //
    // NÃO APAGUE! Esta classe é a base da estrutura.
    public static class Data {
        public String key;
        public int valor;
        // Já implementa um encadeamento simples!
        public Data prox;

        public Data(String k, int v) {
            this.key = k;
            this.valor = v;
            this.prox = null;
        }
    }

    // Vetor principal da tabela hash.
    //
    // Cada posição do vetor (bucket) aponta para o primeiro elemento
    // de uma lista encadeada.
    private Data[] hashTable;

    // Construtor da tabela hash.
    //
    // s = número de buckets (posições) da tabela.
    //
    // Recomenda-se utilizar números primos para melhorar a
    // distribuição das chaves.
    private int tipoHash; // 1 = Divisão, 2 = Multiplicação

    // Atualize o construtor para receber o tipo
    public HashTable(int s, int tipoHash) {
        this.size = s;
        this.tipoHash = tipoHash;
        this.hashTable = new Data[size];
    }

    public int calcularHash(String chave) {
        if (this.tipoHash == 2) {
            return Mhash(chave); // Usa Multiplicação
        }
        return Dhash(chave);     // Usa Divisão (Padrão)
    }

    // Converte uma String em um valor inteiro.
    //
    // Cada caractere é incorporado ao valor acumulado pela fórmula:
    //
    //      val = val * 31 + c
    //
    // O número 31 é primo e é amplamente utilizado em funções hash,
    // inclusive na implementação de String.hashCode() do Java.
    //
    // Essa abordagem preserva a ordem dos caracteres:
    //      "abc" != "cba"
    //
    // Diferente de simplesmente somar os códigos ASCII, essa técnica
    // reduz significativamente o número de colisões.
    //
    // IMPORTANTE:
    // Toda chave textual deve passar por este método antes de ser
    // utilizada em uma função hash.
    public static int toInt(String s) {
        int t = s.length();
        int val = 0;

        for (int i = 0; i < t; i++) {
            char c = s.charAt(i);
            val = val * 31 + (int) c;
        }

        return val;
    }

    // **********************************************************
    // FUNÇÃO HASH POR DIVISÃO
    // **********************************************************
    //
    // Faz:
    //      v mod size
    //
    // com:
    //      v    = valor inteiro da chave
    //      size = tamanho do vetor
    //
    // O operador:
    //      (v & 0x7fffffff)
    //
    // remove o bit de sinal do inteiro, garantindo um valor
    // não negativo mesmo em casos de overflow.
    //
    // NÃO TIRE O 0x7fffffff!
    // Isso evita condicionais extras e elimina problemas com
    // números negativos.
    public  int Dhash(int v) {
        return (v & 0x7fffffff) % size;
    }

    // Sobrecarga para Strings.
    //
    // Permite chamadas mais naturais:
    //      Dhash("java")
    //
    // Fluxo:
    //      String -> toInt() -> Dhash(int) -> índice
    //
    // Exemplo:
    //      "java" -> 3254818 -> 3254818 % size -> 42
    //
    // Ao implementar put(), get() e search(), este é o método
    // que deverá ser utilizado na maioria dos casos.
    //
    //  __
    // (o >
    // /\\ \\
    // \\V_/_
    //
    // Mais um penguim O:)
    public int Dhash(String chave) {
        return Dhash(toInt(chave));
    }

    // **********************************************************
    // FUNÇÃO HASH MULTIPLICATIVA
    // **********************************************************
    //
    // Fórmula:
    //      h(k) = floor( m * frac(k * A) )
    //
    // onde:
    //      m    = tamanho da tabela
    //      A    = constante entre 0 e 1
    //      frac = parte fracionária
    //
    // O valor de A escolhido é baseado no número áureo:
    //      A ≈ 0.6180339887
    //
    // Esse método tende a distribuir melhor as chaves mesmo
    // quando o tamanho da tabela não é primo.
    public int Mhash(int v) {
        v = (v & 0x7fffffff);

        double A = 0.6180339887;
        double p = v * A;

        // Extrai apenas a parte fracionária
        double pf = p - Math.floor(p);

        return (int) (size * pf);
    }

    // Sobrecarga para Strings.
    public int Mhash(String chave) {
        return Mhash(toInt(chave));
    }

    // Busca uma chave no bucket correspondente.
    //
    // Retorna:
    //   - o nó (Data) se a chave existir;
    //   - null caso a chave não seja encontrada.
    //
    // O método percorre a lista encadeada do índice calculado
    // pela função Dhash().
    public Data search(String chave) {
        // Calcula em qual posição da tabela a chave deve estar
        int indice = Dhash(chave);

        // Começa pelo primeiro elemento do bucket
        Data atual = hashTable[indice];

        // Percorre toda a lista encadeada
        while (atual != null) {
            // Se encontrou a chave, retorna o próprio nó
            if (atual.key.equals(chave)) {
                return atual;
            }

            // Avança para o próximo elemento da lista
            atual = atual.prox;
        }

        // Chave não encontrada
        return null;
    }

    // Insere uma palavra na tabela hash.
    //
    // Regras:
    // 1. Se a palavra já existir, incrementa sua frequência.
    // 2. Se não existir, cria um novo nó com frequência 1.
    // 3. O novo nó é inserido no início da lista encadeada.
    public void put(String chave) {
        // Verifica se a chave já está na tabela
        Data encontrado = search(chave);

        // Se já existe, apenas incrementa a frequência
        if (encontrado != null) {
            encontrado.valor++;
            return;
        }

        // Calcula o índice onde a nova chave será inserida
        int indice = Dhash(chave);

        // Cria um novo nó com frequência inicial 1
        Data novo = new Data(chave, 1);

        // O novo nó aponta para o antigo primeiro elemento do bucket
        novo.prox = hashTable[indice];

        // O novo nó passa a ser o primeiro da lista
        hashTable[indice] = novo;
    }

    // Retorna o primeiro elemento de um bucket específico.
    //
    // Útil para testes e para contar encadeamentos.
    public Data getBucket(int indice) {
        return hashTable[indice];
    }

    public void redimensionar(int novoTamanho) {
    // 1. Cria o novo vetor com o tamanho expandido
        Data[] novoVetor = new Data[novoTamanho];
    
    // Guarda o tamanho antigo para a iteração
        int tamanhoAntigo = this.size;
    
    // Atualiza o tamanho da tabela global da instância para as funções de hash usarem o novo valor
        this.size = novoTamanho; 

    // 2. Percorre o vetor antigo para migrar os elementos
        for (int i = 0; i < tamanhoAntigo; i++) {
            Data atual = hashTable[i]; // Pega o início da lista no bucket atual
        
        while (atual != null) {
            // Guarda o próximo elemento antes de alterar a referência do atual
            Data proximo = atual.prox; 
            
            // Recalcula o novo índice usando a função de hash atualizada com o novo 'size'
            int novoIndice = Dhash(atual.key); 
            
            // Insere o nó no início da lista encadeada do novo vetor (Tratamento de Colisão)
            atual.prox = novoVetor[novoIndice];
            novoVetor[novoIndice] = atual;
            
            // Avança para o próximo nó da lista antiga
            atual = proximo;
        }
    }
    
    // 3. Substitui o vetor antigo pelo novo vetor redimensionado
    this.hashTable = novoVetor;
}


    public Data[] getTable() {
        return hashTable;
    }

}