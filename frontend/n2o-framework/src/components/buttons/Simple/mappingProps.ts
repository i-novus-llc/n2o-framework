export default function mappingProps(props: Record<string, unknown>) {
    return { hintPosition: props.placement, ...props }
}
