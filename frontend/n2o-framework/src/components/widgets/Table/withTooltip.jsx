import React from 'react'
import get from 'lodash/get'
import PropTypes from 'prop-types'

import { Tooltip } from '../../snippets/Tooltip/TooltipHOC'

export default function withTooltip(Component) {
    /**
     * HOC, оборачивает Cell добавляя Tooltip,
     * дает компоненту forwardedRef для установки tooltip trigger
     * @param props
     */
    function Wrapper(props) {
        const { model = {}, placement, tooltipFieldId } = props
        const hint = get(model, tooltipFieldId, null)

        if (!hint) {
            return <Component {...props} />
        }

        return (
            <Tooltip hint={hint} placement={placement || 'bottom'} className="n2o-cell-tooltip">
                <Component {...props} />
            </Tooltip>
        )
    }

    Wrapper.displayName = `withTooltip(${Component?.displayName || 'UnknownComponent'})`

    Wrapper.propTypes = {
        model: PropTypes.object,
        placement: PropTypes.object,
        tooltipFieldId: PropTypes.string,
    }

    return Wrapper
}
