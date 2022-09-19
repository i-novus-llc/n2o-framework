import React from 'react'
import get from 'lodash/get'
import PropTypes from 'prop-types'

import { ExtendedTooltipComponent } from '../../snippets/Tooltip/TooltipHOC'

export default function withTooltip(Component) {
    /**
     * HOC, оборачивает Cell добавляя Tooltip
     * @param WrappedComponent оборачиваемый компонент
     * @param model модель данных
     * @param hint подсказка - тело тултипа
     * @param tooltipFieldId ключ по которому резолвится Tooltip и берется hint
     */
    return function Wrapper(props) {
        const { model = {}, placement, tooltipFieldId } = props
        const hint = get(model, tooltipFieldId, null)

        if (!hint) {
            return <Component {...props} />
        }

        Wrapper.propTypes = {
            model: PropTypes.object,
            placement: PropTypes.object,
            tooltipFieldId: PropTypes.string,
        }

        return (
            <ExtendedTooltipComponent
                Component={Component}
                {...props}
                hint={hint}
                placement={placement || 'bottom'}
            />
        )
    }
}
