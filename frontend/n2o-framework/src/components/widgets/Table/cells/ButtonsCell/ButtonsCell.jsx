import React from 'react'
import PropTypes from 'prop-types'
import { compose, withHandlers, withProps } from 'recompose'
import get from 'lodash/get'
import classNames from 'classnames'

import { useResolved } from '../../../../../core/Expression/useResolver'
import Toolbar from '../../../../buttons/Toolbar'
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
    tooltipFieldId,
    placement = 'bottom',
}) {
    const key = `${id || 'buttonCell'}_${get(model, 'id', 1)}`
    const resolverToolbar = useResolved(toolbar, model)

    if (!visible) {
        return null
    }

    const hint = get(model, tooltipFieldId, null)

    return (
        <DefaultCell disabled={disabled} className={classNames('d-inline-flex', className)}>
            <Toolbar
                className="n2o-buttons-cell"
                entityKey={key}
                toolbar={resolverToolbar}
                onClick={onResolve}
                placement={placement}
                hint={hint}
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
