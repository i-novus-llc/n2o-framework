import React, { useLayoutEffect, memo } from 'react'
import map from 'lodash/map'
import isEqual from 'lodash/isEqual'
import classNames from 'classnames'
import { useDispatch } from 'react-redux'

import { WithGetWidget } from '../withWidgetProps'
import { RegionContent } from '../RegionContent'
import { registerRegion, unregisterRegion } from '../../../ducks/regions/store'

import { type NoneRegionProps } from './types'

/**
 * Регион None (простой див)
 * @reactProps {array} content - массив из объектов, которые описывают виджет{id}
 * @reactProps {string} pageId - идентификатор страницы
 */

function arePropsEqual(oldProps: NoneRegionProps, newProps: NoneRegionProps) {
    // widgetsDatasource from withWidgetProps
    return !isEqual(oldProps.widgetsDatasource, newProps.widgetsDatasource) ||
        !isEqual(oldProps.content, newProps.content) ||
        oldProps.disabled !== newProps.disabled ||
        oldProps.visible !== newProps.visible
}

const NoneRegionBody = memo((props: NoneRegionProps) => {
    const { id: regionId, content, className, style, pageId, disabled, parent } = props
    const dispatch = useDispatch()

    useLayoutEffect(() => {
        dispatch(registerRegion(regionId, {
            regionId,
            isInit: true,
            parent,
            visible: true,
        }))

        return () => { dispatch(unregisterRegion(regionId)) }

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    return (
        <div className={classNames('n2o-none-region', className, { 'n2o-disabled': disabled })} style={style}>
            {map(content, (item, i) => (
                <RegionContent key={`${i}-${item.id}`} content={[item]} pageId={pageId} parent={parent} />
            ))}
        </div>
    )
}, arePropsEqual)

export const NoneRegion = WithGetWidget<NoneRegionProps>(NoneRegionBody)
export default NoneRegion
