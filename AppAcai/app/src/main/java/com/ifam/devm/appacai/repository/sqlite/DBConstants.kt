package com.ifam.devm.appacai.repository.sqlite

const val DATABASE_VERSION = 2
const val DATABASE_NAME = "dbAppAcai"

//NOME DAS TABELAS
const val TABLE_USUARIO = "usuario"
const val TABLE_PRODUTO = "produto"
const val TABLE_PAGAMENTO = "pagamento"
const val TABLE_VENDA = "relVenda"
const val TABLE_PRODUTO_VENDA = "produtoVenda"
const val TABLE_FUNCIONARIO = "funcionario"

//NOME DAS COLUNAS
const val COLUMN_ID = "id"
const val COLUMN_ID_VENDA = "idVenda"
const val COLUMN_NOME = "nome"
const val COLUMN_DESCRICAO = "descricao"
const val COLUMN_NOME_EMPRESA = "nomeEmpresa"
const val COLUMN_EMAIL = "email"
const val COLUMN_SENHA = "senha"
const val COLUMN_CHAVEPIX = "chavePix"
const val COLUMN_TIPO = "tipo"
const val COLUMN_VALOR = "valor"
const val COLUMN_FREQ_VENDAS = "freqVendas"
const val COLUMN_AVALIACAO = "avaliacao"
const val COLUMN_QTD_VOTOS = "qtdVotos"
const val COLUMN_FOTO = "foto"
const val COLUMN_VALOR_DIA = "valorDia"
const val COLUMN_FUNC_SEMANA = "funcSemana"
const val COLUMN_VALOR_SEMANA = "valorSemana"
const val COLUMN_AVAL_PRODUTO = "mediaAvalProd"
const val COLUMN_PROD_MAIS_VENDIDOS = "maisVendidos"
const val COLUMN_DATA_RELATORIO = "dataRelatorio"
const val COLUMN_DATA_PAGAMENTO = "dataPagamento"

const val  COLLUMN_ID_FUNCIONARIO = "id_funcionario"
const val  COLUMN_NOME_FUNCIONARIO = "nome_funcionaro"
const val  COLUMN_EMAIL_FUNCIONARIO = "email_funcionario"
const val   COLUMN_TELEFONE_FUNCIONARIO = "telefone_funcionario"
const val COLUMN_META_VENDA = "meta"
const val COLUMN_TOTAL_VENDA = "total_vendas"
const val  COLUMN_SENHA_FUNCIONARIO = "senha_funcionario"
const val COLUMN_CPF_FUNCIONARIO = "cpf"

//Preferences
const val PRIVATE_MODE = 0
const val PREF_DATA_NAME = "dadosAdmin"
const val PREF_DATA_FUNC = "dadosFunc"