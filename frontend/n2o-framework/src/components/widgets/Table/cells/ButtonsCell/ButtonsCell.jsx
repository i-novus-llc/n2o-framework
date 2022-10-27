import React from 'react'
import PropTypes from 'prop-types'
import { compose, withHandlers, withProps } from 'recompose'
import get from 'lodash/get'
import classNames from 'classnames'

import propsResolver from '../../../../../utils/propsResolver'
import Toolbar from '../../../../buttons/Toolbar'
import withTooltip from '../../withTooltip'
import withCell from '../../withCell'
import DefaultCell from '../DefaultCell'

/**
 *
 * @param id
 * @param className
 * @param visible
 * @param disabled
 * @param toolbar
 * @param model
 * @param onResolve
 * @param tooltipTriggerRef
 * @returns {*}
 * @constructor
 * @return {null}
 */
function ButtonsCell({
    id,
    className,
    visible,
    disabled,
    model,
    toolbar,
    onResolve,
    tooltipTriggerRef,
}) {
    const key = `${id || 'buttonCell'}_${get(model, 'id', 1)}`

    if (!visible) {
        return null
    }

    return (
        <DefaultCell disabled={disabled} className={classNames('d-inline-flex', className)}>
            <Toolbar
                tooltipTriggerRef={tooltipTriggerRef}
                className="n2o-buttons-cell"
                entityKey={key}
                toolbar={propsResolver(toolbar, model)}
                onClick={onResolve}
            />
        </DefaultCell>
    )
}

ButtonsCell.propTypes = {
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * ID ячейки
     */
    id: PropTypes.string,
    /**
     * Флаг видимости
     */
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    model: PropTypes.any,
    toolbar: PropTypes.any,
    onResolve: PropTypes.func,
    tooltipTriggerRef: PropTypes.func,
}

ButtonsCell.defaultProps = {
    visible: true,
    disabled: false,
}

const enhance = compose(
    withProps(props => ({
        ...props,
        isControlledTooltip: true,
    })),
    withTooltip,
    withCell,
    withHandlers({
        onResolve: ({ callAction, model }) => () => {
            if (callAction && model) {
                callAction(model)
            }
        },
    }),
)

export { ButtonsCell }
export default enhance(ButtonsCell)
