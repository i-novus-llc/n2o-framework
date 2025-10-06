import React, { ComponentType, ReactElement, useContext } from 'react'
import { ROOT_CLASS_NAME_PARAM } from '@i-novus/n2o-components/lib/navigation/helpers'

import { FactoryContext } from '../../core/factory/context'
import { FactoryLevels } from '../../core/factory/factoryLevels'

import { type BaseNavigationContent, type FactoryComponentProps } from './types'

// @INFO Проблема, что в некоторых компонентах className устанавливается внутрь своей обертки
// прим. <div><button className={className}/></div> это путает семантику в DOM
interface RootClassName {
    rootClassName?: string
}

export const ROOT_CLASS_NAME = 'n2o-navigation-panel__children'

export function FactoryChildren({
    content,
    className,
    [ROOT_CLASS_NAME_PARAM]: rootClassName = ROOT_CLASS_NAME,
}: {
    content: BaseNavigationContent['content'],
    className?: string
} & RootClassName) {
    const { getComponent } = useContext(FactoryContext)

    return (
        // eslint-disable-next-line react/jsx-no-useless-fragment
        <>
            {content
                ?.map(({ src, id, ...rest }) => {
                    const Component = getComponent<
                        ComponentType<FactoryComponentProps & RootClassName>>(src, FactoryLevels.REGIONS)

                    return Component
                        ? React.createElement(Component, { key: id, className, rootClassName, ...rest })
                        : null
                })
                .filter((children): children is ReactElement => children !== null)}
        </>
    )
}
