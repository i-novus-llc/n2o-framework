import forOwn from 'lodash/forOwn'
import isEmpty from 'lodash/isEmpty'

export const sortDependency = (dependency) => {
    const tmpFetch = {}
    let newDependency = {}

    forOwn(dependency, (v, k) => {
        if (k !== 'fetch') {
            newDependency[k] = v
        } else {
            tmpFetch[k] = v
        }
    })

    if (!isEmpty(tmpFetch)) {
        newDependency = {
            ...newDependency,
            ...tmpFetch,
        }
    }

    return newDependency
}
