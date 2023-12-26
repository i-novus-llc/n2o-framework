import React from 'react'
import PropTypes from 'prop-types'
import { compose, setDisplayName } from 'recompose'
import get from 'lodash/get'
import first from 'lodash/first'
import map from 'lodash/map'
import isArray from 'lodash/isArray'

import withCell from '../../withCell'
import DefaultCell from '../DefaultCell'
import getNoun from '../../../../../utils/getNoun'
import { Tooltip } from '../../../../snippets/Tooltip/TooltipHOC'

import { ListTextCellTrigger } from './ListTextCellTrigger'
import { replacePlaceholder } from './utils'

function ListTextCell(props) {
    const {
        model,
        fieldKey,
        disabled,
        label,
        oneLabel,
        fewLabel,
        manyLabel,
        trigger,
        labelDashed,
        placement,
    } = props

    const tooltipList = get(model, fieldKey)
    const singleElement = tooltipList.length === 1
    const nullElements = tooltipList.length === 0

    const validTooltipList = model && fieldKey && isArray(tooltipList)
    const listLength = validTooltipList ? tooltipList.length : 0

    const currentLabel = getNoun(listLength, oneLabel || label, fewLabel || label, manyLabel || label)

    const hint = validTooltipList ? map(tooltipList, (tooltipItem, index) => (
        <div
            key={index}
            className="list-text-cell__tooltip-container__body"
        >
            {tooltipItem}
        </div>
    )) : null

    if (nullElements) {
        return <DefaultCell disabled={disabled} className="list-text-cell" />
    }

    return (
        <DefaultCell disabled={disabled} className="list-text-cell">
            {singleElement ? first(tooltipList) : (
                <Tooltip hint={hint} placement={placement} trigger={trigger}>
                    <ListTextCellTrigger
                        label={replacePlaceholder(currentLabel, listLength)}
                        labelDashed={labelDashed}
                    />
                </Tooltip>
            )}
        </DefaultCell>
    )
}

export { ListTextCell }
export default compose(
    setDisplayName('ListTextCell'),
    withCell,
)(ListTextCell)

ListTextCell.propTypes = {
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
    model: PropTypes.object,
    disabled: PropTypes.bool,
}

ListTextCell.defaultProps = {
    placement: 'bottom',
    trigger: 'hover',
    fieldKey: [],
    labelDashed: false,
    disabled: false,
}
