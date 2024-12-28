import React, { ReactNode } from 'react'
import classNames from 'classnames'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { SortDirection } from '../../../core/datasource/const'

export interface Props {
    title?: string
    children: ReactNode
    sorting?: SortDirection
    sortingParam: string
    onSort(param: string, direction: SortDirection): void
    visible?: boolean
}

const SORTING_DIRECTIONS: Record<SortDirection, SortDirection> = {
    [SortDirection.asc]: SortDirection.desc,
    [SortDirection.desc]: SortDirection.none,
    [SortDirection.none]: SortDirection.asc,
}

export const Sorter: React.FC<Props> = ({
    title,
    children,
    sorting = SortDirection.none,
    sortingParam,
    onSort,
    visible = true,
}) => {
    if (!visible) { return null }

    const onClick = (event: React.MouseEvent<HTMLElement>) => {
        event.preventDefault()
        onSort(sortingParam, SORTING_DIRECTIONS[sorting])
    }

    return (
        <button
            type="button"
            className="link-button"
            title={title}
            tabIndex={-1}
            onClick={onClick}
        >
            {children}
            <Icon
                visible={sorting !== SortDirection.none}
                className={classNames('n2o-sorting-icon', {
                    'fa fa-sort-amount-asc': sorting === SortDirection.asc,
                    'fa fa-sort-amount-desc': sorting === SortDirection.desc,
                })}
                aria-hidden="true"
            />
        </button>
    )
}

export default Sorter
