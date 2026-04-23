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

//colocar classe Retaurante

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

