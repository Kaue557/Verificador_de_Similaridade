package version_joao;

class HashTable {

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
        String key;
        int valor;
        Data prox;

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

    public HashTable(int s) {
        this.size = s;
        hashTable = new Data[size];
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
    public int toInt(String s) {
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
    //      com v = int da chave
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
    public int Dhash(int v) {
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
}