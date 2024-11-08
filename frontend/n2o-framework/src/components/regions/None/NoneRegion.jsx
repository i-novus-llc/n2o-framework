import React, { useLayoutEffect, memo } from 'react'
import map from 'lodash/map'
import classNames from 'classnames'
import { useDispatch } from 'react-redux'
import isEqual from 'lodash/isEqual'

import withWidgetProps from '../withWidgetProps'
import { RegionContent } from '../RegionContent'
import { registerRegion, unregisterRegion } from '../../../ducks/regions/store'

/**
 * Регион None (простой див)
 * @reactProps {array} content - массив из объектов, которые описывают виджет{id}
 * @reactProps {string} pageId - идентификатор страницы
 */

function arePropsEqual(oldProps, newProps) {
    // widgetsDatasource from withWidgetProps
    return !isEqual(oldProps.widgetsDatasource, newProps.widgetsDatasource) ||
        !isEqual(oldProps.content, newProps.content) ||
        oldProps.disabled !== newProps.disabled ||
        oldProps.visible !== newProps.visible
}

const NoneRegionBody = memo((props) => {
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

export const NoneRegion = withWidgetProps(NoneRegionBody)
export default NoneRegion
