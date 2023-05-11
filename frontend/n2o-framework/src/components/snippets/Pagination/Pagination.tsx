import React from 'react'
import classNames from 'classnames'

import { Select } from './Select'
import { Total } from './Total'
import {
    TOTAL,
    TOTAL_TITLE,
    PAGE_LINK_CLASS,
    IPagination,
} from './constants'
import { usePagination, getTotalPages } from './usePagination'
import { Pages } from './Pages'
import { getTotalVisibility } from './helpers'

export function Pagination(props: IPagination) {
    const {
        className,
        showCount,
        count,
        size,
        onSelect,
        prevIcon,
        nextIcon,
        showSinglePage = true,
        showLast = true,
        activePage = 1,
        prevLabel = null,
        nextLabel = null,
        prev = true,
        next = true,
        hasNext = false,
        loading = false,
    } = props

    const total = count ? `${TOTAL} ${count}` : null
    const totalPages = getTotalPages(count, size)
    const totalVisible = getTotalVisibility(showCount, showLast, count)
    const totalClick = () => {
        onSelect(activePage, true)
    }

    const { pages = [], extraFirst, extraEnd } = usePagination(totalPages, activePage, hasNext, showLast)

    const getNextDisabled = () => {
        if (totalPages) {
            return activePage === totalPages
        }

        return !hasNext
    }
    const prevDisabled = activePage === 1
    const nextDisabled = getNextDisabled()

    const prevClick = () => {
        if (!prevDisabled) {
            onSelect(activePage - 1, showLast)
        }
    }

    const nextClick = () => {
        if (!nextDisabled) {
            onSelect(activePage + 1, showLast)
        }
    }

    const multiplePages = pages.length > 1
    const pagesVisible = showSinglePage ? pages.length > 0 : multiplePages

    return (
        <section className={classNames('pagination-container d-inline-flex', className)}>
            <Total
                total={total}
                title={TOTAL_TITLE}
                visible={totalVisible}
                className="pagination__total"
                onClick={totalClick}
            />
            <Select
                onClick={prevClick}
                className={classNames(
                    PAGE_LINK_CLASS, 'prev',
                    { 'with-title': prevLabel, 'with-icon': prevIcon },
                )}
                disabled={loading || prevDisabled}
                icon={prevIcon}
                title={prevLabel}
                visible={prev && multiplePages}
            />
            <Pages
                pages={pages}
                activePage={activePage}
                onSelect={onSelect}
                extraFirstVisible={extraFirst}
                extraLastVisible={extraEnd}
                lastPage={totalPages}
                showLast={showLast}
                hasNext={hasNext}
                loading={loading}
                visible={pagesVisible}
            />
            <Select
                onClick={nextClick}
                className={classNames(
                    PAGE_LINK_CLASS, 'next',
                    { 'with-title': nextLabel, 'with-icon': nextIcon },
                )}
                disabled={loading || nextDisabled}
                icon={nextIcon}
                title={nextLabel}
                visible={next && multiplePages}
            />
        </section>
    )
}
