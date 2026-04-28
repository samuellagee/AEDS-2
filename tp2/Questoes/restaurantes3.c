#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

typedef struct{
    int dia;
    int mes;
    int ano;
} Data;

Data parse_data(char* s){
    Data data;
    sscanf(s, "%d-%d-%d", &data.ano, &data.mes, &data.dia);
    return data;
}

void formatar_data(Data* data, char* buffer){
    sprintf(buffer, "%02d/%02d/%04d", data->dia, data->mes, data->ano);
}

typedef struct{
    int hora;
    int min;
} Hora;

Hora parse_hora(char* s){
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.min);
    return h;
}

void formatar_hora(Hora* h, char* buffer){
    sprintf(buffer, "%02d:%02d", h->hora, h->min);
}

void trim(char* str){
    int i = 0, j;

    while(str[i] == ' ') i++;

    if(i > 0){
        j = 0;
        while(str[i] != '\0'){
            str[j++] = str[i++];
        }
        str[j] = '\0';
    }

    int len = strlen(str);
    while(len > 0 && (str[len - 1] == ' ' || str[len - 1] == '\n' || str[len - 1] == '\r')){
        str[len - 1] = '\0';
        len--;
    }
}

typedef struct{
    int id;
    char nome[100];
    char cidade[100];
    int capacidade;
    double avaliacao;
    char tipos[5][50];
    int qtd_tipos;
    int faixa_preco;
    Hora abertura;
    Hora fechamento;
    Data data;
    int aberto;
} Restaurante;

int contar_faixa(char* s){
    int i = 0, count = 0;
    while(s[i] != '\0'){
        if(s[i] == '$') count++;
        i++;
    }
    return count;
}

void separar_tipos(char* s, Restaurante* r){
    int i = 0, j = 0, k = 0;
    while(s[i] != '\0'){
        if(s[i] == ';'){
            r->tipos[j][k] = '\0';
            j++;
            k = 0;
        }else{
            r->tipos[j][k++] = s[i];
        }
        i++;
    }
    r->tipos[j][k] = '\0';
    r->qtd_tipos = j + 1;
}

Restaurante* parse_restaurante(char* linha){
    Restaurante* r = (Restaurante*) malloc(sizeof(Restaurante));

    char tipos[200], faixa[50], horas[50], data[50], aberto[10];

    sscanf(linha, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^,],%[^,],%s",
           &r->id, r->nome, r->cidade, &r->capacidade, &r->avaliacao,
           tipos, faixa, horas, data, aberto);

    trim(r->nome);
    trim(r->cidade);

    separar_tipos(tipos, r);
    r->faixa_preco = contar_faixa(faixa);

    char h1[10], h2[10];
    sscanf(horas, "%[^-]-%s", h1, h2);

    r->abertura = parse_hora(h1);
    r->fechamento = parse_hora(h2);
    r->data = parse_data(data);

    if(strcmp(aberto, "true") == 0) r->aberto = 1;
    else r->aberto = 0;

    return r;
}

typedef struct{
    Restaurante* restaurantes[2000];
    int tamanho;
} Colecao_restaurantes;

Colecao_restaurantes* ler_csv(){
    FILE* f = fopen("/tmp/restaurantes.csv", "r");
    if(f == NULL){
        printf("Erro");
        return NULL;
    }

    Colecao_restaurantes* c = (Colecao_restaurantes*) malloc(sizeof(Colecao_restaurantes));
    c->tamanho = 0;

    char linha[500];
    fgets(linha, 500, f);

    while(fgets(linha, 500, f) != NULL){
        c->restaurantes[c->tamanho++] = parse_restaurante(linha);
    }

    fclose(f);
    return c;
}

int comparar_nome(Restaurante* r1, Restaurante* r2){
    return strcmp(r1->nome, r2->nome);
}

void swap(Restaurante* v[], int i, int j, long* mov){
    Restaurante* tmp = v[i];
    v[i] = v[j];
    v[j] = tmp;
    *mov += 3;
}

void selecao(Restaurante* v[], int n, long* comp, long* mov){
    for(int i = 0; i < n - 1; i++){
        int menor = i;
        for(int j = i + 1; j < n; j++){
            (*comp)++;
            if(comparar_nome(v[j], v[menor]) < 0){
                menor = j;
            }
        }
        if(menor != i){
            swap(v, i, menor, mov);
        }
    }
}

int pesquisa_binaria(Restaurante* v[], int n, char* chave, long* comp){
    int esq = 0, dir = n - 1, resp = 0;

    while(esq <= dir && resp == 0){
        int meio = (esq + dir) / 2;
        int cmp = strcmp(v[meio]->nome, chave);
        (*comp)++;

        if(cmp == 0){
            resp = 1;
        }else if(cmp < 0){
            esq = meio + 1;
        }else{
            dir = meio - 1;
        }
    }

    return resp;
}

int main(){
    Colecao_restaurantes* colecao = ler_csv();

    int ids[1000];
    int qtd_ids = 0;
    int id;

    scanf("%d", &id);
    while(id != -1){
        ids[qtd_ids++] = id;
        scanf("%d", &id);
    }

    char lixo[50];
    fgets(lixo, 50, stdin);

    Restaurante* selecionados[1000];
    int qtd = 0;

    for(int i = 0; i < qtd_ids; i++){
        int j = 0;
        int achou = 0;

        while(j < colecao->tamanho && achou == 0){
            if(colecao->restaurantes[j]->id == ids[i]){
                selecionados[qtd++] = colecao->restaurantes[j];
                achou = 1;
            }
            j++;
        }
    }

    long comp_ord = 0, mov = 0;
    selecao(selecionados, qtd, &comp_ord, &mov);

    char nome[200];
    long comp_busca = 0;

    clock_t inicio = clock();

    int continuar = 1;

    while(continuar == 1 && fgets(nome, 200, stdin) != NULL){

        int i = 0;
        while(nome[i] != '\0'){
            if(nome[i] == '\n'){
                nome[i] = '\0';
            }
            i++;
        }

        trim(nome);

        if(strcmp(nome, "FIM") == 0){
            continuar = 0;
        }
        else if(nome[0] != '\0'){
            int achou = pesquisa_binaria(selecionados, qtd, nome, &comp_busca);

            if(achou == 1){
                printf("SIM\n");
            }else{
                printf("NAO\n");
            }
        }
    }

    clock_t fim = clock();
    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC;

    FILE* log = fopen("845833_binaria.txt", "w");
    fprintf(log, "845833\t%ld\t%lf", comp_busca, tempo);
    fclose(log);

    return 0;
}
