import map from 'lodash/map'

import { sortDependency } from './sortDependency'
import { IDependencies, IWidgetsDependencies } from './WidgetTypes'

export function getWidgetDependency(
    widgetsDependencies: IWidgetsDependencies,
    widgetId: string,
    dependency: IDependencies,
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
