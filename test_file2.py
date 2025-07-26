#!/usr/bin/env python3
"""
Second Python test file with list/string operations
Contains functions for working with lists and strings
"""

import string
from typing import List, Optional, Dict


def reverse_string(text: str) -> str:
    """Reverse a string"""
    return text[::-1]


def count_vowels(text: str) -> int:
    """Count vowels in a string"""
    vowels = "aeiouAEIOU"
    return sum(1 for char in text if char in vowels)


def find_max_in_list(numbers: List[int]) -> Optional[int]:
    """Find the maximum number in a list"""
    if not numbers:
        return None
    return max(numbers)


def filter_even_numbers(numbers: List[int]) -> List[int]:
    """Filter and return only even numbers from a list"""
    return [num for num in numbers if num % 2 == 0]


def create_word_count(text: str) -> Dict[str, int]:
    """Count words in a text string with punctuation removed and case-insensitive"""
    words = text.lower().split()
    table = str.maketrans("", "", string.punctuation)
    word_count: Dict[str, int] = {}
    for word in words:
        clean_word = word.translate(table)
        if clean_word:
            word_count[clean_word] = word_count.get(clean_word, 0) + 1
    return word_count


def fibonacci_sequence(n: int) -> List[int]:
    """Generate first n numbers in Fibonacci sequence"""
    if n <= 0:
        return []
    elif n == 1:
        return [0]
    sequence = [0, 1]
    for _ in range(2, n):
        sequence.append(sequence[-1] + sequence[-2])
    return sequence


# Test the functions
if __name__ == "__main__":
    print("Testing string and list functions:\n")
    print(f"reverse_string('Python') = {reverse_string('Python')}")
    print(f"count_vowels('Hello World') = {count_vowels('Hello World')}\n")

    test_numbers = [3, 8, 1, 15, 22, 7, 10]
    print(f"find_max_in_list({test_numbers}) = {find_max_in_list(test_numbers)}")
    print(f"filter_even_numbers({test_numbers}) = {filter_even_numbers(test_numbers)}\n")

    test_text = "hello world hello python world"
    print(f"create_word_count('{test_text}') = {create_word_count(test_text)}")
    print(f"fibonacci_sequence(8) = {fibonacci_sequence(8)}")