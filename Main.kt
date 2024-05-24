import java.io.File
import java.io.PrintWriter
import kotlin.random.Random

fun tamanhoTabuleiroValido(numLinhas: Int, numColunas: Int): Boolean {
    return (numLinhas == numColunas && (numColunas == 4 || numColunas == 5 || numColunas == 7 || numColunas == 8 || numColunas == 10))
}

fun processaCoordenadas(coordenadas: String, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {
    if (coordenadas[coordenadas.length - 1].toString().toIntOrNull() != null) { //verifica se a última posicao é um int
        return null
    }
    if (coordenadas[coordenadas.length - 2] != ',') {
        return null
    }
    if (coordenadas.length < 3 || coordenadas.length > 4) {
        return null
    }
    var linhas = 0
    if (coordenadas.length == 3) {
        linhas = coordenadas[0].toString().toInt()
    } else {
        linhas = (coordenadas[0].toString() + coordenadas[1].toString()).toInt()
    }
    val comparaTexto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val coordenadaLetra = coordenadas[coordenadas.length - 1]
    var count = 0
    do {
        if (coordenadaLetra == comparaTexto[count]) {

            if (linhas <= numLinhas && count < numColunas) {
                return Pair(linhas, count + 1)
            }
        }
        count++
    } while (count < numColunas)
    return null
}

fun criaLegendaHorizontal(numColunas: Int): String {
    var count = 1
    var letras = "A"
    val coluna = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    while (count < numColunas) {
        letras += " | " + coluna[count]
        count++
    }
    return letras
}

fun criaTerreno(numLinhas: Int, numColunas: Int): String {
    var terreno = "\n| ${criaLegendaHorizontal(numColunas)} |\n"
    var linha = 0
    while (linha < numLinhas) {
        var coluna = 0
        while (coluna < numColunas) {
            terreno += "|   "
            coluna++
        }
        linha++
        terreno += "| $linha\n"
    }
    return terreno
}

const val MENU_PRINCIPAL = 100
const val MENU_DEFINIR_TABULEIRO = 101
const val MENU_DEFINIR_NAVIOS = 102
const val MENU_JOGAR = 103
const val MENU_LER_FICHEIRO = 104
const val MENU_GRAVAR_FICHEIRO = 105
const val SAIR = 106
const val VOU_SAIR = 107
const val coordenadasInvalidas = "!!! Coordenadas invalidas, tente novamente"

fun main() {
    var menuActual = MENU_PRINCIPAL
    while (true) {
        menuActual = when (menuActual) {
            MENU_PRINCIPAL -> menuPrincipal()
            MENU_DEFINIR_TABULEIRO -> menuDefinirTabuleiro()
            MENU_DEFINIR_NAVIOS -> menuDefinirNavios()
            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro()
            SAIR -> return
            VOU_SAIR -> return
            else -> return
        }
    }
}

fun menuPrincipal(): Int {
    println("\n> > Batalha Naval < <\n")
    println("1 - Definir Tabuleiro e Navios")
    println("2 - Jogar")
    println("3 - Gravar")
    println("4 - Ler")
    println("9 - Vou sair")
    println("0 - Sair\n")
    println("9 - Vou sair\n")

    while (true) {
        val opcao = readLine()?.toIntOrNull()
        when (opcao) {
            -1 -> {
                return MENU_PRINCIPAL
            }
            0 -> {
                return SAIR
            }
            1 -> {
                return MENU_DEFINIR_TABULEIRO
            }
            2 -> {
                return MENU_JOGAR
            }
            3 -> {
                return MENU_GRAVAR_FICHEIRO
            }
            4 -> {
                return MENU_LER_FICHEIRO
            }
            9 -> {
                return VOU_SAIR
            }
            else -> {
                println("!!! Opcao invalida, tente novamente")
            }
        }
    }
}

fun menuDefinirTabuleiro(): Int {
    println("\n> > Batalha Naval < <\n")
    println("Defina o tamanho do tabuleiro:")
    do {
        println("Quantas linhas?")
        val linha = readLine()?.toIntOrNull()
        if (linha == -1) {
            return MENU_PRINCIPAL
        }
        if (linha == null) {
            println("!!! Número de linhas invalidas, tente novamente")
        } else {
            numLinhas = linha
            println("Quantas colunas?")
            val coluna = readLine()?.toIntOrNull()
            if (coluna == -1) {
                return MENU_PRINCIPAL
            }
            if (coluna == null) {
                println("!!! Número de colunas invalidas, tente novamente")
            } else {
                numColunas = coluna

                if (tamanhoTabuleiroValido(numLinhas, numColunas)) {
                    tabuleiroHumano = criaTabuleiroVazio(numLinhas, numColunas)
                    tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
                    tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas, numColunas)
                    tabuleiroPalpitesDoHumano = criaTabuleiroVazio(numLinhas, numColunas)
                    val tabuleiro = obtemMapa(tabuleiroHumano, true)
                    for (indice in 0..tabuleiro.size - 1) {
                        println(tabuleiro[indice])
                    }
                }
            }
        }
    } while (!tamanhoTabuleiroValido(numLinhas,numColunas))
    return MENU_DEFINIR_NAVIOS
}

fun menuDefinirNavios(): Int {
    val navios = calculaNumNavios(numLinhas, numColunas)
    val orientacaoInvalida = "!!! Orientacao invalida, tente novamente"
    for (tipoNavio in 0..navios.size - 1) {
        for (quantidadeNavios in 1..navios[tipoNavio]) {
            when (tipoNavio) {
                0 -> println("Insira as coordenadas de um submarino:")
                1 -> println("Insira as coordenadas de um contra-torpedeiro:")
                2 -> println("Insira as coordenadas de um navio-tanque:")
                3 -> println("Insira as coordenadas de um porta avioes:")
            }
            do {
                var inseriuNavio = false
                println("Coordenadas? (ex: 6,G)")
                val coordenadas = readLine()
                if (coordenadas?.toIntOrNull() == -1) {
                    return MENU_PRINCIPAL
                }
                if (coordenadas == null) {
                    println(coordenadasInvalidas)
                } else {
                    val coordenadasSalvas = processaCoordenadas(coordenadas, numLinhas, numColunas)
                    if (coordenadasSalvas != null) {
                        if (tipoNavio > 0) {
                            println("Insira a orientacao do navio:")

                            do{
                                println("Orientacao? (N, S, E, O)")
                                val orientacao = readLine()
                                var orientacaoInvalida = true
                                if (orientacao?.toIntOrNull() == -1) {
                                    return MENU_PRINCIPAL
                                }
                                if (orientacao == null) {
                                    println(orientacaoInvalida)
                                } else {
                                    if (orientacao == "N" || orientacao == "S" || orientacao == "E" || orientacao == "O") {
                                        orientacaoInvalida = false
                                        inseriuNavio = insereNavio(tabuleiroHumano,
                                            coordenadasSalvas.first,
                                            coordenadasSalvas.second,
                                            orientacao,
                                            tipoNavio + 1)
                                    }
                                    else {
                                        println(orientacaoInvalida)
                                    }
                                }
                            }while(orientacaoInvalida)
                        } else {
                            inseriuNavio = insereNavioSimples(tabuleiroHumano, coordenadasSalvas.first, coordenadasSalvas.second, tipoNavio + 1)
                        }

                    } else {
                        println(coordenadasInvalidas)
                    }
                }
            } while (!inseriuNavio)

            val tabuleiro = obtemMapa(tabuleiroHumano, true)
            for (indice in 0..tabuleiro.size - 1) {
                println(tabuleiro[indice])
            }
        }
    }
    println("Pretende ver o mapa gerado para o Computador? (S/N)")
    preencheTabuleiroComputador(tabuleiroComputador, calculaNumNavios(numLinhas, numColunas))
    val verTabuleiro = readLine()
    if(verTabuleiro == "S"){
        val tabuleiro = obtemMapa(tabuleiroComputador, true)
        for (indice in 0..tabuleiro.size - 1) {
            println(tabuleiro[indice])
        }
    }
    return MENU_PRINCIPAL
}
//TODO
fun menuJogar(): Int {
    while(true) {
        if (numColunas == -1) {
            println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
            return MENU_PRINCIPAL
        }
        val tabuleiro = obtemMapa(tabuleiroPalpitesDoHumano, false)
        for (indice in 0..tabuleiro.size - 1) {
            println(tabuleiro[indice])
        }
        println("Indique a posição que pretende atingir")
        do {
            println("Coordenadas? (ex: 6,G)")
            val coordenadas = readLine()
            if (coordenadas?.toIntOrNull() == -1) {
                return MENU_PRINCIPAL
            }
            if (coordenadas == null) {
                println(coordenadasInvalidas)
            } else {
                val tiroHumano = processaCoordenadas(coordenadas, numLinhas, numColunas)
                if (tiroHumano != null) {
                    val mensagem = lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, tiroHumano)
                    val letraAtual = tabuleiroPalpitesDoHumano[tiroHumano.first - 1][tiroHumano.second - 1]
                    if (letraAtual != null && letraAtual.isDigit() && navioCompleto(tabuleiroPalpitesDoHumano, tiroHumano.first, tiroHumano.second)) {
                        println(">>> HUMANO >>>$mensagem Navio ao fundo!")
                    } else {
                        println(">>> HUMANO >>>$mensagem")
                    }
                } else {
                    println(coordenadasInvalidas)
                }
            }
        } while (coordenadas == null)
        if (venceu(tabuleiroPalpitesDoHumano)) {
            println("PARABENS! Venceu o jogo!")
            println("Prima enter para voltar ao menu principal")
            readLine()
            return MENU_PRINCIPAL
        }
        val tiroComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
        println("Computador lancou tiro para posicao (${tiroComputador.first}, ${tiroComputador.second})")
        val letraAtual = tabuleiroComputador[tiroComputador.first - 1][tiroComputador.second - 1]
        val message = when (letraAtual) {
            '1' -> "Tiro num submarino."
            '2' -> "Tiro num contra-torpedeiro."
            '3' -> "Tiro num navio-tanque."
            '4' -> "Tiro num porta avioes."
            else -> "Agua."
        }
        println(">>> COMPUTADOR >>>$message")
        if (venceu(tabuleiroPalpitesDoComputador)) {
            println("OPS! O computador venceu o jogo!")
            println("Prima enter para voltar ao menu principal")
            readLine()
            return MENU_PRINCIPAL
        }
        println("Prima enter para continuar")
        readLine()
    }
}
//TODO
fun menuLerFicheiro (): Int {

    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeFicheiro = readln()
    tabuleiroHumano = lerJogo(nomeFicheiro, 1)
    tabuleiroPalpitesDoHumano = lerJogo(nomeFicheiro, 2)
    tabuleiroComputador = lerJogo(nomeFicheiro, 3)
    tabuleiroPalpitesDoComputador = lerJogo(nomeFicheiro, 4)
    println("Tabuleiro ${tabuleiroHumano.size}x${tabuleiroHumano.size} lido com sucesso")
    val tabuleiro = obtemMapa(tabuleiroHumano, true)
    for (indice in 0..tabuleiro.size - 1) {
        println(tabuleiro[indice])
    }
    return MENU_PRINCIPAL
}
//TODO
fun menuGravarFicheiro(): Int {
    if (numColunas == -1) {
        println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
        return MENU_PRINCIPAL
    }
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeFicheiro = readln()
    gravarJogo(nomeFicheiro, tabuleiroHumano, tabuleiroPalpitesDoHumano, tabuleiroComputador, tabuleiroPalpitesDoComputador)
    println("Tabuleiro ${tabuleiroHumano.size}x${tabuleiroHumano.size} gravado com sucesso")

    return MENU_PRINCIPAL
}
//TODO: SEGUNDA PARTE

var numLinhas = -1
var numColunas = -1
var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()

fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {
    when (numLinhas) {
        4 -> return arrayOf(2, 0, 0, 0) //submarini, contra-torpedeiro, navios-tanque, porta navios
        5 -> return arrayOf(1, 1, 1, 0)
        7 -> return arrayOf(2, 1, 1, 1)
        8 -> return arrayOf(2, 2, 1, 1)
        10 -> return arrayOf(3, 2, 1, 1)
    }
    return emptyArray()
}

fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>> {
    return Array(numLinhas) { Array(numColunas) { null } }
}

fun coordenadaContida(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    if (linha == 1 && linha <= tabuleiro.size && coluna >= 1 && coluna <= tabuleiro[linha - 1].size) {

        return true
    }
    return false
}

fun limparCoordenadasVazias(pares: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    var paresDiferentesDeZero = emptyArray<Pair<Int, Int>>()
    for (indice in 0..pares.size - 1) {
        if (pares[indice] != Pair(0, 0)) {
            paresDiferentesDeZero += pares[indice]
        }
    }
    return paresDiferentesDeZero
}

fun juntarCoordenadas(primeiroPar: Array<Pair<Int, Int>>, segundoPar: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    val juncao = primeiroPar.size + segundoPar.size
    val coordenadasJuntas = Array(juncao) { Pair(0, 0) }

    var indice = 0
    for (coordenada in primeiroPar) {
        coordenadasJuntas[indice] = coordenada
        indice++
    }

    for (coordenada in segundoPar) {
        coordenadasJuntas[indice] = coordenada
        indice++
    }
    return coordenadasJuntas
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    var pares = emptyArray<Pair<Int, Int>>()
    when (orientacao) {
        "E" -> {
            val valor = coluna + (dimensao - 1)
            if (valor <= tabuleiro.size) {
                for (indice in coluna..valor) {
                    if (coordenadaContida(tabuleiro, linha, indice)) {
                        pares += Pair(linha, indice)
                    }
                }
            }
        }

        "O" -> {
            val valor = coluna - (dimensao - 1)
            if (valor >= 1) {
                for (indice in valor..coluna) {
                    if (coordenadaContida(tabuleiro, linha, indice)) {
                        pares += Pair(linha, indice)
                    }
                }
            }
        }

        "S" -> {
            val valor = linha + (dimensao - 1)
            if (valor <= tabuleiro.size) {
                for (indice in linha..valor) {
                    if (coordenadaContida(tabuleiro, indice, coluna)) {
                        pares += Pair(indice, coluna)
                    }
                }
            }
        }

        "N" -> {
            val valor = linha - (dimensao - 1)
            if (valor >= 1) {
                for (indice in valor..linha) {
                    if (coordenadaContida(tabuleiro, indice, coluna)) {
                        pares += Pair(indice, coluna)
                    }
                }
            }
        }
    }
    return pares
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    var fronteira = emptyArray<Pair<Int, Int>>()
    var dimensaoFinal = 0
    when (orientacao) {
        "E" -> {
            dimensaoFinal = coluna + dimensao
            val linhaBaixo = linha + 1
            for (indice in coluna - 1..dimensaoFinal) {
                if (coordenadaContida(tabuleiro, linhaBaixo, indice)) {
                    fronteira += Pair(linhaBaixo, indice)
                }
            }
            val linhaCima = linha - 1
            for (indice in coluna - 1..dimensaoFinal) {
                if (coordenadaContida(tabuleiro, linhaCima, indice)) {
                    fronteira += Pair(linhaCima, indice)
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna - 1)) {
                fronteira += Pair(linha, coluna - 1)
            }
            if (coordenadaContida(tabuleiro, linha, dimensaoFinal)) {
                fronteira += Pair(linha, dimensaoFinal)
            }
        }

        "O" -> {
            dimensaoFinal = coluna - dimensao
            val linhaBaixo = linha + 1
            for (indice in dimensaoFinal..coluna + 1) {
                if (coordenadaContida(tabuleiro, linhaBaixo, indice)) {
                    fronteira += Pair(linhaBaixo, indice)
                }
            }
            val linhaCima = linha - 1
            for (indice in dimensaoFinal..coluna + 1) {
                if (coordenadaContida(tabuleiro, linhaCima, indice)) {
                    fronteira += Pair(linhaCima, indice)
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna + 1)) {
                fronteira += Pair(linha, coluna + 1)
            }
            if (coordenadaContida(tabuleiro, linha, dimensaoFinal)) {
                fronteira += Pair(linha, dimensaoFinal)
            }
        }

        "S" -> {
            dimensaoFinal = linha + dimensao
            val colunaEsquerda = coluna - 1
            for (indice in linha - 1..dimensaoFinal) {
                if (coordenadaContida(tabuleiro, indice, colunaEsquerda)) {
                    fronteira += Pair(indice, colunaEsquerda)
                }
            }
            val colunaDireita = coluna + 1
            for (indice in linha - 1..dimensaoFinal) {
                if (coordenadaContida(tabuleiro, indice, colunaDireita)) {
                    fronteira += Pair(indice, colunaDireita)
                }
            }
            if (coordenadaContida(tabuleiro, linha - 1, coluna)) {
                fronteira += Pair(linha - 1, coluna)
            }
            if (coordenadaContida(tabuleiro, dimensaoFinal, coluna)) {
                fronteira += Pair(dimensaoFinal, coluna)
            }
        }

        "N" -> {
            dimensaoFinal = linha - dimensao
            val colunaEsquerda = coluna - 1
            for (indice in dimensaoFinal..linha + 1) {
                if (coordenadaContida(tabuleiro, indice, colunaEsquerda)) {
                    fronteira += Pair(indice, colunaEsquerda)
                }
            }
            val colunaDireita = coluna + 1
            for (indice in dimensaoFinal..linha + 1) {
                if (coordenadaContida(tabuleiro, indice, colunaDireita)) {
                    fronteira += Pair(indice, colunaDireita)
                }
            }
            if (coordenadaContida(tabuleiro, linha + 1, coluna)) {
                fronteira += Pair(linha + 1, coluna)
            }
            if (coordenadaContida(tabuleiro, dimensaoFinal, coluna)) {
                fronteira += Pair(dimensaoFinal, coluna)
            }
        }
    }
    return fronteira
}
fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>): Boolean {
    for ((linha, coluna) in coordenadas) {
        if (!coordenadaContida(tabuleiro, linha, coluna) || tabuleiro[linha - 1][coluna - 1] != null) {
            return false
        }
    }
    return true
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, "E", dimensao)
    if (coordenadasNavio.size == 0) {
        return false
    }
    val coordenadasFronteira = gerarCoordenadasFronteira(tabuleiro, linha, coluna, "E", dimensao)
    val juncaoCoordenadas = juntarCoordenadas(coordenadasNavio, coordenadasFronteira)
    if (estaLivre(tabuleiro, juncaoCoordenadas)) {
        for (indice in 0..coordenadasNavio.size - 1) {
            tabuleiro[coordenadasNavio[indice].first - 1][coordenadasNavio[indice].second - 1] = dimensao.toString().first()
        }
        return true
    }
    return false
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean {
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)
    if (coordenadasNavio.size == 0) {
        return false
    }
    val coordenadasFronteira = gerarCoordenadasFronteira(tabuleiro, linha, coluna, orientacao, dimensao)
    val juncaoCoordenadas = juntarCoordenadas(coordenadasNavio, coordenadasFronteira)
    if (estaLivre(tabuleiro, juncaoCoordenadas)) {
        for (indice in 0..coordenadasNavio.size - 1) {
            tabuleiro[coordenadasNavio[indice].first - 1][coordenadasNavio[indice].second - 1] = dimensao.toString().first()
        }
        return true
    }
    return false
}

fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, navios: Array<Int>) {
    for (tipoNavio in 0..navios.size - 1) {
        for (quantidadeNavios in 1..navios[tipoNavio]) {
            do {
                // + 1 porque o ultimo numero e exclusivo
                val coordenada = pegaCoordenadaAleatoria(tabuleiro)
                val orientacaoAleatoria = arrayOf("N", "S", "E", "O").random()
                // + 1 porque o tipoNavio começa em zero
                val inseriuNavio = insereNavio(tabuleiro, coordenada.first, coordenada.second, orientacaoAleatoria, tipoNavio + 1)
            } while (!inseriuNavio)
        }
    }
}

fun pegaCoordenadaAleatoria(tabuleiro: Array<Array<Char?>>): Pair<Int, Int> {
    while (true) {
        val linhaAleatoria = Random.nextInt(1, tabuleiro.size + 1)
        val colunaAleatoria = Random.nextInt(1, tabuleiro.size + 1)
        if (tabuleiro[linhaAleatoria - 1][colunaAleatoria - 1] == null) {
            return Pair(linhaAleatoria, colunaAleatoria)
        }
    }
}

fun navioCompleto(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    val acessoCoordenada = tabuleiro[linha - 1][coluna - 1]
    when (acessoCoordenada) {
        '1' -> {
            return true
        }

        '2' -> {
            val guardaPares = arrayOf(Pair(linha, coluna - 1), Pair(linha, coluna + 1),
                Pair(linha - 1, coluna), Pair(linha + 1, coluna))
            for (indice in 0..guardaPares.size - 1) {
                val linhaAtual = guardaPares[indice].first
                val colunaAtual = guardaPares[indice].second
                if (coordenadaContida(tabuleiro, linhaAtual, colunaAtual) && tabuleiro[linhaAtual - 1][colunaAtual - 1] == '2') {
                    return true
                }
            }
        }

        '3' -> {
            val guardaPares = arrayOf(Pair(linha, coluna - 2), Pair(linha, coluna - 1),
                Pair(linha, coluna + 2), Pair(linha, coluna + 1),
                Pair(linha - 2, coluna), Pair(linha - 1, coluna),
                Pair(linha + 2, coluna), Pair(linha + 1, coluna))
            var contador = 0
            for (indice in 0..guardaPares.size - 1) {
                val linhaAtual = guardaPares[indice].first
                val colunaAtual = guardaPares[indice].second
                if (coordenadaContida(tabuleiro, linhaAtual, colunaAtual) && tabuleiro[linhaAtual - 1][colunaAtual - 1] == '3') {
                    contador++
                }
            }
            return contador == tabuleiro[linha - 1][coluna - 1]!!.digitToInt() - 1
        }

        '4' -> {
            val guardaPares = arrayOf(Pair(linha, coluna - 3), Pair(linha, coluna + 3),
                Pair(linha - 3, coluna), Pair(linha + 3, coluna),
                Pair(linha, coluna - 2), Pair(linha, coluna - 1),
                Pair(linha, coluna + 2), Pair(linha, coluna + 1),
                Pair(linha - 2, coluna), Pair(linha - 1, coluna),
                Pair(linha + 2, coluna), Pair(linha + 1, coluna))
            var contador = 0
            for (indice in 0..guardaPares.size - 1) {
                val linhaAtual = guardaPares[indice].first
                val colunaAtual = guardaPares[indice].second
                if (coordenadaContida(tabuleiro, linhaAtual, colunaAtual) && tabuleiro[linhaAtual - 1][colunaAtual - 1] == '4') {
                    contador++
                }
            }
            return contador == tabuleiro[linha - 1][coluna - 1]!!.digitToInt() - 1
        }
    }
    return false
}

fun obtemMapa(tabuleiro: Array<Array<Char?>>, tabuleiroReal: Boolean): Array<String> {
    val linhasTabuleiro = Array(tabuleiro.size + 1) { "" }
    val comecoLinha = "| "
    val meioLinha = " | "
    linhasTabuleiro[0] = "| ${criaLegendaHorizontal(tabuleiro.size)} |"
    // Comeca com um porque a primeira linha e a da legenda horizontal
    var contaLinha = 1
    if (tabuleiroReal) {
        for (linha in 0..tabuleiro.size - 1) {
            var linhaAtual = comecoLinha
            for (coluna in 0..tabuleiro[linha].size - 1) {
                val letraAtual = tabuleiro[linha][coluna]
                if (letraAtual == null) {
                    linhaAtual += "~ | "
                } else {
                    linhaAtual += "${tabuleiro[linha][coluna]}$meioLinha"
                }
            }
            linhaAtual += "${linha + 1}"
            linhasTabuleiro[contaLinha] = linhaAtual
            contaLinha++
        }
    } else {
        for (linha in 0..tabuleiro.size - 1) {
            var linhaAtual = comecoLinha
            for (coluna in 0..tabuleiro[linha].size - 1) {
                val letraAtual = tabuleiro[linha][coluna]
                if (letraAtual == null) {
                    linhaAtual += "? | "
                } else if (letraAtual.toString().toIntOrNull() != null) {
                    if (navioCompleto(tabuleiro, linha + 1, coluna + 1)) {
                        linhaAtual += "${tabuleiro[linha][coluna]}$meioLinha"
                    } else {
                        when (letraAtual) {
                            '2' -> {
                                linhaAtual += "\u2082 | "
                            }

                            '3' -> {
                                linhaAtual += "\u2083 | "
                            }

                            '4' -> {
                                linhaAtual += "\u2084 | "
                            }
                        }
                    }
                } else {
                    linhaAtual += "${tabuleiro[linha][coluna]} | "
                }
            }
            linhaAtual += "${linha + 1}"
            linhasTabuleiro[contaLinha] = linhaAtual
            contaLinha++
        }
    }

    return linhasTabuleiro
}

fun lancarTiro(tabuleiroReal: Array<Array<Char?>>, tabuleiroPalpites: Array<Array<Char?>>, coordenadas: Pair<Int, Int>): String {
    val linhaAtual = coordenadas.first - 1
    val colunaAtual = coordenadas.second - 1
    val letraAtual = tabuleiroReal[linhaAtual][colunaAtual]
    tabuleiroPalpites[linhaAtual][colunaAtual] = letraAtual
    when (letraAtual) {
        '1' -> return "Tiro num submarino."
        '2' -> return "Tiro num contra-torpedeiro."
        '3' -> return "Tiro num navio-tanque."
        '4' -> return "Tiro num porta avioes."
    }
    tabuleiroPalpites[linhaAtual][colunaAtual] = 'X'
    return "Agua."
}

fun geraTiroComputador(tabuleiroPalpites: Array<Array<Char?>>): Pair<Int, Int> {
    while (true) {
        val linhaAleatoria = Random.nextInt(0, tabuleiroPalpites.size)
        val colunaAleatoria = Random.nextInt(0, tabuleiroPalpites.size)
        if (tabuleiroPalpites[linhaAleatoria][colunaAleatoria] == null) {
            return Pair(linhaAleatoria + 1, colunaAleatoria + 1)
        }
    }
}

fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int {
    var contarNavios = 0
    for (linha in 0..tabuleiro.size - 1) {
        for (coluna in 0..tabuleiro[linha].size - 1) {
            val letraAtual = tabuleiro[linha][coluna]
            if (letraAtual == dimensao.toString().first() && letraAtual.isDigit()) {
                if (navioCompleto(tabuleiro, linha + 1, coluna + 1)) {
                    contarNavios++
                }
            }
        }
    }
    return contarNavios / dimensao
}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {
    val navios = arrayOf(2, 0, 0, 0)
    for (tipoNavio in 0..navios.size - 1) {
        for (quantidadeNavios in 1..navios[tipoNavio]) {
            val naviosDeDimensao = contarNaviosDeDimensao(tabuleiro, tipoNavio + 1)
            if (navios[tipoNavio] != naviosDeDimensao) {
                return false
            }
        }
    }
    return true
}

fun lerJogo(nomeFicheiro: String, tipoTabuleiro: Int): Array<Array<Char?>> {
    val linhasArquivo = File(nomeFicheiro).readLines()
    val tamanhoTabuleiro = linhasArquivo[0].split(',').first().toInt()
    numLinhas = tamanhoTabuleiro
    numColunas = tamanhoTabuleiro
    val linhaPrimeiroTabuleiro = 4
    val espacoEntreTabuleiros = 3
    val tabuleiro = criaTabuleiroVazio(numLinhas, numColunas)
    // Calculo para chegar no começo de cada tabuleiro
    val linhaInicio = if (tipoTabuleiro > 0) {
        linhaPrimeiroTabuleiro + (tamanhoTabuleiro * (tipoTabuleiro - 1)) + (espacoEntreTabuleiros * (tipoTabuleiro - 1))
    } else {
        linhaPrimeiroTabuleiro
    }
    for (numeroLinhas in 0..tamanhoTabuleiro - 1) {
        val linhasTexto = linhasArquivo[linhaInicio + numeroLinhas].split(',')
        for (indiceLinha in 0..linhasTexto.size - 1) {
            val letraAtual = linhasTexto[indiceLinha]
            if (letraAtual != "") {
                tabuleiro[numeroLinhas][indiceLinha] = letraAtual.first()
            }
        }
    }
    return tabuleiro
}

fun gravarJogo(nomeFicheiro: String, tabuleiroHumano: Array<Array<Char?>>,
               tabuleiroPalpitesDoHumano: Array<Array<Char?>>, tabuleiroComputador: Array<Array<Char?>>,
               tabuleiroPalpitesDoComputador: Array<Array<Char?>>) {
    val escreveArquivo = File(nomeFicheiro).printWriter()
    escreveArquivo.print("${tabuleiroComputador.size},${tabuleiroComputador.first().size}")
    escreveArquivo.print("\n\nJogador\n")
    escreveArquivo.print("Real\n")
    gravarTabuleiro(tabuleiroHumano, escreveArquivo)
    escreveArquivo.print("\nJogador\n")
    escreveArquivo.print("Palpites\n")
    gravarTabuleiro(tabuleiroPalpitesDoHumano, escreveArquivo)
    escreveArquivo.print("\nComputador\n")
    escreveArquivo.print("Real\n")
    gravarTabuleiro(tabuleiroComputador, escreveArquivo)
    escreveArquivo.print("\nComputador\n")
    escreveArquivo.print("Palpites\n")
    gravarTabuleiro(tabuleiroPalpitesDoComputador, escreveArquivo)
    escreveArquivo.close()
}

fun gravarTabuleiro(tabuleiro: Array<Array<Char?>>, escreveArquivo: PrintWriter) {
    for (linha in 0..tabuleiro.size - 1) {
        for (coluna in 0..tabuleiro[linha].size - 1) {
            val letraAtual = tabuleiro[linha][coluna]
            if (letraAtual != null) {
                escreveArquivo.print(letraAtual.toString())
            }
            if (coluna < tabuleiro[linha].size - 1) {
                escreveArquivo.print(",")
            }
        }
        escreveArquivo.println()
    }
}
