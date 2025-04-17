import React, { useLayoutEffect } from 'react'
import { connect, useDispatch } from 'react-redux'
import classNames from 'classnames'
import pick from 'lodash/pick'
import isEmpty from 'lodash/isEmpty'
import flowRight from 'lodash/flowRight'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import { Panel, Collapse } from '../../snippets/Collapse/Collapse'
import { RegionContent } from '../RegionContent'
import { registerRegion, unregisterRegion } from '../../../ducks/regions/store'
import { type State } from '../../../ducks/State'
import { WithGetWidget } from '../withWidgetProps'
import { EMPTY_ARRAY } from '../../../utils/emptyTypes'

import { type ListRegionProps } from './types'

const ListRegion = (props: ListRegionProps) => {
    const {
        id: regionId,
        parent,
        getWidgetProps,
        className,
        style,
        disabled,
        isVisible,
        label,
        pageId,
        regionsState,
        collapsible = true,
        expand = true,
        hasSeparator = true,
        content = EMPTY_ARRAY,
    } = props

    const dispatch = useDispatch()

    useLayoutEffect(() => {
        dispatch(registerRegion(regionId, {
            regionId,
            isInit: true,
            parent,
            visible: true,
        }))

        return () => {
            dispatch(unregisterRegion(regionId))
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const collapseProps = pick(props, 'destroyInactivePanel', 'accordion')
    const panelProps = pick(props, [
        'type', 'forceRender', 'collapsible', 'expand', 'hasSeparator',
        'label', 'content', 'isVisible', 'pageId', 'regionsState',
    ])

    const visible = content.some((meta) => {
        const { id, tabs } = meta || {}

        if (tabs) {
            if (isEmpty(regionsState) || !regionsState[id]) { return false }

            return regionsState[id].visible
        }

        const { visible = true } = getWidgetProps(id) || {}

        return visible
    })

    return (
        <div
            className={classNames('n2o-list-region', className, { 'n2o-disabled': disabled, 'd-none': !visible })}
            style={style}
        >
            <Collapse
                defaultActiveKey="open"
                collapsible={collapsible}
                className="n2o-list-region__collapse"
                {...collapseProps}
            >
                <Panel
                    {...panelProps}
                    key={expand ? 'open' : 'close'}
                    header={<span className="n2o-list-region__collapse-name"><Text>{label}</Text></span>}
                    className={classNames({ line: hasSeparator, 'd-none': isVisible === false })}
                >
                    <RegionContent content={content} pageId={pageId} />
                </Panel>
            </Collapse>
        </div>
    )
}

export { ListRegion }
export default flowRight(
    WithGetWidget,
    connect(({ regions, widgets }: State) => ({ regionsState: regions, widgetsState: widgets })),
)(ListRegion)
