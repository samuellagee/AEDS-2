import java.util.Scanner;

public class Hora{
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
