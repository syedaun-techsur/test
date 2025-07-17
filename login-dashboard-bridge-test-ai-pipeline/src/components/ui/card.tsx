import * as React from "react";

import { cn } from "@/lib/utils";

type DivProps = React.HTMLAttributes<HTMLDivElement>;
type HeadingProps = React.HTMLAttributes<HTMLHeadingElement>;
type ParagraphProps = React.HTMLAttributes<HTMLParagraphElement>;

const Card = React.forwardRef<HTMLDivElement, DivProps>(
  (props, ref: React.ForwardedRef<HTMLDivElement>) => {
    const { className, ...rest } = props;
    return (
      <div
        ref={ref}
        className={cn("rounded-lg border bg-card text-card-foreground shadow-sm", className)}
        {...rest}
      />
    );
  }
);
Card.displayName = "Card";

const CardHeader = React.forwardRef<HTMLDivElement, DivProps>(
  (props, ref: React.ForwardedRef<HTMLDivElement>) => {
    const { className, ...rest } = props;
    return (
      <div ref={ref} className={cn("flex flex-col space-y-1.5 p-6", className)} {...rest} />
    );
  }
);
CardHeader.displayName = "CardHeader";

const CardTitle = React.forwardRef<HTMLHeadingElement, HeadingProps>(
  (props, ref: React.ForwardedRef<HTMLHeadingElement>) => {
    const { className, ...rest } = props;
    return (
      <h3
        ref={ref}
        className={cn("text-2xl font-semibold leading-none tracking-tight", className)}
        {...rest}
      />
    );
  }
);
CardTitle.displayName = "CardTitle";

const CardDescription = React.forwardRef<HTMLParagraphElement, ParagraphProps>(
  (props, ref: React.ForwardedRef<HTMLParagraphElement>) => {
    const { className, ...rest } = props;
    return (
      <p ref={ref} className={cn("text-sm text-muted-foreground", className)} {...rest} />
    );
  }
);
CardDescription.displayName = "CardDescription";

const CardContent = React.forwardRef<HTMLDivElement, DivProps>(
  (props, ref: React.ForwardedRef<HTMLDivElement>) => {
    const { className, ...rest } = props;
    return <div ref={ref} className={cn("p-6 pt-0", className)} {...rest} />;
  }
);
CardContent.displayName = "CardContent";

const CardFooter = React.forwardRef<HTMLDivElement, DivProps>(
  (props, ref: React.ForwardedRef<HTMLDivElement>) => {
    const { className, ...rest } = props;
    return (
      <div ref={ref} className={cn("flex items-center p-6 pt-0", className)} {...rest} />
    );
  }
);
CardFooter.displayName = "CardFooter";

export { Card, CardHeader, CardFooter, CardTitle, CardDescription, CardContent };