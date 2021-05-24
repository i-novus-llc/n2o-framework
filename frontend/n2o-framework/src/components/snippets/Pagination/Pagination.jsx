import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'

import { PaginationButton } from './PaginationButton'

/**
 * Компонент интерфейса разбивки по страницам
 * @reactProps {boolean} prev - показать/скрыть кнопку быстрого перехода на предыдущую страницу
 * @reactProps {boolean} prevLabel - текс кнопки
 * @reactProps {boolean} next - показать/скрыть кнопку быстрого перехода на следующую страницу
 * @reactProps {boolean} nextLabel - текст кнопки
 * @reactProps {boolean} first - показать/скрыть кнопку быстрого перехода на первую страницу
 * @reactProps {boolean} last - показать/скрыть кнопку быстрого перехода на последнюю страницу
 * @reactProps {boolean} withoutBody - скрыть тело пагинации
 * @reactProps {boolean} showCount - показать индикатор общего кол-ва записей
 * @reactProps {boolean} showSinglePage - показывать компонент, если страница единственная
 * @reactProps {number} maxPages - максимальное кол-во кнопок перехода между страницами
 * @reactProps {number} stepIncrement - шаг дополнительной кнопки (1,2.3 ... 11)
 * @reactProps {number} count - общее кол-во записей
 * @reactProps {number} size - кол-во записей на одной странице
 * @reactProps {number} activePage - номер активной страницы
 * @reactProps {function} onSelect - callback нажатия по кнопке страницы
 * @reactProps {string} className - класс для списка внутри nav
 * @example
 * <Pagination onSelect={this.changePage}
 *             activePage={datasource.page}
 *             count={datasource.count}
 *             size={datasource.size}
 *             maxPages={4}
 *             stepIncrement={10} />
 */
class Pagination extends React.Component {
    /**
     * Рендер тела компонента. Алгоритм автоматически высчитывает страницы до и после текущей
     * @param activePage
     * @param pages
     * @param maxPages
     * @param stepIncrement
     * @param onSelect
     * @returns {Array} - вовзращает список кнопок
     */
    renderBodyPaging = (activePage, pages, maxPages, stepIncrement, onSelect) => {
        const pageButtons = []

        let startPage
        let endPage

        if (maxPages && maxPages < pages) {
            startPage = Math.max(
                Math.min(
                    activePage - Math.floor(maxPages / 2, 10),
                    pages - maxPages + 1,
                ),
                1,
            )
            endPage = startPage + maxPages
        } else {
            startPage = 1
            endPage = maxPages
        }

        if (endPage > pages) {
            endPage = pages
        }

        for (let page = startPage; page <= endPage; ++page) {
            pageButtons.push(
                <PaginationButton
                    key={page}
                    tabIndex={0}
                    eventKey={page}
                    label={page}
                    active={page === activePage}
                    onSelect={onSelect}
                />,
            )
        }

        if (stepIncrement && endPage < pages - 1) {
            pageButtons.push(
                <PaginationButton
                    label="..."
                    tabIndex={-1}
                    key="ellipsisMiddle"
                    noBorder
                    disabled
                />,
            )
            pageButtons.push(
                <PaginationButton
                    tabIndex={0}
                    key={
                        activePage + stepIncrement > pages
                            ? pages
                            : activePage + stepIncrement
                    }
                    eventKey={
                        activePage + stepIncrement > pages
                            ? pages
                            : activePage + stepIncrement
                    }
                    label={
                        activePage + stepIncrement > pages
                            ? pages
                            : activePage + stepIncrement
                    }
                    onSelect={onSelect}
                />,
            )
            if (activePage + stepIncrement < pages) {
                pageButtons.push(
                    <PaginationButton
                        label="..."
                        tabIndex={-1}
                        key="ellipsisLast"
                        noBorder
                        disabled
                    />,
                )
            }
        } else if (stepIncrement && endPage === pages - 1) {
            pageButtons.push(
                <PaginationButton
                    key={pages}
                    eventKey={pages}
                    label={pages}
                    onSelect={onSelect}
                />,
            )
        }

        if (startPage > 1) {
            if (startPage > 2) {
                pageButtons.unshift(
                    <PaginationButton
                        label="..."
                        key="ellipsisFirst"
                        tabIndex={-1}
                        noBorder
                        disabled
                    />,
                )
            }

            pageButtons.unshift(
                <PaginationButton
                    key={1}
                    eventKey={1}
                    label="1"
                    tabIndex={0}
                    onSelect={onSelect}
                />,
            )
        }

        return pageButtons
    }

    /**
     * Базовый рендер компонента
     */
    render() {
        const {
            activePage,
            count,
            size,
            maxPages,
            stepIncrement,
            first,
            last,
            prev,
            next,
            showCount,
            showSinglePage,
            onSelect,
            className,
            withoutBody,
            prevIcon,
            prevLabel,
            nextIcon,
            nextLabel,
            firstIcon,
            firstLabel,
            lastIcon,
            lastLabel,
            t,
        } = this.props
        const pages = Math.ceil(count / size, 10) || 1
        const lastPage = Math.ceil(count / size)

        const getViewButtonPrev = () => {
            const leftArrows = {
                none: '',
                short: '&lsaquo;',
                long: '&xlarr;',
            }

            let label

            if (prevIcon && prevLabel) {
                label = `${leftArrows[prevIcon]}&nbsp;${prevLabel}`
            } else if (prevIcon) {
                label = `${leftArrows[prevIcon]}`
            } else if (prevLabel) {
                label = prevLabel
            }

            return (
                <PaginationButton
                    eventKey={activePage - 1}
                    label={label}
                    disabled={activePage === 1}
                    onSelect={onSelect}
                    tabIndex={0}
                />
            )
        }

        const getViewButtonNext = () => {
            const rightArrows = {
                none: '',
                short: '&rsaquo;',
                long: '&xrarr;',
            }

            let label

            if (nextIcon && nextLabel) {
                label = `${nextLabel}&nbsp;${rightArrows[nextIcon]}`
            } else if (nextIcon) {
                label = `${rightArrows[nextIcon]}`
            } else if (nextLabel) {
                label = nextLabel
            }

            return (
                <PaginationButton
                    eventKey={activePage + 1}
                    label={label}
                    disabled={lastPage === activePage}
                    onSelect={onSelect}
                    tabIndex={0}
                />
            )
        }

        const getFirst = () => {
            const firstArrows = {
                none: '',
                short: '&laquo;',
                long: '&larrb;',
            }

            let label

            if (firstIcon && firstLabel) {
                label = `${firstArrows[firstIcon]}&nbsp;${firstLabel}`
            } else if (firstIcon) {
                label = `${firstArrows[firstIcon]}`
            } else if (firstLabel) {
                label = firstLabel
            }

            return (
                <PaginationButton
                    eventKey={1}
                    label={label}
                    disabled={activePage === 1}
                    onSelect={onSelect}
                    /* eslint-disable-next-line jsx-a11y/tabindex-no-positive */
                    tabIndex={1}
                />
            )
        }

        const getLast = () => {
            const lastArrows = {
                none: '',
                short: '&raquo;',
                long: '&rarrb;',
            }

            let label

            if (lastIcon && lastLabel) {
                label = `${lastLabel}&nbsp;${lastArrows[lastIcon]}`
            } else if (lastIcon) {
                label = `${lastArrows[lastIcon]}`
            } else if (lastLabel) {
                label = lastLabel
            }

            return (
                <PaginationButton
                    eventKey={lastPage}
                    label={label}
                    disabled={lastPage === activePage}
                    onSelect={onSelect}
                    tabIndex={0}
                />
            )
        }

        return (
            <nav
                className="n2o-pagination"
                style={{ display: 'flex', alignItems: 'baseline' }}
            >
                {!showSinglePage && pages === 1 ? null : (
                    <ul className={classNames('pagination', 'd-inline-flex', className)}>
                        {first && getFirst()}
                        {prev && getViewButtonPrev()}
                        {!withoutBody &&
                                this.renderBodyPaging(
                                    activePage,
                                    pages,
                                    maxPages,
                                    stepIncrement,
                                    onSelect,
                                )}
                        {next && getViewButtonNext()}
                        {last && getLast()}
                    </ul>
                )}
                {/* eslint-disable react/jsx-one-expression-per-line */}
                {showCount && (
                    <span
                        className="n2o-pagination-info"
                        style={{
                            paddingLeft: !showSinglePage && pages === 1 ? 0 : '1rem',
                            display: 'inline-flex',
                        }}
                    >
                        {`${t('paginationTotal')} ${count}`}

                                &nbsp;
                        {t('paginationInterval', { postProcess: 'interval', count })}
                    </span>
                )}
            </nav>
        )
    }
}

Pagination.propTypes = {
    /**
     * Показать/скрыть кнопку быстрого перехода на предыдущую страницу
     */
    prev: PropTypes.bool,
    /**
     * Вид иконки быстрого перехода на предудущую страницу
     * */
    prevIcon: PropTypes.string,
    /**
     * Текст кнопки 'Назад'
     */
    prevLabel: PropTypes.string,
    /**
     * Показать/скрыть кнопку быстрого перехода на следующую страницу
     */
    next: PropTypes.bool,
    /**
     * Вид иконки быстрого перехода на следующую страницу
     */
    nextIcon: PropTypes.string,
    /**
     * Текст кнопки 'Вперед'
     */
    nextLabel: PropTypes.string,
    /**
     * Показать/скрыть кнопку быстрого перехода на первую страницу
     */
    first: PropTypes.bool,
    /**
     * Вид иконки быстрого перехода на первую страницу
     * */
    firstIcon: PropTypes.string,
    /**
     * Текст кнопки 'First'
     */
    firstLabel: PropTypes.string,
    /**
     * Показать/скрыть кнопку быстрого перехода на последнюю страницу
     */
    last: PropTypes.bool,
    /**
     * Вид иконки быстрого перехода на последнюю страницу
     * */
    lastIcon: PropTypes.string,
    /**
     * Текст кнопки 'Last'
     */
    lastLabel: PropTypes.string,
    /**
     * Скрыть тело пагинации
     */
    withoutBody: PropTypes.bool,
    /**
     * Показать индикатор общего кол-ва записей
     */
    showCount: PropTypes.bool,
    /**
     * Скрывать компонент, если страница единственная
     */
    showSinglePage: PropTypes.bool,
    /**
     * Максимальное кол-во кнопок перехода между страницами
     */
    maxPages: PropTypes.number,
    /**
     * Шаг дополнительной кнопки (1,2.3 ... 11)
     */
    stepIncrement: PropTypes.number,
    /**
     * Общее кол-во записей
     */
    count: PropTypes.number,
    /**
     * Кол-во записей на одной странице
     */
    size: PropTypes.number,
    /**
     * Номер активной страницы
     */
    activePage: PropTypes.number,
    /**
     * Сallback нажатия по кнопке страницы
     */
    onSelect: PropTypes.func,
    /**
     * Класс для списка внутри nav
     */
    className: PropTypes.string,
    t: PropTypes.func,
}

Pagination.defaultProps = {
    prev: false,
    prevIcon: 'long',
    prevLabel: 'Previous',
    next: false,
    nextIcon: 'long',
    nextLabel: 'Next',
    first: false,
    firstIcon: 'long',
    firstLabel: '',
    last: false,
    lastIcon: 'long',
    lastLabel: '',
    withoutBody: false,
    showCount: true,
    showSinglePage: false,
    maxPages: 4,
    count: 1,
    size: 1,
    activePage: 1,
    t: () => {},
}

export default withTranslation()(Pagination)
