import java.io.File
import java.util.*
import kotlin.system.exitProcess

class Num(private val myList: MutableList<Int>, private val outputFile: String) {

    fun sortNatural() {
        if (outputFile == "") {
            print("Sorted data: ")
            mergeSort(myList).forEach { print("$it ") }
        } else {
            File(outputFile).printWriter().use { out -> out.print("Sorted data: ") }
            mergeSort(myList).forEach { File(outputFile).printWriter().use { out -> print("$it ") } }
        }
    }

    private fun mergeSort(myList: MutableList<Int>): MutableList<Int> {
        if (myList.size == 1) return myList

        val middle = myList.size / 2
        val left = myList.subList(0, middle)
        val right = myList.subList(middle, myList.size)

        return merge(mergeSort(left), mergeSort(right))
    }

    private fun merge(left: MutableList<Int>, right: MutableList<Int>): MutableList<Int> {
        var indexRight = 0
        var indexLeft = 0
        val newList: MutableList<Int> = mutableListOf()

        while (indexLeft < left.count() && indexRight < right.count()) {
            if (left[indexLeft] <= right[indexRight]) {
                newList.add(left[indexLeft])
                indexLeft++
            } else {
                newList.add(right[indexRight])
                indexRight++
            }
        }
        while (indexLeft < left.size) {
            newList.add(left[indexLeft])
            indexLeft++
        }

        while (indexRight < right.size) {
            newList.add(right[indexRight])
            indexRight++
        }
        return newList
    }

    fun sortByCount() {
        val map = mutableMapOf<Int, Int>().apply {
            for (i in myList) {
                if (this.contains(i)) {
                    this[i] = this[i]!! + 1
                } else {
                    this.put(i, 1)
                }
            }
        }

        map.toList()
            .sortedWith(compareBy { (key, _) -> key })
            .sortedWith(compareBy { (_, value) -> value })
            .toMap()
            .forEach {
                if (outputFile == "") {
                    println("${it.key}: ${it.value} time(s), ${it.value * 100 / myList.size}%")
                } else {
                    File(outputFile).writeText("${it.key}: ${it.value} time(s), ${it.value * 100 / myList.size}%")
                }
            }
    }

    fun allTotal() {
        val str = "Total numbers: ${myList.size}."
        if (outputFile == "") println(str) else File(outputFile).printWriter().use { out -> out.println(str) }
    }
}

class Line(private val myList: MutableList<String>, private val outputFile: String) {

    fun allTotal() {
        val str = "Total lines: ${myList.size}."
        if (outputFile == "") println(str) else File(outputFile).printWriter().use { out -> out.println(str) }
    }

    fun sortNatural() {
        if (outputFile == "") {
            print("Sorted data: ")
            myList.sortedBy { it }.forEach { println(it) }
        } else {
            File(outputFile).printWriter().use { out -> out.print("Sorted data: ") }
            myList.sortedBy { it }.forEach {
                File(outputFile).printWriter().use { println(it) }
            }
        }
    }
    fun sortByCount() = sortByCountString(myList, outputFile)
}

class Str(private val myList: MutableList<String>, private val outputFile: String) {

    fun allTotal() {
        val str = "Total words: ${myList.size}."
        if (outputFile == "") println(str) else File(outputFile).printWriter().use { out -> out.println(str) }
    }

    fun sortNatural() {
        if (outputFile == "") {
            print("Sorted data: ")
            myList.sortedBy { it }.forEach { println(it) }
        } else {
            File(outputFile).printWriter().use { out -> out.print("Sorted data: ") }
            myList.sortedBy { it }.forEach {
                File(outputFile).printWriter().use { println(it) }
            }
        }
    }
    fun sortByCount() = sortByCountString(myList, outputFile)
}

fun sortByCountString(myList: MutableList<String>, outputFile: String) {

    val map = mutableMapOf<String, Int>().apply {
        for (i in myList) {
            if (this.contains(i)) {
                this[i] = this[i]!! + 1
            } else {
                this.put(i, 1)
            }
        }
    }
    map.toList()
        .sortedWith(compareBy { (key, _) -> key })
        .sortedWith(compareBy { (_, value) -> value })
        .toMap()
        .forEach {
            if (outputFile == "") {
                println("${it.key}: ${it.value} time(s), ${it.value * 100 / myList.size}%")
            } else {
                File(outputFile).printWriter().use { out -> out.println("${it.key}: ${it.value} time(s), ${it.value * 100 / myList.size}%") }
            }
        }
}

fun main(args: Array<String>) {
    val lambdaErrorCheck = { argsNew: Array<String> ->
        val regex = Regex("[word]*[long]*[line]*")
        if (argsNew.contains("-dataType")) {
            try {
                if (!regex.matches(argsNew[argsNew.indexOf("-dataType") + 1])) {
                    println("No data type defined!")
                    exitProcess(0)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                println("No data type defined!")
                exitProcess(0)
            }
        }

        if (argsNew.contains("-sortingType")) {
            try {
                if (argsNew[argsNew.indexOf("-sortingType") + 1].let { it != "natural" && it != "byCount" }) {
                    println("No sorting type defined!")
                    exitProcess(0)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                println("No sorting type defined!")
                exitProcess(0)
            }
        }
    }
    lambdaErrorCheck(args)

    val inputFile = if (args.contains("-inputFile")) args[args.indexOf("-inputFile") + 1] else ""
    val outputFile = if (args.contains("-outputFile")) args[args.indexOf("-outputFile") + 1] else ""
    val dataType = if (args.contains("-dataType")) args[args.indexOf("-dataType") + 1] else "word"
    val sortingTool = if (args.contains("-sortingType")) args[args.indexOf("-sortingType") + 1] else "natural"

    val strings = mutableListOf<String>()
    val numbers = mutableListOf<Int>()

    val scanner = if (inputFile == "") Scanner(System.`in`) else Scanner(File(inputFile))
    while (scanner.hasNext()) {
        when (dataType) {
            "line" -> strings.add(scanner.nextLine())
            "long" -> numbers.add(scanner.nextInt())
            else -> strings.add(scanner.next())
        }
    }

    scanner.close()

    when (dataType) {
        "word" -> {
            val word = Str(strings, outputFile)
            word.allTotal()
            if (sortingTool == "natural") {
                word.sortNatural()
            } else if (sortingTool == "byCount") {
                word.sortByCount()
            }
        }
        "long" -> {
            val num = Num(numbers, outputFile)
            num.allTotal()
            if (sortingTool == "natural") {
                num.sortNatural()
            } else if (sortingTool == "byCount") {
                num.sortByCount()
            }
        }
        "line" -> {
            val long = Line(strings, outputFile)
            long.allTotal()
            if (sortingTool == "natural") {
                long.sortNatural()
            } else if (sortingTool == "byCount") {
                long.sortByCount()
            }
        }
    }
}