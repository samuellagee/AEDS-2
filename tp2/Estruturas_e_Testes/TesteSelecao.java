import java.util.Scanner;

class Data{
	private int dia;
	private int mes;
	private int ano;

	public Data(int dia, int mes, int ano){
		this.dia = dia;
		this.mes = mes;
		this.ano = ano;
	}

	public int getDia(){
		return dia;
	}
	public int getMes(){
		return mes;
	}
	public int getAno(){
		return ano;
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

	public int getHora(){
		return hora;
	}
	public int getMin(){
		return min;
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

import java.util.Scanner;

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

	public Restaurante(int id, String nome, String cidade, int capacidade,  double avaliacao, String[] tiposCozinha, int faixaPreco, Hora horarioAbertura, Hora horarioFechamento, Data dataAbertura, boolean aberto){
		this.dataAbertura = dataAbertura;
		this.horarioAbertura = horarioAbertura;
		this.horarioFechamento = horarioFechamento;
		this.aberto = aberto;
		this.id = id;
		this.nome = nome;
		this.cidade = cidade;
		this.capacidade = capacidade;
		this.avaliacao = avaliacao;
		this.tiposCozinha = tiposCozinha;
		this.faixaPreco = faixaPreco;

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
			lista[i] = sc.next();
			i++;
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

		String lerTipos = sc.next();
		String[] tipos = separarTipos(lerTipos);

		String lerFaixa = sc.next();
		int faixa = converterFaixa(lerFaixa);

		String lerHora = sc.next();
		String[] horas = separarHora(lerHora);
		Hora abertura = Hora.parseHora(horas[0]);
		Hora fechamento = Hora.parseHora(horas[1]);

		String lerData = sc.next();
		Data data = Data.parseData(lerData);

		String lerAberto = sc.next();
		boolean aberto = (lerAberto.charAt(0) == 't');
		sc.close();

		return new Restaurante(id, nome, cidade, capacidade, avaliacao, tipos, faixa, abertura, fechamento, data, aberto);
	}

	public String formatar(){
		String tiposC = "";

		for(int i = 0; i < tiposCozinha.length && tiposCozinha[i] != null; i++){
			tiposC = tiposC + tiposCozinha[i];
			if(i < tiposCozinha.length - 1 && tiposCozinha[i + 1] != null){
				tiposC = tiposC + ";";
			}
		}

		String funcionando;
		if(aberto) funcionando = "Sim";
		else funcionando = "Nao";

		String faixaP = "";
		for(int i = 0; i < faixaPreco; i++){
			faixaP = faixaP + '$';
		}

		return String.format("%d,%s,%s,%d,%.2f,%s,%s,%s-%s,%s,%s", id, nome, cidade, capacidade, avaliacao, tiposC, faixaP, horarioAbertura.formatar(), horarioFechamento.formatar(), dataAbertura.formatar(), funcionando);
	}
}


class Selecao{
	public static void ordenar(Restaurante[] array, int n){
		for(int i = 0; i < n - 1; i++){
			int menor = i;
			for(int j = i  + 1; j < n; j++){
				if(compareTo(array[j], array[menor]) < 0){
					menor = j;
				}
			}
			swap(array, i, menor);
		}
	}

	private static void swap(Restaurante[] array, int i, int j){
		Restaurante temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	private static int compareTo(Restaurante e1, Restaurante e2){
		int resp;
		if(e1.getId() < e2.getId()) resp = -1;
		else if(e1.getId() == e2.getId()) resp = 0;
		else resp = 1;
		return resp;
	}
}
public class TesteSelecao{
	public static void main(String[] args){
		int n = 4;
		Restaurante[] array = {
			new Restaurante(5, "R"),
			new Restaurante(3, "F"),
			new Restaurante(10, "B"),
			new Restaurante(7, "A"),
		};

		Selecao.ordenar(array, n);
		for(int i = 0; i < n; i++){
			array[i].imprimir();
		}
	}
}

