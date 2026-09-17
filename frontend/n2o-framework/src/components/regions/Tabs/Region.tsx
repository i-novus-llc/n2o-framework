import React, { useCallback, useContext } from 'react'
import cloneDeep from 'lodash/cloneDeep'
import flowRight from 'lodash/flowRight'
import { Tabs as TabsControl } from '@i-novus/n2o-components/lib/display/Tabs/Tabs'
import set from 'lodash/set'

import { createRegionContainer } from '../withRegionContainer'
import withWidgetProps from '../withWidgetProps'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'

import { TabsController, Props } from './TabsController'

interface Tab {
    content: JSX.Element[]
    disabled: boolean
    icon?: string
    id: string
    invalid?: boolean
    label: string
    opened: boolean
    visible?: boolean
    tooltip?: string
    className?: string
}

export interface TabsRegionProps extends Props {
    tabs: Tab[]
}

function TabsRegionBody({
    tabs,
    activeEntity: active,
    changeActiveEntity: changeActive,
    className,
    hideSingleTab,
    contentStyle,
    contentClassName,
    style,
    datasource,
    activeTabFieldId,
    setResolve,
    resolveModel,
}: TabsRegionProps) {
    const { getComponent } = useContext(FactoryContext)
    const Tabs = getComponent('Tabs', FactoryLevels.SNIPPETS) || TabsControl

    const onChange = useCallback((event) => {
        const id = event.target.value

        if (datasource && activeTabFieldId) {
            const model = resolveModel[datasource]

            if (activeTabFieldId.includes('.')) {
                const newModel = cloneDeep(model)

                set(newModel, activeTabFieldId, id)
                setResolve(newModel)

                return
            }

            setResolve({ ...model, [activeTabFieldId]: id })

            return
        }

        changeActive(id)
    }, [changeActive, datasource, activeTabFieldId, setResolve])

    return (
        <Tabs
            tabs={tabs}
            active={active}
            onChange={onChange}
            className={className}
            contentStyle={contentStyle}
            contentClassName={contentClassName}
            hideSingleTab={hideSingleTab}
            style={style}
            /* Рендер в DOM всего content, неактивный скрывается через css d-none.
             * Причина: на данный момент, невозможно хранить метаданные виджета в store,
             * без его фактического монтирования в DOM.
             * Метаданные виджета необходимы для логики скрытия/переключения/валидации TabsRegion.
             */
            contentRenderMethod="all"
        />
    )
}

export const TabsRegion = flowRight(
    createRegionContainer({ listKey: 'tabs' }),
    withWidgetProps,
    TabsController,
)(TabsRegionBody)
