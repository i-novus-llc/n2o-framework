import get from 'lodash/get'
import find from 'lodash/find'

/* TODO it should take a complete error and return the component,
*   should be obtained through the context and should be able to take a custom config */
export const errorController = (status, components) => {
    if (!status) {
        return null
    }

    return get(
        find(components, component => String(component.status) === String(status)),
        'component',
        null,
    )
}
