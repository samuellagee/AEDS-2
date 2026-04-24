import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;

class Data {
    private int dia, mes, ano;

    public Data(int dia, int mes, int ano) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    public static Data parseData(String d) {
        // d: "YYYY-MM-DD"
        int ano = ((d.charAt(0)-48)*1000) + ((d.charAt(1)-48)*100) + ((d.charAt(2)-48)*10) + (d.charAt(3)-48);
        int mes = ((d.charAt(5)-48)*10) + (d.charAt(6)-48);
        int dia = ((d.charAt(8)-48)*10) + (d.charAt(9)-48);
        return new Data(dia, mes, ano);
    }

    public String formatar() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}

class Hora {
    private int hora, min;

    public Hora(int hora, int min) {
        this.hora = hora;
        this.min = min;
    }

    public static Hora parseHora(String h) {
        // h: "HH:MM"
        int hora = ((h.charAt(0)-48)*10) + (h.charAt(1)-48);
        int min = ((h.charAt(3)-48)*10) + (h.charAt(4)-48);
        return new Hora(hora, min);
    }

    public String formatar() {
        return String.format("%02d:%02d", hora, min);
    }
}

class Restaurante {
    private Data dataAbertura;
    private Hora horarioAbertura, horarioFechamento;
    private boolean aberto;
    private int id, capacidade, faixaPreco;
    private String nome, cidade;
    private double avaliacao;
    private String[] tiposCozinha;

    public Restaurante(int id, String nome, String cidade, int capacidade, double avaliacao,
                       String[] tiposCozinha, int faixaPreco, Hora hA, Hora hF, Data dA, boolean aberto) {
        this.id = id; this.nome = nome; this.cidade = cidade;
        this.capacidade = capacidade; this.avaliacao = avaliacao;
        this.tiposCozinha = tiposCozinha; this.faixaPreco = faixaPreco;
        this.horarioAbertura = hA; this.horarioFechamento = hF;
        this.dataAbertura = dA; this.aberto = aberto;
    }

    public int getId() { return id; }
    public String getCidade() { return cidade; }

    // Conversor manual de String para int (sem Integer.parseInt)
    public static int stringToInt(String s) {
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') res = res * 10 + (c - '0');
        }
        return res;
    }

    // Conversor manual de String para double (sem Double.parseDouble)
    public static double stringToDouble(String s) {
        double res = 0;
        int ponto = s.indexOf('.');
        if (ponto == -1) return (double) stringToInt(s);
        
        res = stringToInt(s.substring(0, ponto));
        double decimal = 0;
        double div = 10;
        for (int i = ponto + 1; i < s.length(); i++) {
            decimal += (s.charAt(i) - '0') / div;
            div *= 10;
        }
        return res + decimal;
    }

    public static Restaurante parseRestaurante(String linha) {
        int inicio = 0, fim;

        fim = linha.indexOf(',', inicio);
        int id = stringToInt(linha.substring(inicio, fim)); inicio = fim + 1;

        fim = linha.indexOf(',', inicio);
        String nome = linha.substring(inicio, fim); inicio = fim + 1;

        fim = linha.indexOf(',', inicio);
        String cidade = linha.substring(inicio, fim); inicio = fim + 1;

        fim = linha.indexOf(',', inicio);
        int capacidade = stringToInt(linha.substring(inicio, fim)); inicio = fim + 1;

        fim = linha.indexOf(',', inicio);
        double avaliacao = stringToDouble(linha.substring(inicio, fim)); inicio = fim + 1;

        fim = linha.indexOf(',', inicio);
        String tiposStr = linha.substring(inicio, fim);
        String[] tipos = new String[5];
        int tPos = 0, tIni = 0, tFim = tiposStr.indexOf(';');
        while (tFim != -1) {
            tipos[tPos++] = tiposStr.substring(tIni, tFim);
            tIni = tFim + 1; tFim = tiposStr.indexOf(';', tIni);
        }
        tipos[tPos] = tiposStr.substring(tIni);
        inicio = fim + 1;

        fim = linha.indexOf(',', inicio);
        String faixaStr = linha.substring(inicio, fim);
        int faixa = 0;
        for (int i = 0; i < faixaStr.length(); i++) if (faixaStr.charAt(i) == '$') faixa++;
        inicio = fim + 1;

        fim = linha.indexOf(',', inicio);
        String horas = linha.substring(inicio, fim);
        int sep = horas.indexOf('-');
        Hora hA = Hora.parseHora(horas.substring(0, sep));
        Hora hF = Hora.parseHora(horas.substring(sep + 1));
        inicio = fim + 1;

        fim = linha.indexOf(',', inicio);
        Data dA = Data.parseData(linha.substring(inicio, fim));
        inicio = fim + 1;

        boolean aberto = linha.substring(inicio).charAt(0) == 't';

        return new Restaurante(id, nome, cidade, capacidade, avaliacao, tipos, faixa, hA, hF, dA, aberto);
    }

    public String imprimir() {
        String t = "";
        for (int i = 0; i < tiposCozinha.length && tiposCozinha[i] != null; i++) {
            t += tiposCozinha[i];
            if (i < tiposCozinha.length - 1 && tiposCozinha[i+1] != null) t += ",";
        }
        String f = "";
        for (int i = 0; i < faixaPreco; i++) f += "$";
        return "[" + id + " ## " + nome + " ## " + cidade + " ## " + capacidade + " ## " + avaliacao + " ## [" + t + "] ## " + f + " ## " + horarioAbertura.formatar() + "-" + horarioFechamento.formatar() + " ## " + dataAbertura.formatar() + " ## " + aberto + "]";
    }
}

class ColecaoRestaurantes {
    private int tamanho;
    private Restaurante[] restaurantes;

    public ColecaoRestaurantes() {
        this.restaurantes = new Restaurante[1000];
        this.tamanho = 0;
    }

    public int getTamanho() { return tamanho; }
    public Restaurante[] getRestaurantes() { return restaurantes; }

    public static ColecaoRestaurantes lerCsv() {
        ColecaoRestaurantes c = new ColecaoRestaurantes();
        try {
            Scanner sc = new Scanner(new File("/tmp/restaurantes.csv"));
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                c.restaurantes[c.tamanho++] = Restaurante.parseRestaurante(sc.nextLine());
            }
            sc.close();
        } catch (Exception e) {}
        return c;
    }
}

class Insercao {
    public static void ordenacao(Restaurante[] array, int n, long[] comp, long[] mov) {
        for (int i = 1; i < n; i++) {
            Restaurante tmp = array[i]; mov[0]++;
            int j = i - 1;
            while (j >= 0 && precisaMover(array[j], tmp, comp)) {
                array[j + 1] = array[j]; mov[0]++;
                j--;
            }
            array[j + 1] = tmp; mov[0]++;
        }
    }

    private static boolean precisaMover(Restaurante atual, Restaurante tmp, long[] comp) {
        comp[0]++;
        int res = atual.getCidade().compareTo(tmp.getCidade());
        if (res > 0) return true;
        if (res == 0 && atual.getId() > tmp.getId()) return true;
        return false;
    }
}

public class Teste{
    public static void main(String[] args) throws Exception {
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();
        Restaurante[] lista = colecao.getRestaurantes();
        Scanner sc = new Scanner(System.in);

        long[] comp = {0}, mov = {0};
        long inicio = System.nanoTime();
        Insercao.ordenacao(lista, colecao.getTamanho(), comp, mov);
        long fim = System.nanoTime();

        FileWriter fw = new FileWriter("845833_insercao.txt");
        fw.write("845833 " + comp[0] + " " + mov[0] + " " + (fim - inicio));
        fw.close();

        int num = sc.nextInt();
        while (num != -1) {
            System.out.println(lista[num].imprimir());
            num = sc.nextInt();
        }
        sc.close();
    }
}
