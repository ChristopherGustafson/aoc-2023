import java.io.File

fun Char.toValue() = when (this) {
    'A' -> 14
    'K' -> 13
    'Q' -> 12
    'J' -> 11
    'T' -> 10
    else -> this.digitToInt()
}

fun Char.toValue2() = when (this) {
    'J' -> 0
    else -> this.toValue()
}

data class Hand(val cardsString: String, val bidString: String) : Comparable<Hand> {

    val bid get(): Int = bidString.toInt()

    private val cards = cardsString.toList().map { it.toValue() }

    private val cardCounts = cards.groupBy { it }.mapValues { it.value.size }

    override fun compareTo(other: Hand): Int =
        compareValuesBy(
            this,
            other,
            { it.cardCounts.containsValue(5) },
            { it.cardCounts.containsValue(4) },
            { it.cardCounts.containsValue(3) && it.cardCounts.containsValue(2) },
            { it.cardCounts.containsValue(3) },
            { it.cardCounts.values.count { it == 2 } },
            { it.cardCounts.containsValue(2) },
            { it.cards[0] },
            { it.cards[1] },
            { it.cards[2] },
            { it.cards[3] },
            { it.cards[4] },
        )
}

data class Hand2(val cardsString: String, val bidString: String) : Comparable<Hand2> {

    val bid get(): Int = bidString.toInt()

    private val cards = cardsString.toList().map { it.toValue2() }

    private val cardCounts = cardCounts(cards)

    override fun compareTo(other: Hand2): Int =
        compareValuesBy(
            this,
            other,
            { it.cardCounts.containsValue(5) },
            { it.cardCounts.containsValue(4) },
            { it.cardCounts.containsValue(3) && it.cardCounts.containsValue(2) },
            { it.cardCounts.containsValue(3) },
            { it.cardCounts.values.count { it == 2 } },
            { it.cardCounts.containsValue(2) },
            { it.cards[0] },
            { it.cards[1] },
            { it.cards[2] },
            { it.cards[3] },
            { it.cards[4] },
        )
}

// Replace jokers with the type of card that is most frequent in the hand
fun cardCounts(cards: List<Int>): Map<Int, Int> {
    val cardCounts = cards.groupBy { it }.mapValues { it.value.size }.toMutableMap()
    val jokers = cardCounts[0] ?: return cardCounts
    val mostCards = cardCounts.filterNot { it.key == 0 }.maxByOrNull { it.value }?.key ?: return cardCounts
    cardCounts.remove(0)
    return cardCounts + Pair(mostCards, cardCounts[mostCards]!! + jokers)
}

fun part1(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .map { it.split(" ") }
        .map { (cards, bid) -> Hand(cards, bid) }
        .sorted()
        .mapIndexed{ rank, hand -> hand.bid * (rank + 1) }
        .sum()

fun part2(input: String) =
    input.split("\n")
        .filter { it.isNotBlank() }
        .map { it.split(" ") }
        .map { (cards, bid) -> Hand2(cards, bid) }
        .sorted()
        .mapIndexed{ rank, hand -> hand.bid * (rank + 1) }
        .sum()

val exampleInput = "32T3K 765\n" +
    "T55J5 684\n" +
    "KK677 28\n" +
    "KTJJT 220\n" +
    "QQQJA 483"

val realInput = File("day7_input.txt").readText()

println("Example part 1: " + part1(exampleInput))

println("Real input part 1: " + part1(realInput))

println("Example part 2: " + part2(exampleInput))

println("Real input part 2: " + part2(realInput))
