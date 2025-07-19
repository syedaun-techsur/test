import * as React from "react";

const MOBILE_BREAKPOINT = 768;

export function useIsMobile(): boolean {
  const [isMobile, setIsMobile] = React.useState(() => {
    if (typeof window !== "undefined") {
      return window.innerWidth < MOBILE_BREAKPOINT;
    }
    return false;
  });

  React.useEffect(() => {
    if (typeof window === "undefined") {
      return;
    }

    const mql = window.matchMedia(`(max-width: ${MOBILE_BREAKPOINT - 1}px)`);

    const onChange = (event: MediaQueryListEvent) => {
      setIsMobile(event.matches);
    };

    // Use addListener/removeListener for better browser compatibility
    mql.addListener(onChange);

    // Initialize state in case of changes after mount
    setIsMobile(mql.matches);

    return () => {
      mql.removeListener(onChange);
    };
  }, []);

  return isMobile;
}