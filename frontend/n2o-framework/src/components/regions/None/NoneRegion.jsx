import React from 'react'
import PropTypes from 'prop-types'
import { compose, pure, setDisplayName } from 'recompose'
import map from 'lodash/map'
import classNames from 'classnames'

import withWidgetProps from '../withWidgetProps'
import { RegionContent } from '../RegionContent'

/**
 * Регион None (простой див)
 * @reactProps {array} content - массив из объектов, которые описывают виджет{id}
 * @reactProps {function} getWidget - функция получения виджета
 * @reactProps {string} pageId - идентификатор страницы
 */

const NoneRegion = ({ content, className, style, pageId, disabled }) => (
    <div className={classNames('n2o-none-region', className, { 'n2o-disabled': disabled })} style={style}>
        {map(content, (item, i) => (
            <RegionContent key={`${i}-${item.id}`} content={[item]} pageId={pageId} />
        ))}
    </div>
)

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
