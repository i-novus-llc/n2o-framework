import React, { ComponentType, useContext, useMemo } from 'react'
import classNames from 'classnames'

import { FactoryContext } from '../../core/factory/context'
import { FactoryLevels } from '../../core/factory/factoryLevels'

import { type BaseNavigationContent, type FactoryComponentProps } from './types'

export function useFactoryChildren({
    content,
    className,
}: {
    content: BaseNavigationContent['content'],
    className?: string
}) {
    const { getComponent } = useContext(FactoryContext)

    return useMemo(() => content?.map(({ src, id, className: contentClass, ...rest }) => {
        const Component = getComponent<
            ComponentType<FactoryComponentProps & { id: string }>>(src, FactoryLevels.REGIONS)

        return Component
            ? React.createElement(Component, {
                key: id,
                id,
                className: classNames(className, contentClass),
                ...rest,
            })
            : null
    }), [content, className, getComponent])
}

useFactoryChildren.displayName = 'n2o/navigation/useFactoryChildren'
