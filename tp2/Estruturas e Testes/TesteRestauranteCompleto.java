public class TesteRestauranteCompleto{
    public static void main(String[] args) {

        // Exemplo de linha do CSV (simulando o arquivo)
        String linha = "1,SushiHouse,Tokyo,80,4.5,Sushi;Japones,$$$,18:00-23:30,2020-05-10,true";

        // Parse do restaurante
        Restaurante r = Restaurante.parseRestaurante(linha);

        // Teste do formatar
        System.out.println(r.formatar());
    }
}
