public class TesteColecao {
    public static void main(String[] args) {

        // Usando o método que retorna a coleção pronta
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();

        // Mostrar quantos restaurantes foram carregados
        System.out.println("Total: " + colecao.getTamanho());

        // Imprimir alguns restaurantes (ex: primeiros 5)
        Restaurante[] lista = colecao.getRestaurantes();

        for (int i = 0; i < 5 && i < colecao.getTamanho(); i++) {
            System.out.println(lista[i].formatar());
        }
    }
}
