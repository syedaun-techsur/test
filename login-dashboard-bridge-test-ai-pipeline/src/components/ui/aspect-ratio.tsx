import { Root as AspectRatioRoot } from "@radix-ui/react-aspect-ratio";

type AspectRatioProps = React.ComponentProps<typeof AspectRatioRoot>;

const AspectRatio: React.FC<AspectRatioProps> = (props) => {
  return <AspectRatioRoot {...props} />;
};

export { AspectRatio, AspectRatioRoot };
export default AspectRatio;