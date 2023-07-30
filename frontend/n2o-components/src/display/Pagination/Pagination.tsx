import React from 'react'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'

import { PaginationButton } from './PaginationButton'
import { ButtonType, Direction, GetControlButtonArgs, GetLabelArgs, Layout, PaginationProps, RenderBodyPagingArgs } from './types'

const getLabel = ({ direction, icon, label, additionalClass }: GetLabelArgs) => {
    if (icon && label) {
        if (direction === 'left') {
            return (
                <>
                    <span className={classNames(icon, additionalClass)} aria-hidden="true" />
                    {' '}
                    <span>{label}</span>
                </>
            )
        }

        if (direction === 'right') {
            return (
                <>
                    <span>{label}</span>
                    {' '}
                    <span className={classNames(icon, additionalClass)} aria-hidden="true" />
                </>
            )
        }
    }

    if (icon) {
        return (<i className={classNames(icon, additionalClass)} aria-hidden="true" />)
    }

    return label
}

const getControlButton = ({
    buttonType,
    icon,
    label,
    activePage = 0,
    onSelect = () => {},
    lastPage,
}: GetControlButtonArgs) => {
    const buttonsOptions = {
        first: { direction: Direction.left, props: { eventKey: 1, disabled: activePage === 1 }, additionalClass: 'first-button' },
        last: { direction: Direction.right, props: { eventKey: lastPage, disabled: lastPage === activePage }, additionalClass: 'last-button' },
        next: { direction: Direction.right, props: { eventKey: activePage + 1, disabled: lastPage === activePage }, additionalClass: 'next-button' },
        prev: { direction: Direction.left, props: { eventKey: activePage - 1, disabled: activePage === 1 }, additionalClass: 'previous-button' },
    }

    const buttonOption = buttonsOptions[buttonType]

    return (
        <PaginationButton
            {...buttonOption.props}
            label={getLabel({
                direction: buttonOption.direction,
                icon,
                label,
                additionalClass: buttonOption.additionalClass,
            })}
            onSelect={onSelect}
            tabIndex={0}
        />
    )
}

const renderBodyPaging = ({ activePage, maxPages, onSelect, totalPages }: RenderBodyPagingArgs) => {
    const getPager = (
        totalPages: RenderBodyPagingArgs['totalPages'],
        currentPage: RenderBodyPagingArgs['activePage'],
        maxPages: RenderBodyPagingArgs['maxPages'],
    ) => {
        let startPage: number
        let endPage

        if (totalPages <= maxPages) {
            // if we have less than maxPages so show all
            startPage = 1
            endPage = totalPages
        } else {
            // we have more than maxPages so calculate start and end pages
            const middle = Math.ceil(maxPages / 2)

            if (currentPage <= middle) {
                startPage = 1
                endPage = maxPages
            } else if (currentPage + (maxPages - middle) >= totalPages) {
                startPage = totalPages - (maxPages - 1)
                endPage = totalPages
            } else {
                startPage = currentPage - Math.floor(maxPages / 2)
                endPage = currentPage + middle - 1
            }
        }

        return [...Array((endPage + 1) - startPage).keys()].map(i => startPage + i)
    }

    return getPager(totalPages, activePage, maxPages).map(page => (
        <PaginationButton
            key={page.toString()}
            tabIndex={0}
            eventKey={page}
            label={page}
            active={page === activePage}
            onSelect={onSelect}
        />
    ))
}

// Компонент интерфейса разбивки по страницам

/**
* @example
* <Pagination
*     onSelect={this.changePage}
*     activePage={datasource.page}
*     count={datasource.count}
*     size={datasource.size}
*     maxPages={5}
* />
*/

const PaginationComponent = ({
    layout = Layout.separated,
    activePage = 1,
    count = 1,
    size = 1,
    maxPages = 5,
    first = false,
    last = false,
    prev = false,
    next = false,
    showCount = true,
    showSinglePage = false,
    onSelect = () => {},
    className,
    prevIcon = 'fa fa-angle-left',
    prevLabel = '',
    nextIcon = 'fa fa-angle-right',
    nextLabel = '',
    firstIcon = 'fa fa-angle-double-left',
    firstLabel = '',
    lastIcon = 'fa fa-angle-double-right',
    lastLabel = '',
    style = { display: 'flex', alignItems: 'baseline' },
    t = (str: string) => '',
}: PaginationProps) => {
    const pages = Math.ceil(count / size) || 1
    const lastPage = Math.ceil(count / size)

    return (
        <nav
            className={classNames('n2o-pagination', className)}
            style={style}
        >
            {showSinglePage || pages > 1 ? (
                <ul className={classNames('pagination', 'd-inline-flex', layout)}>
                    {first && getControlButton(
                        {
                            buttonType: ButtonType.first,
                            icon: firstIcon,
                            label: firstLabel,
                            activePage,
                            onSelect,
                            lastPage,
                        },
                    )}
                    {prev && getControlButton(
                        {
                            buttonType: ButtonType.prev,
                            icon: prevIcon,
                            label: prevLabel,
                            activePage,
                            onSelect,
                            lastPage,
                        },
                    )}
                    {renderBodyPaging({ activePage, maxPages, onSelect, totalPages: pages })}
                    {next && getControlButton(
                        {
                            buttonType: ButtonType.next,
                            icon: nextIcon,
                            label: nextLabel,
                            activePage,
                            onSelect,
                            lastPage,
                        },
                    )}
                    {last && getControlButton(
                        {
                            buttonType: ButtonType.last,
                            icon: lastIcon,
                            label: lastLabel,
                            activePage,
                            onSelect,
                            lastPage,
                        },
                    )}
                </ul>
            ) : null}

            {showCount && (
                <span
                    className="n2o-pagination-total"
                    style={{
                        paddingLeft: showSinglePage || pages > 1 ? '1rem' : 0,
                        display: 'inline-flex',
                    }}
                >
                    {`${t('paginationTotal')} ${count} ${t('paginationCount', { count })}`}
                </span>
            )}
        </nav>
    )
}

export const Pagination = withTranslation()(PaginationComponent)
