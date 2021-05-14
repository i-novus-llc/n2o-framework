import map from 'lodash/map'

import { sortDependency } from './sortDependency'

export function getWidgetDependency(
    widgetsDependencies,
    widgetId,
    dependency,
) {
    if (dependency) {
        const parents = []

        dependency = sortDependency(dependency)

        map(dependency, (dep) => {
            map(dep, (d) => {
                if (d.on) {
                    parents.push(d.on)
                }
            })
        })

        return {
            ...widgetsDependencies,
            [widgetId]: {
                widgetId,
                dependency,
                parents,
            },
        }
    }

    return widgetsDependencies
}
