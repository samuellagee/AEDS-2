import java.util.Scanner;

class Restaurante{
	private int id;
	private String nome;

	public Restaurante(int id, String nome){
		this.id = id;
		this.nome = nome;
	}

	public int getId(){
		return id;
	}
	public String getNome(){
		return nome;
	}

	public void imprimir(){
		System.out.println(id + "," + nome);
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
		int resp = e1.getNome().compareTo(e2.getNome());
		return resp;
	}
}
public class TesteSelecao2{
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

