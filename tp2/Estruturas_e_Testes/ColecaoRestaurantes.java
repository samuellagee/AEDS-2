import java.util.Scanner;

public class ColecaoRestaurantes{
	private int tamanho;
	private Restaurante[] restaurantes;

	public ColecaoRestaurantes(){
		this.restaurantes = new Restaurante[502];
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

			if(sc.hasNextLine()){
				sc.nextLine();
			}
			while(sc.hasNextLine()){
				String linha = sc.nextLine();

				Restaurante r = Restaurante.parseRestaurante(linha);
				c.restaurantes[c.tamanho] = r;
				c.tamanho++;
			}
			sc.close();
		}catch(Exception e){
			System.out.println("Erro ao ler");
		}

		return c;
	}
}
