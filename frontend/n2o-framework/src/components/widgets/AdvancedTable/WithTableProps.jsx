import React, { useMemo, useContext } from 'react'
import omit from 'lodash/omit'
import { useStore } from 'react-redux'

import { useCheckAccess as check } from '../../../core/auth/SecurityController'
import { FactoryContext } from '../../../core/factory/context'
import { StandardFieldset } from '../Form/fieldsets'

import { useColumnsState } from './hooks/useColumnsState'
import { useResolveCellsVisible } from './hooks/useResolveCellsVisible'

export function WithTableProps(Component) {
    function Wrapper(props) {
        const { getState } = useStore()
        const state = getState()

        const { filter, id, table } = props
        const { resolveProps } = useContext(FactoryContext)
        const { header, body } = table

        const cells = useMemo(() => ({
            header: header.cells.map(cell => resolveProps(cell)),
            body: body.cells.map(cell => resolveProps(cell)),
        }), [body.cells, header.cells, resolveProps])

        const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps])

        const [columnsState, changeColumnParam] = useColumnsState(cells.header, id, state)
        const resolvedCells = useResolveCellsVisible(cells, columnsState)

        const tableConfig = useMemo(() => {
            const config = omit(table, ['autoSelect', 'autoFocus', 'textWrap', 'header.cells', 'body.cells'])

            return resolveProps(config)
        }, [resolveProps, table])

        const paginationVisible = useMemo(() => !columnsState.every(
            column => !column.visibleState || !column.visible,
        ),
        [columnsState])
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

    return Wrapper
}
