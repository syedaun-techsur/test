import React from "react"
import { cn } from "@/lib/utils"

/**
 * Skeleton component renders a loading placeholder with pulse animation.
 * 
 * @param {React.HTMLAttributes<HTMLDivElement>} props - Props including className and other div attributes.
 */
interface SkeletonProps extends React.HTMLAttributes<HTMLDivElement> {}

const Skeleton: React.FC<SkeletonProps> = ({ className, ...props }) => {
  return (
    <div
      className={cn("animate-pulse rounded-md bg-muted", className)}
      {...props}
    />
  )
}

export { Skeleton }