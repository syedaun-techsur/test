#!/usr/bin/env python3
"""
Second Python test file with list/string operations
Contains functions for working with lists and strings
"""

def reverse_string(text):
    """Reverse a string"""
    return text[::-1]

def count_vowels(text):
    """Count vowels in a string"""
    vowels = "aeiouAEIOU"
    return sum(1 for char in text if char in vowels)

def find_max_in_list(numbers):
    """Find the maximum number in a list"""
    if not numbers:
        return None
    return max(numbers)

def filter_even_numbers(numbers):
    """Filter and return only even numbers from a list"""
    return [num for num in numbers if num % 2 == 0]

def create_word_count(text):
    """Count words in a text string"""
    words = text.lower().split()
    word_count = {}
    for word in words:
        # Remove basic punctuation
        word = word.strip('.,!?;:"')
        word_count[word] = word_count.get(word, 0) + 1
    return word_count

def fibonacci_sequence(n):
    """Generate first n numbers in Fibonacci sequence"""
    if n <= 0:
        return []
    elif n == 1:
        return [0]
    elif n == 2:
        return [0, 1]
    
    sequence = [0, 1]
    for i in range(2, n):
        sequence.append(sequence[i-1] + sequence[i-2])
    return sequence

# Test the functions
if __name__ == "__main__":
    print("Testing string and list functions:")
    print(f"reverse_string('Python') = {reverse_string('Python')}")
    print(f"count_vowels('Hello World') = {count_vowels('Hello World')}")
    
    test_numbers = [3, 8, 1, 15, 22, 7, 10]
    print(f"find_max_in_list({test_numbers}) = {find_max_in_list(test_numbers)}")
    print(f"filter_even_numbers({test_numbers}) = {filter_even_numbers(test_numbers)}")
    
    test_text = "hello world hello python world"
    print(f"create_word_count('{test_text}') = {create_word_count(test_text)}")
    print(f"fibonacci_sequence(8) = {fibonacci_sequence(8)}") 