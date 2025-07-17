import React from "react"
import { cn } from "@/lib/utils"

/**
 * Skeleton component to display a loading placeholder with animation.
 * It accepts all standard div HTML attributes.
 */
const Skeleton: React.FC<React.HTMLAttributes<HTMLDivElement>> = ({
  className,
  ...props
}) => {
  return (
    <div
      className={cn("animate-pulse rounded-md bg-muted", className)}
      {...props}
    />
  )
}

export { Skeleton }
export default Skeleton