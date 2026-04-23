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
