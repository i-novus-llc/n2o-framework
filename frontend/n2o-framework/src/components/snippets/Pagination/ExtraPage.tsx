import React from 'react'
import classNames from 'classnames'

import { Select } from './Select'
import { PAGE_LINK_CLASS, ExtraPage as ExtraPageProps } from './constants'

function Ellipsis({ visible }: { visible: boolean }) {
    if (!visible) {
        return null
    }

    return <span className="ellipsis d-flex align-items-md-end">...</span>
}

export function ExtraPage({ visible, page, onSelect, ellipsis, showLast, hasNext, disabled = false, canSelect = true }: ExtraPageProps) {
    if (!visible) {
        return null
    }

    const setPage = () => {
        if (canSelect && page) {
            onSelect(page)
        }
    }

    const needToSelect = canSelect && typeof page === 'number'
    const leftCondition = showLast ? true : hasNext

    return (
        <div className="extra-page-item d-inline-flex">
            <Ellipsis visible={ellipsis === 'left' && leftCondition} />
            <Select
                title={page}
                onClick={setPage}
                className={classNames(PAGE_LINK_CLASS, page)}
                visible={needToSelect}
                disabled={disabled}
            />
            <Ellipsis visible={ellipsis === 'right'} />
        </div>
    )
}
