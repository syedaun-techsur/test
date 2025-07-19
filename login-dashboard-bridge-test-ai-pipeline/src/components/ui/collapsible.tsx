import React from "react";
import * as CollapsiblePrimitive from "@radix-ui/react-collapsible";

const Collapsible: React.FC<React.ComponentProps<typeof CollapsiblePrimitive.Root>> = CollapsiblePrimitive.Root;

const CollapsibleTrigger: React.FC<React.ComponentProps<typeof CollapsiblePrimitive.Trigger>> = CollapsiblePrimitive.Trigger;

const CollapsibleContent: React.FC<React.ComponentProps<typeof CollapsiblePrimitive.Content>> = CollapsiblePrimitive.Content;

export { Collapsible, CollapsibleTrigger, CollapsibleContent };