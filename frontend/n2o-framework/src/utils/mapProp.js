import transform from 'lodash/transform'

export default function mapProp(prop) {
    return transform(
        prop,
        (result, value, key) => {
            result.push({ value: key, label: value })
        },
        [],
    )
}
