import React from 'react'
import get from 'lodash/get'
import isUndefined from 'lodash/isUndefined'

import Tooltip from '../../snippets/Tooltip/Tooltip'

/**
 * HOC, оборачивает Cell добавляя Tooltip
 * @param WrappedComponent оборачиваемый компонент
 * @param model модель данных
 * @param hint подсказка - тело тултипа
 * @param tooltipFieldId ключ по которому резолвится Tooltip и берется hint
 */
export default function withTooltip(WrappedComponent) {
    class TooltipHOC extends React.Component {
        render() {
            const { model, placement, tooltipFieldId } = this.props
            const hint = get(model, tooltipFieldId)

            return !isUndefined(hint) ? (
                <Tooltip
                    label={<WrappedComponent {...this.props} />}
                    hint={hint}
                    placement={placement || 'bottom'}
                />
            ) : (
                <WrappedComponent {...this.props} />
            )
        }
    }
    return TooltipHOC
}
