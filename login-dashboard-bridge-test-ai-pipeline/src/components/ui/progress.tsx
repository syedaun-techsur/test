import * as React from "react"
import * as ProgressPrimitive from "@radix-ui/react-progress"

import { cn } from "@/lib/utils"

const Progress = React.forwardRef<
  React.ElementRef<typeof ProgressPrimitive.Root>,
  React.ComponentPropsWithoutRef<typeof ProgressPrimitive.Root> & { value?: number }
>(({ className, value = 0, ...props }, ref) => {
  const clampedValue = Math.min(Math.max(value, 0), 100) // Clamp value between 0 and 100

  return (
    <ProgressPrimitive.Root
      ref={ref}
      className={cn(
        "relative h-4 w-full overflow-hidden rounded-full bg-secondary",
        className
      )}
      value={clampedValue}
      role="progressbar"
      aria-valuenow={clampedValue}
      aria-valuemin={0}
      aria-valuemax={100}
      {...props}
    >
      <ProgressPrimitive.Indicator
        className="h-full bg-primary transition-all duration-300 ease-in-out"
        style={{ width: `${clampedValue}%` }}
      />
    </ProgressPrimitive.Root>
  )
})
Progress.displayName = "Progress"

export { Progress }