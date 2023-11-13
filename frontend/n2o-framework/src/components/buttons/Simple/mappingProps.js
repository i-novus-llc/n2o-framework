export default function mappingProps(props) {
    return {
        hintPosition: props.placement,
        ...props,
    }
}
