import React, { useCallback, useState } from 'react'
import isUndefined from 'lodash/isUndefined'
import { Tooltip } from 'reactstrap'
import PropTypes from 'prop-types'

const N2O_ROOT = document.getElementById('n2o')

/**
 * @description Простой управляемый тултип, построенный на компоненте Tooltip из библиотеки reactstrap.
 * @param {String} id - id элемента, на котором будет всплывать тултип
 * @param {String} message - текст тултипа
 * @param {String} trigger - список триггеров, разделенных пробелом (пример: "click hover focus"), по умолчанию: "hover"
 * @param {String} placement - флаг положения тултипа, по умолчанию: "top"
 * @returns {JSX.Element|null}
 * @see {@link https://reactstrap.github.io/components/tooltips/}
 */
export const SimpleTooltip = ({ id, message, trigger, placement }) => {
    const [tooltipOpen, setTooltipOpen] = useState(false)

    const toggle = useCallback(() => setTooltipOpen(tooltipOpen => !tooltipOpen), [])

    if (isUndefined(message) || typeof document === 'undefined') {
        return null
    }

    return (
        <Tooltip
            trigger={trigger}
            target={id}
            placement={placement}
            isOpen={tooltipOpen}
            toggle={toggle}
            boundariesElement={N2O_ROOT || 'window'}
        >
            {message}
        </Tooltip>
    )
}

SimpleTooltip.propTypes = {
    id: PropTypes.string,
    message: PropTypes.string,
    trigger: PropTypes.string,
    placement: PropTypes.oneOf([
        'auto',
        'auto-start',
        'auto-end',
        'top',
        'top-start',
        'top-end',
        'right',
        'right-start',
        'right-end',
        'bottom',
        'bottom-start',
        'bottom-end',
        'left',
        'left-start',
        'left-end',
    ]),
}

SimpleTooltip.defaultProps = {
    trigger: 'hover',
    placement: 'top',
}
