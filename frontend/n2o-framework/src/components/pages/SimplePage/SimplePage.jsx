import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import isUndefined from 'lodash/isUndefined'
import { mapProps } from 'recompose'
import cn from 'classnames'

import { WIDGETS } from '../../../core/factory/factoryLevels'
import Factory from '../../../core/factory/Factory'
import DefaultPage from '../DefaultPage'

function SimplePage({ id, widget, metadata, ...rest }) {
    const customWidth = get(metadata, 'width')
    const hasCustomWidth = !isUndefined(customWidth)

    const style = hasCustomWidth ? { width: customWidth } : {}

    return (
        <DefaultPage metadata={metadata} {...rest}>
            <div
                className={cn({
                    'n2o-simple-page': !hasCustomWidth,
                    'n2o-simple-page_slim': hasCustomWidth,
                })}
            >
                <div style={style}>
                    <Factory
                        key={`simple-page-${id}`}
                        level={WIDGETS}
                        {...widget}
                        pageId={id}
                    />
                </div>
            </div>
        </DefaultPage>
    )
}

SimplePage.propTypes = {
    id: PropTypes.string,
    widget: PropTypes.object,
    slim: PropTypes.bool,
}

SimplePage.defaultProps = {
    widget: {},
    /**
   * slim флаг сжатия контента страницы к центру
   */
    slim: false,
}

export default mapProps(props => ({
    ...props,
    widget: get(props, 'metadata.widget', {}),
}))(SimplePage)
