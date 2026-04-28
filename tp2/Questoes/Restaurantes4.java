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

class MergeSort{
	public static void ordenacao(Restaurante[] v, int n, long[] comp, long[] mov){
		mergeSort(v, 0, n - 1, comp, mov);
	}
	private static void mergeSort(Restaurante[] v, int esq, int dir, long[] comp, long[] mov) {
		if (esq < dir) {
			int meio = (esq + dir) / 2;
			mergeSort(v, esq, meio, comp, mov);
			mergeSort(v, meio + 1, dir, comp, mov);
			intercalar(v, esq, meio, dir, comp, mov);
		}
	}

	private static void intercalar(Restaurante[] v, int esq, int meio, int dir, long[] comp, long[] mov) {
		int n1 = meio - esq + 1;
		int n2 = dir - meio;

		Restaurante[] a = new Restaurante[n1];
		Restaurante[] b = new Restaurante[n2];

		for (int i = 0; i < n1; i++) {
			a[i] = v[esq + i];
			mov[0]++;
		}

		for (int j = 0; j < n2; j++) {
			b[j] = v[meio + 1 + j];
			mov[0]++;
		}

		int i = 0;
		int j = 0;
		int k = esq;

		while (i < n1 && j < n2) {
			comp[0]++;
			int cmpCidade = a[i].getCidade().compareTo(b[j].getCidade());

			if (cmpCidade < 0 || (cmpCidade == 0 && a[i].getNome().compareTo(b[j].getNome()) <= 0)) {
				v[k] = a[i];
				i++;
			} else {
				v[k] = b[j];
				j++;
			}
			mov[0]++;
			k++;
		}

		while (i < n1) {
			v[k] = a[i];
			i++;
			k++;
			mov[0]++;
		}

		while (j < n2) {
			v[k] = b[j];
			j++;
			k++;
			mov[0]++;
		}
	}
}

public class Restaurantes4{
	public static void main(String[] args) throws Exception{
		ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();
		Scanner sc = new Scanner(System.in);

		int[] ids = new int[1000];
		int qtdIds = 0;
		int id = sc.nextInt();

		while(id != -1){
			ids[qtdIds] = id;
			qtdIds++;
			id = sc.nextInt();
		}
		Restaurante[] selecionados = new Restaurante[1000];
		int qtd = 0;

		for(int i = 0; i < qtdIds; i++){
			int j = 0;
			boolean achou = false;

			while(j < colecao.getTamanho() && achou == false){
				if(colecao.getRestaurantes()[j].getId() == ids[i]){
					selecionados[qtd] = colecao.getRestaurantes()[j];
					qtd++;
					achou = true;
				}
				j++;
			}
		}

		long[] comp = {0};
		long[] mov = {0};
		long inicio = System.nanoTime();
		MergeSort.ordenacao(selecionados, qtd, comp, mov);
		long fim = System.nanoTime();
		long tempo = fim - inicio;

		FileWriter fw = new FileWriter("845833_mergesort.txt");
		fw.write("845833\t" + comp[0] + "\t" + mov[0] + "\t" + tempo);
		fw.close();

		for(int i = 0; i < qtd; i++){
			System.out.println(selecionados[i].imprimir());
		}

		sc.close();
	}
}

