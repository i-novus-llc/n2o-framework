import React, { useMemo } from 'react'
import { pure } from 'recompose'
import pick from 'lodash/pick'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import evalExpression from '../../../utils/evalExpression'

/**
 * Компонент создания строки в таблице
 * @reactProps enablingCondition - условие доступности действия rowClick при клике по строке
 * @constructor
 */
function AdvancedTableRowWithAction(props) {
    const {
        className,
        isRowActive,
        setRef,
        children,
        model,
        rowClick,
        rowClass,
        clickWithAction,
        clickFocusWithAction,
    } = props

    const enablingCondition = useMemo(() => {
        if (!rowClick) { return true }
        const enablingCondition = evalExpression(rowClick.enablingCondition, model)

        return (enablingCondition || enablingCondition === undefined)
    }, [rowClick, model])

    const onClick = useMemo(() => (enablingCondition
        ? clickWithAction
        : () => {}), [clickWithAction, enablingCondition])

    const classes = classNames(
        className,
        'n2o-table-row n2o-advanced-table-row',
        enablingCondition ? 'row-click' : 'row-deleted',
        {
            'table-active': isRowActive,
            [rowClass]: rowClass,
        },
    )

    const newProps = {
        ...pick(props, ['data-row-key', 'onFocus', 'style']),
        ref: el => setRef && setRef(el, model.id),
        tabIndex: 0,
        key: model.id,
        className: classes,
        onClick,
        onFocus: clickFocusWithAction,
    }

    return React.createElement('tr', newProps, [...children])
}

AdvancedTableRowWithAction.propTypes = {
    clickFocusWithAction: PropTypes.func,
    clickWithAction: PropTypes.func,
    rowClick: PropTypes.func,
    rowClass: PropTypes.string,
    className: PropTypes.string,
    isRowActive: PropTypes.bool,
    setRef: PropTypes.func,
    children: PropTypes.array,
    model: PropTypes.object,
}

export default pure(AdvancedTableRowWithAction)
