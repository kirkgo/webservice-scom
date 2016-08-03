Valor da Construção - Webservice
================================

Para realizar a integração foi criado um webservice onde será feito o post do arquivo CSV com as informações dos produtos.

O nome deste arquivo será formado pelo "CNPJ" da empresa + Data de Geração do Arquivo + Hora de Geração do Arquivo, separados por "_", no seguinte formato:

  - CNPJ : 12.123.123/0001-12 (serão utilizados apenas numeros); 
  - Data de Geração do Arquivo : 24/04/2013 (será utilizada em formato YYYYMMDD)
  - Hora de Geração do Arquivo : 16:30 (será utilizada em formato HHMM)

Exemplo: <b>12123123000112_20132404_1630.csv</b>

O arquivo deverá seguir o seguinte padrão:

- Separação da colunas por ponto e virgula ";"
- O valor das campos dentro da coluna entre aspas
- Disposição das colunas:
  - Coluna 1 = Codigo do fabricante
  - Coluna 2 = Codigo de barras (opcional)
  - Coluna 3 = Codigo do sistema de origem (opcional)
  - Coluna 4 = Descricao
  - Coluna 5 = Marca
  - Coluna 6 = Unidade
  - Coluna 7 = Preço a vista
  - Coluna 8 = Estoque disponível (opcional)
  - Coluna 9 = CNPJ da loja (opcional)

O envio do arquivo deve ser feito utilizando o método <b>HTTP POST</b> na url passando os seguintes parâmetros:

- <b>products_file (arquivo CSV)</b>

A autenticação é feita através do <b>HTTP BASIC AUTHENTICATION</b>, passando como parâmetros o usuário e senha fornecidos pela equipe do valor da construção.

Após executar o envio do arquivo o sistema retornará um XML com o status do envio. 

Se o arquivo for sincronizado com sucesso ele retornará nesse XML a lista de produtos, o status de cada produto (cadastrado ou não) e os possíveis erros em cada um dos produtos da lista.

Se houver algum erro o sistema retornará uma mensagem com o erro no retorno XML.

Dentro da pasta <b>"EXEMPLO/"</b> encontra-se o arquivo <b>12123123000112_20132404_1630.csv</b> que deve ser usado para testes.

Uma versão em java do script para integração também está disponível dentro da pasta <b>"Java/"</b>.