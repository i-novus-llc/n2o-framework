import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'

import { PaginationButton } from './PaginationButton'

/**
 * Компонент интерфейса разбивки по страницам
 * @reactProps {boolean} prev - показать/скрыть кнопку быстрого перехода на предыдущую страницу
 * @reactProps {boolean} prevIcon - Вид иконки быстрого перехода на предудущую страницу
 * @reactProps {boolean} prevLabel - текс кнопки
 * @reactProps {boolean} next - показать/скрыть кнопку быстрого перехода на следующую страницу
 * @reactProps {boolean} nextIcon - Вид иконки быстрого перехода на следующую страницу
 * @reactProps {boolean} nextLabel - текст кнопки
 * @reactProps {boolean} first - показать/скрыть кнопку быстрого перехода на первую страницу
 * @reactProps {boolean} firstIcon - Вид иконки быстрого перехода на первую страницу
 * @reactProps {boolean} firstLabel - текст кнопки
 * @reactProps {boolean} last - показать/скрыть кнопку быстрого перехода на последнюю страницу
 * @reactProps {boolean} lastIcon - Вид иконки быстрого перехода на последнюю страницу
 * @reactProps {boolean} lastLabel - текст кнопки
 * @reactProps {boolean} showCount - показать индикатор общего кол-ва записей
 * @reactProps {boolean} showSinglePage - показывать компонент, если страница единственная
 * @reactProps {number} maxPages - максимальное кол-во отображаемых кнопок перехода между страницами
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
 *             maxPages={5}/>
 */
class Pagination extends React.Component {
    /**
     * Рендер тела компонента. Алгоритм автоматически высчитывает страницы до и после текущей
     * @param activePage
     * @param pages
     * @param maxPages
     * @param onSelect
     * @param totalPages
     * @returns {Array} - вовзращает список кнопок
     */
    renderBodyPaging = (activePage, pages, maxPages, onSelect, totalPages) => {
        const getPager = (totalPages, currentPage, maxPages) => {
            let startPage
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

    /**
     * Базовый рендер компонента
     */
    render() {
        const {
            layout,
            activePage,
            count,
            size,
            maxPages,
            first,
            last,
            prev,
            next,
            showCount,
            showSinglePage,
            onSelect,
            className,
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
        const pages = Math.ceil(count / size) || 1
        const lastPage = Math.ceil(count / size)

        const getLabel = (direction, icon, label) => {
            if (icon && label) {
                if (direction === 'left') {
                    return (
                        <>
                            <span className={icon} aria-hidden="true" />
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
                            <span className={icon} aria-hidden="true" />
                        </>
                    )
                }
            }

            if (icon) {
                return (<i className={icon} aria-hidden="true" />)
            }

            return label
        }

        const getFirst = () => {
            const label = getLabel('left', firstIcon, firstLabel)

            return (
                <PaginationButton
                    eventKey={1}
                    label={label}
                    disabled={activePage === 1}
                    onSelect={onSelect}
                    tabIndex={0}
                />
            )
        }

        const getPrev = () => {
            const label = getLabel('left', prevIcon, prevLabel)

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

        const getNext = () => {
            const label = getLabel('right', nextIcon, nextLabel)

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

        const getLast = () => {
            const label = getLabel('right', lastIcon, lastLabel)

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
                    <ul className={classNames('pagination', 'd-inline-flex', className, layout)}>
                        {first && getFirst()}
                        {prev && getPrev()}
                        {this.renderBodyPaging(
                            activePage,
                            pages,
                            maxPages,
                            onSelect,
                            pages,
                        )}
                        {next && getNext()}
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
     *  Стиль
     * */
    layout: PropTypes.string,
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
    layout: 'bordered-rounded',
    prev: false,
    prevIcon: 'fa fa-angle-left',
    prevLabel: '',
    next: false,
    nextIcon: 'fa fa-angle-right',
    nextLabel: '',
    first: false,
    firstIcon: 'fa fa-angle-double-left',
    firstLabel: '',
    last: false,
    lastIcon: 'fa fa-angle-double-right',
    lastLabel: '',
    showCount: true,
    showSinglePage: false,
    maxPages: 5,
    count: 1,
    size: 1,
    activePage: 1,
    t: () => {},
}

export default withTranslation()(Pagination)
