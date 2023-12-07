package year2023

import utils.getPuzzleInput
import utils.logMeasureTime
import year2023.Day07.Card.Companion.cardOrdinal
import year2023.Day07.Mode.PART_1
import year2023.Day07.Mode.PART_2

class Day07 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val lines = getPuzzleInput(this)

            println("=== Part One ===")
            logMeasureTime {
                Day07().partOne(lines)
            }
            println()

            println("=== Part Two ===")
            logMeasureTime {
                Day07().partTwo(lines)
            }
            println()
        }
    }

    private fun partOne(lines: List<String>) {
        val hands = getHands(lines, PART_1)

        println(
            sortHands(hands, PART_1)
                .reversed()
                .foldIndexed(0) { index, acc, hand ->
                    acc + hand.bid * (index + 1)
                }
        )
    }

    private fun partTwo(lines: List<String>) {
        val hands = getHands(lines, PART_2)

        println(
            sortHands(hands, PART_2)
                .reversed()
                .foldIndexed(0) { index, acc, hand ->
                    acc + hand.bid * (index + 1)
                }
        )
    }

    private fun getHands(lines: List<String>, mode: Mode): List<Hand> {
        val hands = lines.map { line ->
            val (cards, bid) = parseCardsAndBid(line)

            Hand(
                cards = cards,
                type = getHighestType(cards, mode),
                bid = bid,
            )
        }
        return hands
    }

    private fun parseCardsAndBid(line: String): Pair<List<Card>, Int> {
        val cards = line.substringBefore(" ").map { char -> Card.entries.first { it.char == char } }
        val bid = line.substringAfter(" ").toInt()

        return Pair(cards, bid)
    }

    private fun getHighestType(hand: List<Card>, mode: Mode): Type = when (mode) {
        PART_1 -> Type.entries.find { type ->
            type.isPresent(hand)
        } ?: throw IllegalStateException("${hand.joinToString(separator = ", ")} doesn't match any type ??!")

        PART_2 -> {
            if (!hand.contains(Card.CARD_J)) {
                getHighestType(hand, PART_1)
            } else {
                val highestAppearingCard = hand.groupingBy { it }.eachCount().maxBy { it.value }.key

                if (highestAppearingCard == Card.CARD_J) {
                    // JJJJJ
                    if (hand.all { it == Card.CARD_J }) {
                        Type.FIVE_OF_A_KIND
                    } else {
                        val highestAppearingCardNotJ: Card = hand
                            .groupingBy { it }
                            .eachCount()
                            .filter { it.key != Card.CARD_J }
                            .maxBy { it.value }
                            .key

                        getHighestType(
                            replaceJackBy(hand, highestAppearingCardNotJ),
                            PART_1
                        )
                    }
                } else {
                    getHighestType(
                        replaceJackBy(hand, highestAppearingCard),
                        PART_1
                    )
                }
            }
        }
    }

    private fun replaceJackBy(
        hand: List<Card>,
        highestAppearingCardNotJ: Card
    ) = hand.map {
        if (it == Card.CARD_J) {
            highestAppearingCardNotJ
        } else {
            it
        }
    }

    private fun sortHands(hands: List<Hand>, mode: Mode) = hands.sortedWith { hand1, hand2 ->
        val typeComparison = hand1.type.compareTo(hand2.type)

        if (typeComparison != 0) {
            typeComparison
        } else {
            hand1.cards.forEachIndexed { index, card1 ->
                val card2 = hand2.cards[index]

                val cardComparison = card1.cardOrdinal(mode).compareTo(card2.cardOrdinal(mode))

                if (cardComparison != 0) {
                    return@sortedWith cardComparison
                }
            }

            throw IllegalStateException("Couldn't detect difference between $hand1 and $hand2")
        }
    }

    private data class Hand(
        val cards: List<Card>,
        val type: Type,
        val bid: Int,
    )

    private enum class Card(
        val char: Char,
    ) {
        CARD_A('A'),
        CARD_K('K'),
        CARD_Q('Q'),
        CARD_J('J'),
        CARD_T('T'),
        CARD_9('9'),
        CARD_8('8'),
        CARD_7('7'),
        CARD_6('6'),
        CARD_5('5'),
        CARD_4('4'),
        CARD_3('3'),
        CARD_2('2');

        companion object {
            fun Card.cardOrdinal(mode: Mode): Int = if (this == CARD_J && mode == PART_2) {
                Card.entries.size
            } else {
                ordinal
            }
        }
    }

    private enum class Type(val isPresent: (mode: List<Card>) -> Boolean) {
        FIVE_OF_A_KIND(
            isPresent = { chars -> chars.groupingBy { it.char }.eachCount().size == 1 }
        ),
        FOUR_OF_A_KIND(
            isPresent = { chars ->
                val groups = chars.groupingBy { it.char }.eachCount()

                groups.size == 2 && groups.values.any { it == 4 }
            }
        ),
        FULL_HOUSE(
            isPresent = { chars ->
                val groups = chars.groupingBy { it.char }.eachCount()

                groups.size == 2
            }
        ),
        THREE_OF_A_KIND(
            isPresent = { chars ->
                val groups = chars.groupingBy { it.char }.eachCount()

                groups.values.any { it == 3 }
            }
        ),
        TWO_PAIRS(
            isPresent = { chars ->
                val groups = chars.groupingBy { it.char }.eachCount()

                groups.values.count { it == 2 } == 2
            }
        ),
        ONE_PAIR(
            isPresent = { chars ->
                val groups = chars.groupingBy { it.char }.eachCount()

                groups.values.any { it == 2 }
            }
        ),
        HIGH_CARD(isPresent = { true }),
    }

    private enum class Mode {
        PART_1,
        PART_2,
    }
}