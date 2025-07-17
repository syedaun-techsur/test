import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

/**
 * Combines class names using clsx and merges Tailwind classes with twMerge.
 * @param inputs - Array of class values to be combined and merged.
 * @returns A single merged string of class names.
 */
export function cn(...inputs: ClassValue[]): string {
  return twMerge(clsx(...inputs))
}