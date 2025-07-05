#!/usr/bin/env python3
"""
Second Python test file with list/string operations
Contains functions for working with lists and strings
"""

from typing import List, Optional, Dict


def reverse_string(text: str) -> str:
    """Return the reversed version of the input string."""
    return text[::-1]


def count_vowels(text: str) -> int:
    """Count and return the number of vowels in the input string."""
    if not text:
        return 0
    vowels = "aeiouAEIOU"
    return sum(1 for char in text if char in vowels)


def find_max_in_list(numbers: List[int]) -> Optional[int]:
    """Return the maximum number in the list or None if the list is empty."""
    if not numbers:
        return None
    return max(numbers)


def filter_even_numbers(numbers: List[int]) -> List[int]:
    """Return a list containing only the even numbers from the input list."""
    if not numbers:
        return []
    return [num for num in numbers if num % 2 == 0]


def create_word_count(text: str) -> Dict[str, int]:
    """
    Count the occurrences of each word in the input text string.
    Words are converted to lowercase and stripped of basic punctuation.
    """
    translator = str.maketrans('', '', '.,!?;:"')
    words = text.lower().translate(translator).split()
    word_count: Dict[str, int] = {}
    for word in words:
        word_count[word] = word_count.get(word, 0) + 1
    return word_count


def fibonacci_sequence(n: int) -> List[int]:
    """Generate and return the first n numbers in the Fibonacci sequence."""
    if n <= 0:
        return []
    sequence = [0, 1]
    for i in range(2, n):
        sequence.append(sequence[i - 1] + sequence[i - 2])
    return sequence[:n]


def main() -> None:
    print("Testing string and list functions:")
    print(f"reverse_string('Python') = {reverse_string('Python')}")
    print(f"count_vowels('Hello World') = {count_vowels('Hello World')}")

    test_numbers = [3, 8, 1, 15, 22, 7, 10]
    print(f"find_max_in_list({test_numbers}) = {find_max_in_list(test_numbers)}")
    print(f"filter_even_numbers({test_numbers}) = {filter_even_numbers(test_numbers)}")

    test_text = "hello world hello python world"
    print(f"create_word_count('{test_text}') = {create_word_count(test_text)}")
    print(f"fibonacci_sequence(8) = {fibonacci_sequence(8)}")


if __name__ == "__main__":
    main()