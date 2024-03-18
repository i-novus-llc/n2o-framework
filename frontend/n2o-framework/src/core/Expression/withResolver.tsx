import React from 'react'

import { useResolver } from './useResolver'

/**
 * HOC для работы с propsResolver c с данными из контекста в class-components
 */
export function WithPropsResolver<Props>(Component: React.Component<Props>['constructor']) {
    return function WithResolver(props: Props) {
        const propsResolver = useResolver()

        return (<Component {...props} propsResolver={propsResolver} />)
    }
}
