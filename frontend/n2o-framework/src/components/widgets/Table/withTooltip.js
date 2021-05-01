import React from 'react'
import get from 'lodash/get'

import Tooltip from '../../snippets/Tooltip/Tooltip'

/**
 * HOC, оборачивает Cell добавляя Tooltip
 * @param WrappedComponent оборачиваемый компонент
 * @param model модель данных
 * @param hint подсказка - тело тултипа
 * @param tooltipFieldId ключ по которому резолвится Tooltip и берется hint
 */
export default function withTooltip(WrappedComponent) {
    return function TooltipHOC(props) {
        const { model, placement, tooltipFieldId } = props
        const hint = get(model, tooltipFieldId)

        if (!hint) {
            return <WrappedComponent {...props} />
        }

        return (
            <Tooltip
                label={<WrappedComponent {...props} />}
                hint={hint}
                placement={placement || 'bottom'}
            />
        )
    }
}
