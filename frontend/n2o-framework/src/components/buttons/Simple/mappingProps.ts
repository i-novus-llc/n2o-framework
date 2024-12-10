export function mappingProps(props: Record<string, unknown>) {
    return { hintPosition: props.placement, ...props }
}

export default mappingProps
