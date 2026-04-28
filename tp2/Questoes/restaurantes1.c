#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct{
    int dia;
    int mes;
    int ano;
} Data;

typedef struct{
    int hora;
    int minuto;
} Hora;

typedef struct{
    int id;
    char nome[100];
    char cidade[100];
    int capacidade;
    double avaliacao;
    char tipos_cozinha[5][50];
    int qtd_tipos;
    int faixa_preco;
    Hora horario_abertura;
    Hora horario_fechamento;
    Data data_abertura;
    int aberto;
} Restaurante;

typedef struct{
    Restaurante* restaurantes[2000];
    int tamanho;
} Colecao_restaurantes;

Data parse_data(char* s){
    Data data;
    sscanf(s, "%d-%d-%d", &data.ano, &data.mes, &data.dia);
    return data;
}

void formatar_data(Data* data, char* buffer){
    sprintf(buffer, "%02d/%02d/%04d", data->dia, data->mes, data->ano);
}

Hora parse_hora(char* s){
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

void formatar_hora(Hora* h, char* buffer){
    sprintf(buffer, "%02d:%02d", h->hora, h->minuto);
}

int contar_faixa(char* s){
    int i = 0;
    int count = 0;

    while(s[i] != '\0'){
        if(s[i] == '$'){
            count++;
        }
        i++;
    }

    return count;
}

void separar_tipos(char* s, Restaurante* r){
    int i = 0, j = 0, k = 0;

    while(1){
        if(s[i] == ';' || s[i] == '\0'){
            r->tipos_cozinha[j][k] = '\0';
            j++;
            k = 0;

            if(s[i] == '\0'){
                break;
            }
        }else{
            r->tipos_cozinha[j][k] = s[i];
            k++;
        }
        i++;
    }

    r->qtd_tipos = j;
}

Restaurante* parse_restaurante(char* linha){
    Restaurante* r = (Restaurante*) malloc(sizeof(Restaurante));

    char tipos[200];
    char faixa[10];
    char horas[50];
    char data_str[20];
    char aberto_str[10];

    sscanf(linha, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^,],%[^,],%s",
        &r->id,
        r->nome,
        r->cidade,
        &r->capacidade,
        &r->avaliacao,
        tipos,
        faixa,
        horas,
        data_str,
        aberto_str
    );

    separar_tipos(tipos, r);
    r->faixa_preco = contar_faixa(faixa);

    char abertura[10], fechamento[10];
    sscanf(horas, "%[^-]-%s", abertura, fechamento);

    r->horario_abertura = parse_hora(abertura);
    r->horario_fechamento = parse_hora(fechamento);
    r->data_abertura = parse_data(data_str);

    if(aberto_str[0] == 't'){
        r->aberto = 1;
    }else{
        r->aberto = 0;
    }

    return r;
}

void formatar_restaurante(Restaurante* r, char* buffer){
    char data[20];
    char h_abertura[10], h_fechamento[10];

    formatar_data(&r->data_abertura, data);
    formatar_hora(&r->horario_abertura, h_abertura);
    formatar_hora(&r->horario_fechamento, h_fechamento);

    char tipos[200];
    int pos = 0;

    for(int i = 0; i < r->qtd_tipos; i++){
        int j = 0;
        while(r->tipos_cozinha[i][j] != '\0'){
            tipos[pos++] = r->tipos_cozinha[i][j];
            j++;
        }

        if(i < r->qtd_tipos - 1){
            tipos[pos++] = ',';
        }
    }
    tipos[pos] = '\0';

    char faixa[10];
    int k = 0;
    for(int i = 0; i < r->faixa_preco; i++){
        faixa[k++] = '$';
    }
    faixa[k] = '\0';

    char aberto_str[6];
    if(r->aberto == 1){
        aberto_str[0] = 't';
        aberto_str[1] = 'r';
        aberto_str[2] = 'u';
        aberto_str[3] = 'e';
        aberto_str[4] = '\0';
    }else{
        aberto_str[0] = 'f';
        aberto_str[1] = 'a';
        aberto_str[2] = 'l';
        aberto_str[3] = 's';
        aberto_str[4] = 'e';
        aberto_str[5] = '\0';
    }

    sprintf(buffer, "[%d ## %s ## %s ## %d ## %.1lf ## [%s] ## %s ## %s-%s ## %s ## %s]",
        r->id,
        r->nome,
        r->cidade,
        r->capacidade,
        r->avaliacao,
        tipos,
        faixa,
        h_abertura,
        h_fechamento,
        data,
        aberto_str
    );
}

void ler_csv_colecao(Colecao_restaurantes* c, char* path){
    FILE* file = fopen(path, "r");
    if(file == NULL){
        printf("Erro");
        return;
    }
    char linha[500];
    fgets(linha, sizeof(linha), file);

    while(fgets(linha, sizeof(linha), file)){
        int i = 0;
        while(linha[i] != '\0'){
            if(linha[i] == '\n'){
                linha[i] = '\0';
            }
            i++;
        }

        c->restaurantes[c->tamanho++] = parse_restaurante(linha);
    }

    fclose(file);
}

Colecao_restaurantes* ler_csv(){
    Colecao_restaurantes* c = (Colecao_restaurantes*) malloc(sizeof(Colecao_restaurantes));
    c->tamanho = 0;
    ler_csv_colecao(c, "/tmp/restaurantes.csv");
    return c;
}

int main(){
    Colecao_restaurantes* colecao = ler_csv();
    int id;
    scanf("%d", &id);

    while(id != -1){
        for(int i = 0; i < colecao->tamanho; i++){
            if(colecao->restaurantes[i]->id == id){
                char buffer[500];
                formatar_restaurante(colecao->restaurantes[i], buffer);
                printf("%s\n", buffer);
            }
        }

        scanf("%d", &id);
    }

    return 0;
}
