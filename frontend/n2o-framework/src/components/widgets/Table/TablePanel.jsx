import React from 'react'
import indexOf from 'lodash/indexOf'
import isEmpty from 'lodash/isEmpty'
import pure from 'recompose/pure'
import PropTypes from 'prop-types'

import { hotkeys } from '../../../tools/hotkeys'
import { KEY_UP_ARROW, KEY_DOWN_ARROW } from '../../../tools/keycodes'

// eslint-disable-next-line import/extensions,import/no-unresolved
import Thead from './Thead'
// eslint-disable-next-line import/extensions,import/no-unresolved
import Tbody from './Tbody'
import RowPure from './RowPure'
// eslint-disable-next-line import/extensions,import/no-unresolved
import Cell from './Cell'

class TablePanel extends React.Component {
    // eslint-disable-next-line no-useless-constructor
    constructor(props) {
        super(props)
    }

    onPressArrowUp(e) {
        this.moveSelectedRow(e, -1)
    }

    onPressArrowDown(e) {
        this.moveSelectedRow(e, 1)
    }

    moveSelectedRow(e, rowDelta) {
        e.preventDefault()
        const { onResolveById } = this.props
        const nextId = this.calculateNextSelectionPosition(rowDelta)

        onResolveById(nextId)
    }

    calculateNextSelectionPosition(rowDelta) {
        const { ids, resolveModel } = this.props
        const idx = indexOf(ids, resolveModel.id)

        return ids[idx + rowDelta] ? ids[idx + rowDelta] : resolveModel.id
    }

    resolveTable(model) {
        const { onResolve } = this.props

        onResolve(model)
    }

    render() {
        const {
            cells,
            headers,
            hasSelect,
            datasource,
            onSort,
            sorting,
        } = this.props

        return (
            <table className="table table-responsive table-sm table-hover">
                <Thead onSort={onSort} sorting={sorting} headers={headers} />
                <Tbody>
                    {
                        !isEmpty(datasource)
                            ? datasource.map(model => (
                                <RowPure
                                    id={model.id}
                                    onClick={hasSelect && this.resolveTable.bind(this, model)}
                                >
                                    {cells.map(cell => (
                                        <Cell model={model} metadata={cell} />
                                    ))}
                                </RowPure>
                            ))
                            : (
                                <RowPure>
                                    <td style={{ height: 330 }} colSpan={cells.length}>
                                        {'Нет записей'}
                                    </td>
                                </RowPure>
                            )
                    }
                </Tbody>
            </table>
        )
    }
}

TablePanel.propTypes = {
    onResolveById: PropTypes.func,
    ids: PropTypes.string,
    resolveModel: PropTypes.object,
    onResolve: PropTypes.func,
    cells: PropTypes.array,
    headers: PropTypes.array,
    hasSelect: PropTypes.bool,
    datasource: PropTypes.array,
    onSort: PropTypes.func,
    sorting: PropTypes.object,
}

export default pure(
    hotkeys(TablePanel, {
        [KEY_UP_ARROW]: 'onPressArrowUp',
        [KEY_DOWN_ARROW]: 'onPressArrowDown',
    }),
)
