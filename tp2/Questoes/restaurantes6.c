#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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

	separar_tipos(tipos, r);
	r->faixa_preco = contar_faixa(faixa);

	char h1[10], h2[10];
	sscanf(horas, "%[^-]-%s", h1, h2);

	r->abertura = parse_hora(h1);
	r->fechamento = parse_hora(h2);
	r->data = parse_data(data);

	if(aberto[0] == 't') r->aberto = 1;
	else r->aberto = 0;

	return r;
}

void formatar_restaurante(Restaurante* r, char* buffer){
	char data[20], h1[10], h2[10];

	formatar_data(&r->data, data);
	formatar_hora(&r->abertura, h1);
	formatar_hora(&r->fechamento, h2);

	int pos = 0;

	pos += sprintf(buffer + pos, "[%d ## %s ## %s ## %d ## %.1lf ## [",
			r->id, r->nome, r->cidade, r->capacidade, r->avaliacao);

	for(int i = 0; i < r->qtd_tipos; i++){
		pos += sprintf(buffer + pos, "%s", r->tipos[i]);
		if(i < r->qtd_tipos - 1){
			pos += sprintf(buffer + pos, ",");
		}
	}

	pos += sprintf(buffer + pos, "] ## ");

	for(int i = 0; i < r->faixa_preco; i++){
		pos += sprintf(buffer + pos, "$");
	}

	pos += sprintf(buffer + pos, " ## %s-%s ## %s ## %s]",
			h1, h2, data, r->aberto ? "true" : "false");
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

typedef struct{
	Restaurante* array[1000];
	int topo;
} Pilha;

void iniciar(Pilha* p){
	p->topo = 0;
}

void empilhar(Pilha* p, Restaurante* r){
	p->array[p->topo++] = r;
}

Restaurante* desempilhar(Pilha* p){
	return p->array[--p->topo];
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

	Pilha pilha;
	iniciar(&pilha);

	for(int i = 0; i < qtd_ids; i++){
		int j = 0;
		int achou = 0;

		while(j < colecao->tamanho && achou == 0){
			if(colecao->restaurantes[j]->id == ids[i]){
				empilhar(&pilha, colecao->restaurantes[j]);
				achou = 1;
			}
			j++;
		}
	}

	int n;
	scanf("%d", &n);

	for(int i = 0; i < n; i++){
		char comando[10];
		scanf("%s", comando);

		if(comando[0] == 'I'){
			int x;
			scanf("%d", &x);

			int j = 0;
			int achou = 0;

			while(j < colecao->tamanho && achou == 0){
				if(colecao->restaurantes[j]->id == x){
					empilhar(&pilha, colecao->restaurantes[j]);
					achou = 1;
				}
				j++;
			}
		}
		else if(comando[0] == 'R'){
			Restaurante* r = desempilhar(&pilha);
			printf("(R)%s\n", r->nome);
		}
	}

	char buffer[500];
	for(int i = pilha.topo - 1; i >= 0; i--){
		formatar_restaurante(pilha.array[i], buffer);
		printf("%s\n", buffer);
	}

	return 0;
}
