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
            {/* eslint-disable-next-line no-nested-ternary */}
            {singleElement
                ? (first(tooltipList))
                : nullElements
                    ? null
                    : (<Tooltip {...props} />)
            }
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
    fieldKey: PropTypes.array,
    /**
   * trigger показывать tooltip по hover или click
   */
    trigger: PropTypes.string,
    /**
   * расположение tooltip
   */
    placement: PropTypes.string,
    /**
   * применить к label dashed underline
   */
    labelDashed: PropTypes.bool,
    /**
   * dark(default) или light тема tooltip
   */
    theme: PropTypes.string,
    model: PropTypes.object,
}

ListTextCell.defaultProps = {
    placement: 'bottom',
    trigger: 'hover',
    fieldKey: [],
    labelDashed: false,
    theme: 'dark',
}
