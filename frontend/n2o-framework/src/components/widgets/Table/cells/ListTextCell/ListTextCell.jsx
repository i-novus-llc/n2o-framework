import React from 'react'
import get from 'lodash/get'
import first from 'lodash/first'
import PropTypes from 'prop-types'
import { compose, setDisplayName } from 'recompose'

import withCell from '../../withCell'

import Tooltip from './Tooltip'

function ListTextCell(props) {
    const { model, fieldKey } = props

    const tooltipList = get(model, fieldKey)
    const singleElement = tooltipList.length === 1
    const nullElements = tooltipList.length === 0

    return (
        <div className="list-text-cell">
            {singleElement ? (
                first(tooltipList)
            ) : nullElements ? null : (
                <Tooltip {...props} />
            )}
        </div>
    )
}

export { ListTextCell }
export default compose(
    setDisplayName('ListTextCell'),
    withCell,
)(ListTextCell)

ListTextCell.propTypes = {
    /**
   * ID ячейки
   */
    id: PropTypes.string.isRequired,
    /**
   * src
   */
    src: PropTypes.string.isRequired,
    /**
   * заголовок tooltip
   */
    label: PropTypes.string.isRequired,
    /**
   * Склонение заголовка для единичных значений
   */
    oneLabel: PropTypes.string,
    /**
   * Склонение заголовка для нескольких значений
   */
    fewLabel: PropTypes.string,
    /**
   * Склонение заголовка для множества значений
   */
    manyLabel: PropTypes.string,
    /**
   * массив для списка tooltip
   */
    fieldKey: PropTypes.array.isRequired,
    /**
   * trigger показывать tooltip по hover или click
   */
    trigger: PropTypes.string.isRequired,
    /**
   * расположение tooltip
   */
    placement: PropTypes.string.isRequired,
    /**
   * применить к label dashed underline
   */
    labelDashed: PropTypes.bool.isRequired,
    /**
   * dark(default) или light тема tooltip
   */
    theme: PropTypes.string.isRequired,
}

ListTextCell.defaultProps = {
    placement: 'bottom',
    trigger: 'hover',
    fieldKey: [],
    labelDashed: false,
    theme: 'dark',
}
