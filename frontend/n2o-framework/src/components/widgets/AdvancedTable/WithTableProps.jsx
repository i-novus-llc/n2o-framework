import React, { useMemo, useContext } from 'react'
import omit from 'lodash/omit'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import flowRight from 'lodash/flowRight'
import { useStore } from 'react-redux'

import { useCheckAccess as check } from '../../../core/auth/SecurityController'
import { FactoryContext } from '../../../core/factory/context'
import { StandardFieldset } from '../Form/fieldsets'
import { makeTableByIdSelector } from '../../../ducks/table/selectors'

import { useColumnsState } from './hooks/useColumnsState'
import { useResolveCellsVisible } from './hooks/useResolveCellsVisible'

function WithReduxTable(Component) {
    function Wrapper(props) {
        const { getState } = useStore()
        const state = getState()
        const { id } = props
        const reduxTable = makeTableByIdSelector(id)(state)

        if (isEmpty(reduxTable)) {
            return null
        }

        return <Component {...props} table={reduxTable} />
    }

    return Wrapper
}

function WithSecurity(Component) {
    function Wrapper(props) {
        const { table } = props
        const headerCells = get(table, 'header.cells', [])

        const cells = []

        for (const cell of headerCells) {
            const { security } = cell

            if (isEmpty(security) || check(security)) {
                cells.push(cell)
            }
        }

        return <Component {...props} table={{ ...table, header: { cells } }} />
    }

    return Wrapper
}

function withCells(Component) {
    function Wrapper(props) {
        const { table } = props
        const { resolveProps } = useContext(FactoryContext)

        const { header, body } = table

        const cells = useMemo(() => ({
            header: header.cells.map(cell => resolveProps(cell)),
            body: body.cells.map(cell => resolveProps(cell)),
        }), [header.cells, body.cells, resolveProps])

        return <Component {...props} cells={cells} />
    }

    return Wrapper
}

export function WithTableProps(Component) {
    function Wrapper(props) {
        const { getState } = useStore()
        const state = getState()

        const { filter, id, table, cells } = props
        const { resolveProps } = useContext(FactoryContext)
        const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps])

        const [columnsState, changeColumnParam] = useColumnsState(cells.header, id, state)
        const resolvedCells = useResolveCellsVisible(cells, columnsState)

        const tableConfig = useMemo(() => {
            const config = omit(table, ['autoSelect', 'autoFocus', 'textWrap', 'header.cells', 'body.cells'])

            return resolveProps(config)
        }, [resolveProps, table])

        const paginationVisible = useMemo(() => columnsState.some(column => column.visible), [columnsState])
        const hasSecurityAccess = check(tableConfig.body?.row?.security)

        return (
            <Component
                {...props}
                changeColumnParam={changeColumnParam}
                columnsState={columnsState}
                tableConfig={tableConfig}
                resolvedFilter={resolvedFilter}
                hasSecurityAccess={hasSecurityAccess}
                resolvedCells={resolvedCells}
                paginationVisible={paginationVisible}
            />
        )
    }

    return flowRight(
        WithReduxTable,
        WithSecurity,
        withCells,
    )(Wrapper)
}
