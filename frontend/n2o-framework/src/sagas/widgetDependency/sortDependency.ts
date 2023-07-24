import forOwn from 'lodash/forOwn'
import isEmpty from 'lodash/isEmpty'

import { metaPropsType } from '../../plugins/utils'

import { IDependencies } from './WidgetTypes'

export const sortDependency = (dependency: IDependencies) => {
    const tmpFetch: metaPropsType = {}
    let newDependency: metaPropsType = {}

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
