import React from 'react'
import indexOf from 'lodash/indexOf'
import isEmpty from 'lodash/isEmpty'
import pure from 'recompose/pure'

import { hotkeys } from '../../../tools/hotkeys'
import { KEY_UP_ARROW, KEY_DOWN_ARROW } from '../../../tools/keycodes'

import Thead from './Thead'
import Tbody from './Tbody'
import RowPure from './RowPure'
import Cell from './Cell'

class TablePanel extends React.Component {
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
        this.props.onResolve(model)
    }

    render() {
        const {
            cells,
            headers,
            hasSelect,
            datasource,
            resolveModel,
            onSort,
            sorting,
        } = this.props
        let rows = null

        if (!isEmpty(datasource)) {
            rows = datasource.map(model => (
                <RowPure
                    id={model.id}
                    onClick={hasSelect && this.resolveTable.bind(this, model)}
                >
                    {cells.map(cell => (
                        <Cell model={model} metadata={cell} />
                    ))}
                </RowPure>
            ))
        } else {
            rows = (
                <RowPure>
                    <td style={{ height: 330 }} colSpan={cells.length}>

            Нет записей
                    </td>
                </RowPure>
            )
        }

        return (
            <table className="table table-responsive table-sm table-hover">
                <Thead onSort={onSort} sorting={sorting} headers={headers} />
                <Tbody>{rows}</Tbody>
            </table>
        )
    }
}

export default pure(
    hotkeys(TablePanel, {
        [KEY_UP_ARROW]: 'onPressArrowUp',
        [KEY_DOWN_ARROW]: 'onPressArrowDown',
    }),
)
