import React, { useLayoutEffect } from 'react'
import PropTypes from 'prop-types'
import { compose, pure, setDisplayName } from 'recompose'
import map from 'lodash/map'
import classNames from 'classnames'
import { useDispatch } from 'react-redux'

import withWidgetProps from '../withWidgetProps'
import { RegionContent } from '../RegionContent'
import { registerRegion, unregisterRegion } from '../../../ducks/regions/store'

/**
 * Регион None (простой див)
 * @reactProps {array} content - массив из объектов, которые описывают виджет{id}
 * @reactProps {string} pageId - идентификатор страницы
 */

const NoneRegion = ({ id: regionId, content, className, style, pageId, disabled, parent }) => {
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

    return (
        <div className={classNames('n2o-none-region', className, { 'n2o-disabled': disabled })} style={style}>
            {map(content, (item, i) => <RegionContent key={`${i}-${item.id}`} content={[item]} pageId={pageId} parent={parent} />)}
        </div>
    )
}

NoneRegion.propTypes = {
    className: PropTypes.string,
    pageId: PropTypes.string,
    style: PropTypes.object,
    disabled: PropTypes.bool,
    content: PropTypes.any,
}

export { NoneRegion }
export default compose(
    setDisplayName('NoneRegion'),
    pure,
    withWidgetProps,
)(NoneRegion)
