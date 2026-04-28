public class TesteHora{
	public static void main(String[] args){
		Hora hora = Hora.parseHora("22:30");
		System.out.println(hora.formatar());
	}
}
