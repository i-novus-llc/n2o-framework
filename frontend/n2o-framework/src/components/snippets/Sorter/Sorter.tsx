import React, { ReactNode } from 'react'
import classNames from 'classnames'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { SortDirection } from '../../../core/datasource/const'

export interface Props {
    title?: string
    children?: ReactNode
    className?: string
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
    className,
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
    let icon: string | undefined

    if (sorting === SortDirection.asc) { icon = 'fa-solid fa-arrow-down-short-wide' }
    if (sorting === SortDirection.desc) { icon = 'fa-solid fa-arrow-down-wide-short' }

    return (
        <button
            type="button"
            className={classNames('link-button', className)}
            title={title}
            tabIndex={-1}
            onClick={onClick}
        >
            {children}
            <Icon
                name={icon}
                className="n2o-sorting-icon"
                aria-hidden="true"
            />
        </button>
    )
}

export default Sorter
