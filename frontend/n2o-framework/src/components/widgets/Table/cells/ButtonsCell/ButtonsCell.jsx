import React from 'react'
import PropTypes from 'prop-types'
import { compose, withHandlers } from 'recompose'
import get from 'lodash/get'
import cx from 'classnames'

import propsResolver from '../../../../../utils/propsResolver'
import { setModel } from '../../../../../actions/models'
import { PREFIXES } from '../../../../../constants/models'
import Toolbar from '../../../../buttons/Toolbar'
import withTooltip from '../../withTooltip'

/**
 *
 * @param id
 * @param className
 * @param visible
 * @param actions
 * @param toolbar
 * @param model
 * @param style
 * @param widgetId
 * @param onResolve
 * @param other
 * @returns {*}
 * @constructor
 * @return {null}
 */
function ButtonsCell({
    id,
    className,
    visible,
    model,
    style,
    toolbar,
    actions,
    widgetId,
    onResolve,
    ...other
}) {
    const key = `${id || 'buttonCell'}_${get(model, 'id', 1)}`

    return visible ? (
        <div className="d-inline-flex">
            <Toolbar
                className={cx('n2o-buttons-cell', className)}
                entityKey={key}
                toolbar={propsResolver(toolbar, model)}
                onClick={onResolve}
            />
        </div>
    ) : null
}

ButtonsCell.propTypes = {
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Стили
   */
    style: PropTypes.object,
    /**
   * ID ячейки
   */
    id: PropTypes.string,
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
}

ButtonsCell.defaultProps = {
    visible: true,
}

const enhance = compose(
    withTooltip,
    withHandlers({
        onResolve: ({ dispatch, widgetId, model }) => () => dispatch(setModel(PREFIXES.resolve, widgetId, model)),
    }),
)

export { ButtonsCell }
export default enhance(ButtonsCell)
