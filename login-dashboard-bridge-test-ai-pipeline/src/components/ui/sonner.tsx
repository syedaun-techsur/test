import { useTheme } from "next-themes";
import { Toaster as Sonner } from "sonner";

const Toaster = () => {
  const { theme = "system" } = useTheme();

  return <Sonner theme={theme} />;
};

export { Toaster };