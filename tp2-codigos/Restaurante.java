import java.util.Scanner;

class Restaurante{
	private Data data_abertura;
	private Hora horario_abertura;
	private Hora horario_fechamento;
	private boolean aberto;
	private int id;
	private String nome;
	private String cidade;
	private int capacidade;
	private double avaliacao;
	private int[] tipos_cozinha;
	private int faixa_preco;

	public static Restaurante(String linha){
		
	}


	public static Restaurante parseRestaurante(String linha){
		Scanner sc = new Scanner(linha);
		sc
	}
}
