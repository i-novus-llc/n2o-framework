import React from 'react'

import { Select } from './Select'
import { ExtraPage } from './ExtraPage'
import { PAGE_LINK_CLASS, Pages as PagesProps } from './constants'

export function Pages({
    pages,
    activePage,
    onSelect,
    extraFirstVisible,
    extraLastVisible,
    lastPage,
    showLast,
    hasNext,
    loading,
    visible,
}: PagesProps) {
    if (!pages.length || !visible) {
        return null
    }

    return (
        <div className="pagination-pages pagination d-inline-flex">
            <ExtraPage
                visible={extraFirstVisible}
                page={1}
                onSelect={onSelect}
                ellipsis="right"
                showLast={showLast}
                hasNext={hasNext}
                disabled={loading}
            />
            {pages.map((page) => {
                const active = activePage === page
                const setPage = () => {
                    if (!active && !loading) {
                        onSelect(page)
                    }
                }

                return (
                    <Select
                        key={page}
                        title={page}
                        onClick={setPage}
                        className={PAGE_LINK_CLASS}
                        active={active}
                        disabled={loading}
                    />
                )
            })}
            <ExtraPage
                visible={extraLastVisible}
                page={lastPage}
                onSelect={onSelect}
                ellipsis="left"
                showLast={showLast}
                canSelect={showLast}
                hasNext={hasNext}
                disabled={loading}
            />
        </div>
    )
}
