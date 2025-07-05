#!/usr/bin/env python3
"""
Basic Python file for testing purposes.
Contains simple functions and basic operations.
"""

def add_numbers(a: float, b: float) -> float:
    """Add two numbers and return the result."""
    return a + b

def multiply_numbers(a: float, b: float) -> float:
    """Multiply two numbers and return the result."""
    return a * b

def greet_user(name: str) -> str:
    """Greet a user with their name."""
    return f"Hello, {name}!"

def is_even(number: int) -> bool:
    """Check if a number is even."""
    return number % 2 == 0

def calculate_factorial(n: int) -> int:
    """
    Calculate factorial of a non-negative integer n.

    Raises:
        ValueError: If n is negative.
    """
    if n < 0:
        raise ValueError("Factorial is not defined for negative numbers.")
    if n == 0 or n == 1:
        return 1
    result = 1
    for i in range(2, n + 1):
        result *= i
    return result

if __name__ == "__main__":
    print("Testing basic functions:")
    print(f"add_numbers(5, 3) = {add_numbers(5, 3)}")
    print(f"multiply_numbers(4, 7) = {multiply_numbers(4, 7)}")
    print(f"greet_user('Alice') = {greet_user('Alice')}")
    print(f"is_even(10) = {is_even(10)}")
    print(f"is_even(7) = {is_even(7)}")
    print(f"calculate_factorial(5) = {calculate_factorial(5)}")