import java.util.Scanner;
import java.io.FileWriter;

class Data{
	private int dia;
	private int mes;
	private int ano;

	public Data(int dia, int mes, int ano){
		this.dia = dia;
		this.mes = mes;
		this.ano = ano;
	}

	public int getAno(){
		return ano;
	}
	public int getMes(){
		return mes;
	}
	public int getDia(){
		return dia;
	}

	public static Data parseData(String d){
		Scanner sc = new Scanner(d);
		sc.useDelimiter("-");
		int ano = sc.nextInt();
		int mes = sc.nextInt();
		int dia = sc.nextInt();
		sc.close();
		return new Data(dia, mes, ano);
	}

	public String formatar(){
		return String.format("%02d/%02d/%04d", dia, mes, ano);
	}
}

class Hora{
	private int hora;
	private int min;

	public Hora(int hora, int min){
		this.hora = hora;
		this.min = min;
	}

	public static Hora parseHora(String h){
		Scanner sc = new Scanner(h);
		sc.useDelimiter(":");
		int hora = sc.nextInt();
		int min = sc.nextInt();
		sc.close();
		return new Hora(hora, min);
	}

	public String formatar(){
		return String.format("%02d:%02d", hora, min);
	}
}

class Restaurante{
	private Data dataAbertura;
	private Hora horarioAbertura;
	private Hora horarioFechamento;
	private boolean aberto;
	private int id;
	private String nome;
	private String cidade;
	private int capacidade;
	private double avaliacao;
	private String[] tiposCozinha;
	private int faixaPreco;

	public Restaurante(int id, String nome, String cidade, int capacidade, double avaliacao,
			String[] tiposCozinha, int faixaPreco,
			Hora horarioAbertura, Hora horarioFechamento,
			Data dataAbertura, boolean aberto){

		this.id = id;
		this.nome = nome;
		this.cidade = cidade;
		this.capacidade = capacidade;
		this.avaliacao = avaliacao;
		this.tiposCozinha = tiposCozinha;
		this.faixaPreco = faixaPreco;
		this.horarioAbertura = horarioAbertura;
		this.horarioFechamento = horarioFechamento;
		this.dataAbertura = dataAbertura;
		this.aberto = aberto;
	}

	public int getId(){
		return id;
	}
	public String getNome(){
		return nome;
	}
	public String getCidade(){
		return cidade;
	}
	public int getCapacidade(){
		return capacidade;
	}
	public double getAvaliacao(){
		return avaliacao;
	}
	public String[] getTiposCozinha(){
		return tiposCozinha;
	}
	public int getFaixaPreco(){
		return faixaPreco;
	}
	public Hora getHorarioAbertura(){
		return horarioAbertura;
	}
	public Hora getHorarioFechamento(){
		return horarioFechamento; 
	}
	public Data getDataAbertura(){ 
		return dataAbertura;
	}
	public boolean getAberto(){ 
		return aberto; 
	}

	public static String[] separarTipos(String tipos){
		Scanner sc = new Scanner(tipos);
		sc.useDelimiter(";");

		String[] lista = new String[5];
		int i = 0;
		while(sc.hasNext()){
			lista[i++] = sc.next();
		}

		sc.close();
		return lista;
	}

	public static int converterFaixa(String faixa){
		int num = 0;
		for(int i = 0; i < faixa.length(); i++){
			if(faixa.charAt(i) == '$'){
				num++;
			}
		}
		return num;
	}

	public static String[] separarHora(String hora){
		Scanner sc = new Scanner(hora);
		sc.useDelimiter("-");

		String abertura = sc.next();
		String fechamento = sc.next();
		sc.close();

		return new String[]{abertura, fechamento};
	}

	public static Restaurante parseRestaurante(String linha){
		Scanner sc = new Scanner(linha);
		sc.useDelimiter(",");
		sc.useLocale(java.util.Locale.US);

		int id = sc.nextInt();
		String nome = sc.next();
		String cidade = sc.next();
		int capacidade = sc.nextInt();
		double avaliacao = sc.nextDouble();

		String[] tipos = separarTipos(sc.next());
		int faixa = converterFaixa(sc.next());

		String[] horas = separarHora(sc.next());
		Hora abertura = Hora.parseHora(horas[0]);
		Hora fechamento = Hora.parseHora(horas[1]);

		Data data = Data.parseData(sc.next());

		boolean aberto = (sc.next().charAt(0) == 't');

		sc.close();

		return new Restaurante(id, nome, cidade, capacidade, avaliacao,
				tipos, faixa, abertura, fechamento, data, aberto);
	}

	public String imprimir(){
		String tipos = "";
		for(int i = 0; i < tiposCozinha.length && tiposCozinha[i] != null; i++){
			tipos += tiposCozinha[i];
			if(i < tiposCozinha.length - 1 && tiposCozinha[i+1] != null){
				tipos += ",";
			}
		}

		String faixa = "";
		for(int i = 0; i < faixaPreco; i++){
			faixa += "$";
		}

		return "[" + id + " ## " + nome + " ## " + cidade + " ## " + capacidade + " ## " + avaliacao + " ## [" + tipos + "] ## " + faixa + " ## " + horarioAbertura.formatar() + "-" + horarioFechamento.formatar() + " ## " + dataAbertura.formatar() + " ## " + aberto + "]";
	}
}

class ColecaoRestaurantes{
	private int tamanho;
	private Restaurante[] restaurantes;

	public ColecaoRestaurantes(){
		this.restaurantes = new Restaurante[2000];
		this.tamanho = 0;
	}

	public int getTamanho(){ 
		return tamanho; 
	}
	public Restaurante[] getRestaurantes(){ 
		return restaurantes; 
	}

	public void lerCsv(String path){
		try{
			Scanner sc = new Scanner(new java.io.File(path));

			if(sc.hasNextLine()){
				sc.nextLine();
			}
			while(sc.hasNextLine()){
				String l = sc.nextLine();

				Restaurante r = Restaurante.parseRestaurante(l);
				restaurantes[tamanho] = r;
				tamanho++;
			}
			sc.close();
		}catch(Exception e){
			System.out.println("Erro ao ler");
		}
	}


	public static ColecaoRestaurantes lerCsv(){
		ColecaoRestaurantes c = new ColecaoRestaurantes();

		try{
			Scanner sc = new Scanner(new java.io.File("/tmp/restaurantes.csv"));

			if(sc.hasNextLine()) sc.nextLine();

			while(sc.hasNextLine()){
				c.restaurantes[c.tamanho++] =
					Restaurante.parseRestaurante(sc.nextLine());
			}

			sc.close();
		}catch(Exception e){
			System.out.println("Erro ao ler");
		}

		return c;
	}
}

class Fila{
	private Restaurante[] array;
	private int primeiro;
	private int ultimo;
	private int tamanho;

	public Fila(){
		array = new Restaurante[5];
		primeiro = 0;
		ultimo = 0;
		tamanho = 0;
	}

	public void inserir(Restaurante x){
		if(tamanho == 5){
			remover();
		}
		array[ultimo] = x;
		ultimo = (ultimo + 1) % 5;
		tamanho++;

		System.out.println("(I)" + media());
	}

	public Restaurante remover(){
		Restaurante resp = array[primeiro];
		primeiro = (primeiro + 1) % 5;
		tamanho--;

		System.out.println("(R)" + resp.getNome());

		return resp;
	}

	public int media(){
		int soma = 0;

		for(int i = 0; i < tamanho; i++){
			int pos = (primeiro + i) % 5;
			soma += array[pos].getDataAbertura().getAno();
		}
		double media = (double)soma / tamanho;
		int resp = (int)media;
		if(media - resp >= 0.5){
			resp = resp + 1;
		}

		return resp;
	}

	public void mostrar(){
		for(int i = 0; i < tamanho; i++){
			int pos = (primeiro + i) % 5;
			System.out.println(array[pos].imprimir());
		}
	}
}

public class Restaurantes7{
	public static void main(String[] args) throws Exception{

		ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();
		Scanner sc = new Scanner(System.in);

		Fila fila = new Fila();

		int id = sc.nextInt();

		while(id != -1){

			int j = 0;
			boolean achou = false;

			while(j < colecao.getTamanho() && achou == false){
				if(colecao.getRestaurantes()[j].getId() == id){
					fila.inserir(colecao.getRestaurantes()[j]);
					achou = true;
				}
				j++;
			}

			id = sc.nextInt();
		}

		int n = sc.nextInt();
		sc.nextLine();

		for(int i = 0; i < n; i++){

			String linha = sc.nextLine();

			while(linha.length() == 0){
				linha = sc.nextLine();
			}

			if(linha.charAt(0) == 'I'){

				Scanner sc2 = new Scanner(linha);
				sc2.next();
				int idInserir = sc2.nextInt();

				int j = 0;
				boolean achou = false;

				while(j < colecao.getTamanho() && achou == false){
					if(colecao.getRestaurantes()[j].getId() == idInserir){
						fila.inserir(colecao.getRestaurantes()[j]);
						achou = true;
					}
					j++;
				}

				sc2.close();

			}else if(linha.charAt(0) == 'R'){
				fila.remover();
			}
		}

		fila.mostrar();

		sc.close();
	}
}


