import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

/**
 * Combines class names with clsx and merges them using tailwind-merge
 * to handle conditional classes and Tailwind CSS conflict resolution.
 *
 * @param inputs - List of class values to combine and merge
 * @returns The merged and combined class string
 */
export function cn(...inputs: ClassValue[]): string {
  return twMerge(clsx(...inputs))
}