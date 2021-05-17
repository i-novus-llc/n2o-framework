import React from 'react'
import get from 'lodash/get'
import PropTypes from 'prop-types'

// eslint-disable-next-line import/no-named-as-default
import Tooltip from '../../snippets/Tooltip/Tooltip'

export default function withTooltip(WrappedComponent) {
    /**
     * HOC, оборачивает Cell добавляя Tooltip
     * @param WrappedComponent оборачиваемый компонент
     * @param model модель данных
     * @param hint подсказка - тело тултипа
     * @param tooltipFieldId ключ по которому резолвится Tooltip и берется hint
     */
    return function TooltipHOC(props) {
        const { model, placement, tooltipFieldId } = props
        const hint = get(model, tooltipFieldId)

        if (!hint) {
            return <WrappedComponent {...props} />
        }
        TooltipHOC.propTypes = {
            model: PropTypes.object,
            placement: PropTypes.object,
            tooltipFieldId: PropTypes.string,
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
