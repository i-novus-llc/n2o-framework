import map from 'lodash/map'

import { sortDependency } from './sortDependency'
import { Dependencies, WidgetsDependencies } from './WidgetTypes'

export function getWidgetDependency(
    widgetsDependencies: WidgetsDependencies,
    widgetId: string,
    dependency: Dependencies,
) {
    if (dependency) {
        const parents: string[] = []

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
